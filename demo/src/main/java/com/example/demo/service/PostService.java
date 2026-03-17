package com.example.demo.service;

import com.example.demo.dto.PostSaveRequestDto;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Post> getPostsByCategory(String category, Pageable pageable) {
        return postRepository.findByCategoryOrderByCreatedAtDesc(category, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Post> searchPosts(String keyword, Pageable pageable) {
        return postRepository.findByTitleContainingOrContentContainingOrderByCreatedAtDesc(keyword, keyword, pageable);
    }

    @Transactional(readOnly = true)
    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + id));
    }

    @Transactional
    public Post getPostAndIncrementView(Long id) {
        Post post = getPost(id);
        post.incrementViewCount();
        return post;
    }

    @Transactional
    public Post save(PostSaveRequestDto dto, MultipartFile image, User author) throws IOException {
        String imageUrl = (image != null && !image.isEmpty()) ? saveImage(image) : null;
        Post post = Post.builder()
                .title(dto.title())
                .content(dto.content())
                .category(dto.category())
                .imageUrl(imageUrl)
                .author(author)
                .build();
        return postRepository.save(post);
    }

    @Transactional
    public Post update(Long id, PostSaveRequestDto dto, MultipartFile image) throws IOException {
        Post post = getPost(id);
        String imageUrl = (image != null && !image.isEmpty()) ? saveImage(image) : null;
        post.update(dto.title(), dto.content(), dto.category(), imageUrl);
        return post;
    }

    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Post> getRecentPosts() {
        return postRepository.findTop5ByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Post> getPopularPosts() {
        return postRepository.findTop5ByOrderByViewCountDesc();
    }

    private String saveImage(MultipartFile file) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        file.transferTo(new File(dir, filename));
        return "/uploads/" + filename;
    }
}
