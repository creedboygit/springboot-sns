package com.valletta.sns.controller;

import com.valletta.sns.controller.request.PostCreateRequest;
import com.valletta.sns.controller.request.PostModifyRequest;
import com.valletta.sns.controller.response.Response;
import com.valletta.sns.model.PostDto;
import com.valletta.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    public Response<Void> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication) {

        PostDto postDto = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);
        return Response.success();
    }

}
