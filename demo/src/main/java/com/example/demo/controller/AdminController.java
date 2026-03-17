package com.example.demo.controller;

import com.example.demo.config.PrincipalDetail;
import com.example.demo.dto.PostSaveRequestDto;
import com.example.demo.entity.Post;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PostService postService;

    @GetMapping("/write")
    public String writePage() {
        return "admin/write";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute PostSaveRequestDto dto,
                        @RequestParam(required = false) MultipartFile image,
                        @AuthenticationPrincipal PrincipalDetail principal) throws Exception {
        Post post = postService.save(dto, image, principal.getUser());
        return "redirect:/post/" + post.getId();
    }

    @GetMapping("/update/{id}")
    public String updatePage(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "admin/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute PostSaveRequestDto dto,
                         @RequestParam(required = false) MultipartFile image) throws Exception {
        postService.update(id, dto, image);
        return "redirect:/post/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/post/list";
    }
}
