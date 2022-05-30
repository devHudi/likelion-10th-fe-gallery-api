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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class CommentAcceptanceTest extends AcceptanceTest {
    @DisplayName("코멘트 생성")
    @Test
    void createComment() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("author", "익명");
        params.put("body", "안녕하세요");

        // when
        ExtractableResponse<Response> response = AcceptanceUtil.postRequest(params, "/comments");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location").split("/")[2]).isNotBlank()
        );
    }

    @DisplayName("모든 코멘트 조회")
    @Test
    void getAllComments() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("author", "익명");
        params.put("body", "안녕하세요");

        AcceptanceUtil.postRequest(params, "/comments");

        // when
        ExtractableResponse<Response> response = AcceptanceUtil.getRequest("/comments");
        int actualStatusCode = response.statusCode();
        List<CommentResponse> actualCommentResponses = new ArrayList<>(
                response.jsonPath().getList(".", CommentResponse.class));

        // then
        assertAll(
                () -> assertThat(actualStatusCode).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actualCommentResponses).hasSize(1)
        );
    }

    @DisplayName("단일 코멘트 조회")
    @Test
    void getComment() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("author", "익명");
        params.put("body", "안녕하세요");

        String id = AcceptanceUtil.postRequest(params, "/comments").header("Location").split("/")[2];

        // when
        ExtractableResponse<Response> actual = AcceptanceUtil.getRequest("/comments/" + id);

        // then
        assertAll(
                () -> assertThat(actual.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual.jsonPath().getString("author")).isEqualTo("익명"),
                () -> assertThat(actual.jsonPath().getString("body")).isEqualTo("안녕하세요")
        );
    }

    @DisplayName("코멘트 제거")
    @Test
    void deleteComment() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("author", "익명");
        params.put("body", "안녕하세요");

        String id = AcceptanceUtil.postRequest(params, "/comments").header("Location").split("/")[2];

        // when
        ExtractableResponse<Response> actual = AcceptanceUtil.deleteRequest("/comments/" + id);

        // then
        assertThat(actual.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
