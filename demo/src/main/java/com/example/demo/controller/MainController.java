package com.example.demo.controller;

import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("recentPosts", postService.getRecentPosts());
        model.addAttribute("popularPosts", postService.getPopularPosts());
        return "index";
    }
}
