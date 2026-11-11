package ru.netology.servlets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.netology.servlets.controller.PostController;
import ru.netology.servlets.repository.PostRepository;
import ru.netology.servlets.service.PostService;

@Configuration // Сообщаем Spring: "Это класс с настройками"
public class AppConfig {

    @Bean
    public PostRepository postRepository() {
        return new PostRepository();
    }

    @Bean
    public PostService postService() {
        // МАГИЯ SPRING (из лекции): "все вызовы к этим методам перехватываются
        // (паттерн Proxy) и заменяются на dependency injection".
        // Мы просто вызываем метод postRepository(). Spring НЕ создаст второй репозиторий,
        // а подставит тот самый единственный бин, который он уже создал выше.
        return new PostService(postRepository());
    }

    @Bean
    public PostController postController() {
        // Точно так же Spring подставит уже созданный бин PostService
        return new PostController(postService());
    }
}