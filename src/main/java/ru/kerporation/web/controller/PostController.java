package ru.kerporation.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kerporation.model.Post;
import ru.kerporation.service.PostService;
import ru.kerporation.web.dto.PostDto;
import ru.kerporation.web.mapper.PostMapper;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto create(@Valid @RequestBody PostDto dto) {
        Post post = postMapper.toEntity(dto);
        post = postService.createPost(post);
        return postMapper.toDto(post);
    }

    @GetMapping
    public List<PostDto> getAll() {
        List<Post> posts = postService.getAll();
        return postMapper.toDto(posts);
    }

    @GetMapping("/{id}")
    public PostDto getById(@PathVariable long id) {
        Post post = postService.findPostById(id);
        return postMapper.toDto(post);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        postService.deletePost(id);
    }

}