package ru.kerporation.service;

import ru.kerporation.model.Post;

import java.util.List;

public interface PostService {

    Post findPostById(final long postId);
    List<Post> getAll();
    Post createPost(final Post post);
    void deletePost(final long postId);
}
