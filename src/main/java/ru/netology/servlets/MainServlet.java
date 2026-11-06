package ru.netology.servlets;

import ru.netology.servlets.controller.PostController;
import ru.netology.servlets.exception.PostNotFoundException;
import ru.netology.servlets.repository.PostRepository;
import ru.netology.servlets.service.PostService;

import com.google.gson.JsonParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MainServlet extends HttpServlet {

    private static final String POSTS_PATH = "/posts";
    private static final String POST_ID_PREFIX = "/posts/";

    private PostController controller;

    // Инициализация приложения
    @Override
    public void init() throws ServletException {

        PostRepository repository = new PostRepository();

        PostService service = new PostService(repository);

        controller = new PostController(service);
    }

    // Диспетчеризация запросов
    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String method = request.getMethod();
        String path = request.getPathInfo();

        try {

            if (path == null) {
                controller.sendError(
                        response,
                        HttpServletResponse.SC_NOT_FOUND,
                        "Endpoint not found"
                );
                return;
            }

            // GET /api/posts
            if (HttpMethod.GET.equals(method)
                    && POSTS_PATH.equals(path)) {

                controller.all(response);
                return;
            }

            // GET /api/posts/{id}
            if (HttpMethod.GET.equals(method)
                    && path.startsWith(POST_ID_PREFIX)) {

                long id = extractId(path);

                controller.getById(id, response);
                return;
            }

            // POST /api/posts
            if (HttpMethod.POST.equals(method)
                    && POSTS_PATH.equals(path)) {

                controller.save(request, response);
                return;
            }

            // PUT /api/posts/{id}
            if (HttpMethod.PUT.equals(method)
                    && path.startsWith(POST_ID_PREFIX)) {

                long id = extractId(path);

                controller.update(id, request, response);
                return;
            }

            // DELETE /api/posts/{id}
            if (HttpMethod.DELETE.equals(method)
                    && path.startsWith(POST_ID_PREFIX)) {

                long id = extractId(path);

                controller.removeById(id, response);
                return;
            }

            controller.sendError(
                    response,
                    HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "Method or endpoint not supported"
            );

        } catch (PostNotFoundException e) {

            controller.sendError(
                    response,
                    HttpServletResponse.SC_NOT_FOUND,
                    e.getMessage()
            );

        } catch (JsonParseException | IllegalArgumentException e) {

            controller.sendError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    // Извлечение ID из URL
    private long extractId(String path) {

        String idValue = path.substring(POST_ID_PREFIX.length());

        long id = Long.parseLong(idValue);

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid post ID");
        }

        return id;
    }
}