package ru.netology.servlets.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(long id) {
        super("Post with id " + id + " not found");
    }
}