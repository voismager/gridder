package com.example.grid.controller;

import com.example.grid.data.payload.*;
import com.example.grid.data.payload.post.CreatePost;
import com.example.grid.data.payload.post.GetPost;
import com.example.grid.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostRestController {
    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody CreatePost data, @AuthenticationPrincipal UserDetails details) {
        this.postService.create(data, details.getUsername());
    }

    @RequestMapping(path = "/search", method = RequestMethod.POST)
    public Page<GetPost> search(@RequestBody AdvanceSearchRequest request) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        return this.postService.advanceSearch(
                request.getQuery(),
                PageRequest.of(request.getPage(), request.getSize(), sort)
        );
    }
}
