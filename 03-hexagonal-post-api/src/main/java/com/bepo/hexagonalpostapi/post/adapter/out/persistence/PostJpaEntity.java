package com.bepo.hexagonalpostapi.post.adapter.out.persistence;

import com.bepo.hexagonalpostapi.post.domain.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class PostJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String title;

    @Column(length = 1000)
    private String content;

    public static PostJpaEntity from(Post post) {
        return PostJpaEntity.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public Post toDomain() {
        return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();
    }
}
