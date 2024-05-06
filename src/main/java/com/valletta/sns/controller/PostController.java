package com.valletta.sns.controller;

import com.valletta.sns.controller.request.PostCommentRequest;
import com.valletta.sns.controller.request.PostCreateRequest;
import com.valletta.sns.controller.request.PostModifyRequest;
import com.valletta.sns.controller.response.PostResponse;
import com.valletta.sns.controller.response.Response;
import com.valletta.sns.model.PostDto;
import com.valletta.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {

        postService.create(request.getTitle(), request.getBody(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable("postId") Integer postId, @RequestBody PostModifyRequest request, Authentication authentication) {

        PostDto postDto = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);
        return Response.success(PostResponse.fromPostDto(postDto));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable("postId") Integer postId, Authentication authentication) {

        postService.delete(authentication.getName(), postId);
        return Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable, Authentication authentication) {

        return Response.success(postService.list(pageable).map(PostResponse::fromPostDto));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> my(Pageable pageable, Authentication authentication) {

        return Response.success(postService.my(authentication.getName(), pageable).map(PostResponse::fromPostDto));
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable("postId") Integer postId, Authentication authentication) {

        postService.like(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    public Response<Integer> likeCount(@PathVariable("postId") Integer postId, Authentication authentication) {

        return Response.success(postService.likeCount(postId));
    }

    @PostMapping("/{postId}/comment")
    public Response<Void> comment(@PathVariable("postId") Integer postId, Authentication authentication, PostCommentRequest request) {

        postService.comment(postId, authentication.getName(), request.getComment());
        return Response.success();
    }
}
