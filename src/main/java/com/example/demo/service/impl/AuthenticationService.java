package com.example.demo.service.impl;

import com.example.demo.dto.AuthenticationRequestDto;
import com.example.demo.dto.AuthenticationResponseDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.entity.User;
import com.example.demo.exception.ApiRequestException;
import com.example.demo.repository.UserRepository;
import com.example.demo.token.Token;
import com.example.demo.token.TokenRepository;
import com.example.demo.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repo;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;

    private final static String BEARER = "Bearer ";

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        // authenticate user request
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // find user by email orElse throw Exception
        User user = repo.findByEmail(request.getEmail()).orElseThrow();

        // revoke user token before generate new one.
        revokeAllUserToken(user);

        // generate token
        String accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // save generated token
        saveUserToken(user, accessToken);

        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponseDto register(RegisterRequestDto request) {
        User userRequest = this.repo.findByEmail(request.getEmail()).orElse(null);
        if (Objects.nonNull(userRequest)) {
            throw new ApiRequestException("User email already existed!");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .passwd(passwordEncoder.encode(request.getPassword())) // get password encoder then save.
                .role(request.getRole())
                .build();

        // save to db
        var savedUser = repo.save(user);

        // generate token
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // save generated token
        saveUserToken(savedUser, accessToken);

        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Do save user token
     *
     * @param savedUser User
     * @param jwtToken Token string
     */
    private void saveUserToken(User savedUser, String jwtToken) {
        Token token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    /**
     * Do revoke all user valid token
     *
     * @param user User
     */
    private void revokeAllUserToken(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        // empty check valid user tokens before revoke
        if (validUserTokens.isEmpty()) {
            return;
        }

        validUserTokens.forEach(e -> {
            e.setRevoked(true);
            e.setExpired(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        // check jwt token when null.
        if (Objects.isNull(authHeader)
                || !authHeader.startsWith(BEARER)) {
            return;
        }

        // extract token from header
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUserName(refreshToken);

        /* check user exist on DB.
         no need to check user authentication again when get refresh token
         but get user from db by using findByEmail */
        if (Objects.nonNull(userEmail)) {
            User user = this.repo.findByEmail(userEmail).orElseThrow();

            // check token is valid and update context holder and send back to dispatcher Servlet
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);

                // revoke all access token currently available
                revokeAllUserToken(user);
                saveUserToken(user, accessToken);

                var authResponse = AuthenticationResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                // write body of the response
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}
