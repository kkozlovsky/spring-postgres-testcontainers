package ru.kerporation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.kerporation.model.Post;
import ru.kerporation.model.exception.PostNotFoundException;
import ru.kerporation.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post findPostById(long postId) {
        final Post post = postRepository
                .findById(postId)
                .orElseThrow(PostNotFoundException::new);

        postRepository.addView(postId);
        return post;
    }

    @Override
    public List<Post> getAll() {
        final List<Post> postList = postRepository.findAll(Sort.by("postId").descending());
        postList.forEach(post -> postRepository.addView(post.getPostId()));
        return postList;
    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deletePost(long postId) {
        postRepository.deleteById(postId);
    }
}
