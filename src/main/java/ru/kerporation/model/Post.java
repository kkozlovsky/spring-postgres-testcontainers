package ru.kerporation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "posts")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(length = 1024)
    private String title;

    @Column(length = 16384)
    private String text;

    private long viewsCount;

    public Post(final String title,
                final String text) {
        this.title = title;
        this.text = text;
    }
}
