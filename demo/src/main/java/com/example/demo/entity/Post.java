package com.example.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 100)
    private String category;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private int viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    protected Post() {}

    private Post(Builder builder) {
        this.title     = builder.title;
        this.content   = builder.content;
        this.category  = builder.category;
        this.imageUrl  = builder.imageUrl;
        this.author    = builder.author;
        this.viewCount = 0;
        this.comments  = new ArrayList<>();
    }

    // Getters
    public Long getId()               { return id; }
    public String getTitle()          { return title; }
    public String getContent()        { return content; }
    public String getCategory()       { return category; }
    public String getImageUrl()       { return imageUrl; }
    public int getViewCount()         { return viewCount; }
    public User getAuthor()           { return author; }
    public List<Comment> getComments(){ return comments; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
    public LocalDateTime getUpdatedAt(){ return updatedAt; }

    // 업데이트용 메서드 (Setter 대신)
    public void update(String title, String content, String category, String imageUrl) {
        this.title    = title;
        this.content  = content;
        this.category = category;
        if (imageUrl != null) this.imageUrl = imageUrl;
    }

    public void incrementViewCount() { this.viewCount++; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String title;
        private String content;
        private String category;
        private String imageUrl;
        private User   author;

        public Builder title(String title)       { this.title = title;       return this; }
        public Builder content(String content)   { this.content = content;   return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public Builder author(User author)       { this.author = author;     return this; }

        public Post build() { return new Post(this); }
    }
}
