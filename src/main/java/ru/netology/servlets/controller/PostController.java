package ru.netology.servlets.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.netology.servlets.model.Post;
import ru.netology.servlets.service.PostService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PostController {

    private static final String APPLICATION_JSON = "application/json";

    private final PostService service;

    private final Gson gson = new Gson();

    public PostController(PostService service) {
        this.service = service;
    }

    // Получить все посты
    public void all(HttpServletResponse response) throws IOException {

        List<Post> data = service.all();

        writeJson(response, HttpServletResponse.SC_OK, data);
    }

    // Получить пост по ID
    public void getById(long id,
                        HttpServletResponse response) throws IOException {

        Post data = service.getById(id);

        writeJson(response, HttpServletResponse.SC_OK, data);
    }

    // Создать или обновить пост
    public void save(HttpServletRequest request,
                     HttpServletResponse response) throws IOException {

        Post post = gson.fromJson(request.getReader(), Post.class);

        if (post == null) {
            throw new IllegalArgumentException("Request body is empty");
        }

        boolean isNew = post.getId() == 0;

        Post savedPost = service.save(post);

        int status = isNew
                ? HttpServletResponse.SC_CREATED
                : HttpServletResponse.SC_OK;

        writeJson(response, status, savedPost);
    }

    // Обновить пост по ID
    public void update(long id,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {

        Post post = gson.fromJson(request.getReader(), Post.class);

        if (post == null) {
            throw new IllegalArgumentException("Request body is empty");
        }

        post.setId(id);

        Post updatedPost = service.save(post);

        writeJson(response, HttpServletResponse.SC_OK, updatedPost);
    }

    // Удалить пост
    public void removeById(long id,
                           HttpServletResponse response) {

        service.removeById(id);

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    // Отправить JSON
    public void writeJson(HttpServletResponse response,
                          int status,
                          Object data) throws IOException {

        response.setStatus(status);
        response.setContentType(APPLICATION_JSON);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(gson.toJson(data));
    }

    // Отправить JSON с ошибкой
    public void sendError(HttpServletResponse response,
                          int status,
                          String message) throws IOException {

        writeJson(response, status, Map.of("error", message));
    }
}