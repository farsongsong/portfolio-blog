package com.example.demo.controller;

import com.example.demo.config.PrincipalDetail;
import com.example.demo.entity.Post;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String category,
                       @RequestParam(defaultValue = "") String keyword,
                       Model model) {

        Pageable pageable = PageRequest.of(page, 6);
        Page<Post> posts;

        if (!keyword.isBlank()) {
            posts = postService.searchPosts(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else if (!category.isBlank()) {
            posts = postService.getPostsByCategory(category, pageable);
            model.addAttribute("category", category);
        } else {
            posts = postService.getPosts(pageable);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        return "post/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         @AuthenticationPrincipal PrincipalDetail principal,
                         Model model) {
        // 비로그인 사용자는 로그인 유도 페이지로
        if (principal == null) {
            model.addAttribute("redirectUrl", "/post/" + id);
            return "post/login-required";
        }

        Post post = postService.getPostAndIncrementView(id);
        model.addAttribute("post", post);
        model.addAttribute("comments", commentService.getComments(id));
        return "post/detail";
    }
}
