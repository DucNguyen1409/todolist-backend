package com.example.demo.config;

import com.example.demo.service.impl.JwtService;
import com.example.demo.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthentication extends OncePerRequestFilter {

    private final static String AUTHORIZATION = "Authorization";
    private final static String BEARER = "Bearer ";

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwt;
        final String userEmail;

        // check jwt token when null.
        if (Objects.isNull(authHeader)
                || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        // extract token from header
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUserName(jwt);

        // check user exist on DB.
        // SecurityContextHolder to get user is already authenticate or not
        if (Objects.nonNull(userEmail)
                && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            var isTokenActive = tokenRepository.findByToken(jwt)
                                            .map(t -> !t.getExpired() && !t.getRevoked())
                                            .orElse(false);

            // check token is valid and update context holder and send back to dispatcher Servlet
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenActive) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // update context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // do finalize the filter
        filterChain.doFilter(request, response);
    }

}







