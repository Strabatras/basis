package ru.dedus;

import java.util.Arrays;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.dedus.service.UserService;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);

        UserService userService = context.getBean(UserService.class);

        userService.createUser("James");

        var names = Arrays.asList("Michael", "Robert", "John", "David", "William");
        userService.createUser(names);
    }
}
