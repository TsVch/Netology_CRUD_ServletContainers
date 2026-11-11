package ru.netology.servlets.repository;

import ru.netology.servlets.exception.PostNotFoundException;
import ru.netology.servlets.model.Post;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PostRepository {

    private final Map<Long, Post> posts = new LinkedHashMap<>();

    private long nextId = 1;

    // Получить все посты
    public synchronized List<Post> all() {
        List<Post> result = new ArrayList<>();

        for (Post post : posts.values()) {
            result.add(copy(post));
        }

        return result;
    }

    // Получить пост по ID
    public synchronized Optional<Post> getById(long id) {
        Post post = posts.get(id);

        if (post == null) {
            return Optional.empty();
        }

        return Optional.of(copy(post));
    }

    // Создать новый пост или обновить существующий
    public synchronized Post save(Post post) {

        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }

        // Создание нового поста
        if (post.getId() == 0) {

            Post newPost = new Post(nextId++, post.getContent());

            posts.put(newPost.getId(), newPost);

            return copy(newPost);
        }

        // Обновление существующего поста
        if (!posts.containsKey(post.getId())) {
            throw new PostNotFoundException(post.getId());
        }

        Post updatedPost = copy(post);

        posts.put(updatedPost.getId(), updatedPost);

        return copy(updatedPost);
    }

    // Удаление поста
    public synchronized void removeById(long id) {

        if (!posts.containsKey(id)) {
            throw new PostNotFoundException(id);
        }

        posts.remove(id);
    }

    // Создание независимой копии объекта
    private Post copy(Post post) {
        return new Post(post.getId(), post.getContent());
    }
}