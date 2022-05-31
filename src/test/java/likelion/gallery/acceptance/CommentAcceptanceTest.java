package likelion.gallery.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import likelion.gallery.dto.response.CommentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class CommentAcceptanceTest extends AcceptanceTest {
    private String createdImageId;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        Map<String, String> params = new HashMap<>();
        params.put("title", "이미지1");
        params.put("description", "이미지1 입니다");
        params.put("imageUrl",
                "https://images.unsplash.com/photo-1649798511342-b468e770c222?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80");

        ExtractableResponse<Response> response = AcceptanceUtil.postRequest(params, "/images");
        createdImageId = response.header("Location").split("/")[2];
    }

    @DisplayName("코멘트 생성")
    @Test
    void createComment() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("author", "익명");
        params.put("body", "안녕하세요");

        // when
        ExtractableResponse<Response> response = AcceptanceUtil.postRequest(params,
                "/images/" + createdImageId + "/comments");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location").split("/")[3]).isNotBlank()
        );
    }

    @DisplayName("모든 코멘트 조회")
    @Test
    void getAllComments() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("author", "익명");
        params.put("body", "안녕하세요");

        AcceptanceUtil.postRequest(params, "/images/" + createdImageId + "/comments");

        // when
        ExtractableResponse<Response> response = AcceptanceUtil.getRequest("/images/" + createdImageId + "/comments");
        int actualStatusCode = response.statusCode();
        List<CommentResponse> actualCommentResponses = new ArrayList<>(
                response.jsonPath().getList(".", CommentResponse.class));

        // then
        assertAll(
                () -> assertThat(actualStatusCode).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actualCommentResponses).hasSize(1)
        );
    }

    @DisplayName("코멘트 제거")
    @Test
    void deleteComment() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("author", "익명");
        params.put("body", "안녕하세요");

        String id = AcceptanceUtil.postRequest(params, "/images/" + createdImageId + "/comments")
                .header("Location").split("/")[3];

        // when
        ExtractableResponse<Response> actual = AcceptanceUtil.deleteRequest(
                "/images/" + createdImageId + "/comments/" + id);

        // then
        assertThat(actual.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
