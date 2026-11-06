package ru.netology.servlets.service;

import ru.netology.servlets.exception.PostNotFoundException;
import ru.netology.servlets.model.Post;
import ru.netology.servlets.repository.PostRepository;

import java.util.List;

public class PostService {

    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> all() {
        return repository.all();
    }

    public Post getById(long id) {
        return repository.getById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    public Post save(Post post) {

        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }

        if (post.getContent() == null || post.getContent().isBlank()) {
            throw new IllegalArgumentException("Post content cannot be empty");
        }

        return repository.save(post);
    }

    public void removeById(long id) {
        repository.removeById(id);
    }
}