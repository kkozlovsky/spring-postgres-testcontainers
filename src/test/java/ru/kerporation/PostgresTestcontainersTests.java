package ru.kerporation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.kerporation.model.Post;
import ru.kerporation.repository.PostRepository;
import ru.kerporation.web.dto.PostDto;

import java.util.List;

@Testcontainers
@TestConfiguration(proxyBeanMethods = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostgresTestcontainersTests {

    @Container
    @ServiceConnection // c версии 3.2 автоматически параметры подсовывает
    final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1-alpine");
    @LocalServerPort
    private Integer port;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
        postRepository.deleteAll();
    }


    @Test
    void testEmptyGetAll() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/posts")
                .then()
                .statusCode(
                        HttpStatus.OK.value()
                )
                .body(
                        ".",
                        Matchers.empty()
                );
    }

    @Test
    void testNotEmptyGetAll() {
        final List<Post> posts = List.of(
                new Post("First title", "First text"),
                new Post("Second title", "Second text")
        );
        postRepository.saveAll(posts);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/posts")
                .then()
                .statusCode(
                        HttpStatus.OK.value()
                )
                .body(
                        ".",
                        Matchers.hasSize(2)
                );
    }

    @Test
    void testEmptyGetById() {
        final long id = 1L;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/posts/{id}", id)
                .then()
                .statusCode(
                        HttpStatus.NOT_FOUND.value()
                )
                .body(
                        "message",
                        Matchers.containsString("not found")
                );
    }

    @Test
    void testNotEmptyGetById() {
        final Post post = postRepository.save(new Post("First title", "First text"));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/posts/{id}", post.getPostId())
                .then()
                .statusCode(
                        HttpStatus.OK.value()
                )
                .body(
                        "postId",
                        Matchers.equalTo(post.getPostId().intValue())
                )
                .body(
                        "title",
                        Matchers.equalTo(post.getTitle())
                )
                .body(
                        "text",
                        Matchers.equalTo(post.getText())
                );
    }

    @Test
    void testViewsCountIncrement() {
        final Post post = postRepository.save(new Post("First title", "First text"));
        final int counter = 5;

        for (int i = 0; i < counter; i++) {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/api/v1/posts/{id}", post.getPostId())
                    .then()
                    .statusCode(
                            HttpStatus.OK.value()
                    )
                    .body(
                            "viewsCount",
                            Matchers.equalTo(i)
                    );
        }
    }

    @Test
    void testCreate() {
        final PostDto post = new PostDto("First title", "First text");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(post)
                .when()
                .post("/api/v1/posts")
                .then()
                .statusCode(
                        HttpStatus.CREATED.value()
                )
                .body(
                        "postId",
                        Matchers.notNullValue()
                )
                .body(
                        "viewsCount",
                        Matchers.equalTo(0)
                );
    }

    @Test
    void testIncorrectCreate() {
        final PostDto post = new PostDto("First title", null);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(post)
                .when()
                .post("/api/v1/posts")
                .then()
                .statusCode(
                        HttpStatus.BAD_REQUEST.value()
                )
                .body(
                        "message",
                        Matchers.containsString("not null")
                );
    }

    @Test
    void testDelete() {
        final long id = 1L;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/v1/posts/{id}", id)
                .then()
                .statusCode(
                        HttpStatus.NO_CONTENT.value()
                );
    }

}
