package ru.kerporation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kerporation.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = """
            UPDATE posts
            SET views_count = views_count + 1
            WHERE post_id = :postId
            """, nativeQuery = true)
    @Modifying
    void addView(@Param("postId") final long postId);
}
