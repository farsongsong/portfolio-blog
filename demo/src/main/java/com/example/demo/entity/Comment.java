package com.example.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 50)
    private String authorName;

    @Column(length = 100)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    protected Comment() {}

    private Comment(Builder builder) {
        this.content    = builder.content;
        this.authorName = builder.authorName;
        this.password   = builder.password;
        this.post       = builder.post;
        this.user       = builder.user;
    }

    public Long getId()               { return id; }
    public String getContent()        { return content; }
    public String getAuthorName()     { return authorName; }
    public String getPassword()       { return password; }
    public Post getPost()             { return post; }
    public User getUser()             { return user; }
    public LocalDateTime getCreatedAt(){ return createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String content;
        private String authorName;
        private String password;
        private Post   post;
        private User   user;

        public Builder content(String content)     { this.content = content;       return this; }
        public Builder authorName(String name)     { this.authorName = name;       return this; }
        public Builder password(String password)   { this.password = password;     return this; }
        public Builder post(Post post)             { this.post = post;             return this; }
        public Builder user(User user)             { this.user = user;             return this; }

        public Comment build() { return new Comment(this); }
    }
}
