package com.example.demo;

import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.Status;
import com.example.demo.entity.Todo;
import com.example.demo.service.TodoListService;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService authService,
											   TodoListService todoListService,
											   UserService userService) {
		return args -> {
			// register user
			var admin = RegisterRequestDto.builder()
					.lastName("Admin")
					.email("Admin@ad")
					.firstName("ad")
					.password("1234")
					.role(Role.ADMIN)
					.build();
			System.out.println("Admin token: " + authService.register(admin).getAccessToken());
			var user = RegisterRequestDto.builder()
					.lastName("User")
					.email("User@us")
					.firstName("us")
					.password("1234")
					.role(Role.USER)
					.build();
			System.out.println("User token: " + authService.register(user).getAccessToken());


			// register mock data to-do
			var userEntity = userService.findByLastName("User");
			var sampleTodo1 = Todo.builder()
					.title("todo-01")
					.createdBy(userEntity.getId())
					.status(Status.NEW)
					.build();
			var sampleTodo2 = Todo.builder()
					.title("todo-02")
					.createdBy(userEntity.getId())
					.status(Status.NEW)
					.build();

			todoListService.saveToDo(sampleTodo1);
			todoListService.saveToDo(sampleTodo2);


			var adminEntity = userService.findByLastName("Admin");
			var sampleTodo3 = Todo.builder()
					.title("todo-01")
					.createdBy(adminEntity.getId())
					.status(Status.NEW)
					.build();
			var sampleTodo4 = Todo.builder()
					.title("todo-02")
					.createdBy(adminEntity.getId())
					.status(Status.NEW)
					.build();

			todoListService.saveToDo(sampleTodo3);
			todoListService.saveToDo(sampleTodo4);
		};
	}

}
