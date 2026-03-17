package com.example.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    protected User() {}

    private User(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.nickname = builder.nickname;
        this.role     = builder.role;
    }

    public Long getId()              { return id; }
    public String getUsername()      { return username; }
    public String getPassword()      { return password; }
    public String getNickname()      { return nickname; }
    public RoleType getRole()        { return role; }
    public LocalDateTime getCreatedAt(){ return createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String   username;
        private String   password;
        private String   nickname;
        private RoleType role;

        public Builder username(String username) { this.username = username; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder nickname(String nickname) { this.nickname = nickname; return this; }
        public Builder role(RoleType role)       { this.role = role;         return this; }

        public User build() { return new User(this); }
    }
}
