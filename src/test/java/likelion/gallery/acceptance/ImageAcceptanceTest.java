package likelion.gallery.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import likelion.gallery.dto.response.ImageResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ImageAcceptanceTest extends AcceptanceTest {
    @DisplayName("모든 이미지 조회")
    @Test
    void getAllImages() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("title", "이미지1");
        params.put("description", "이미지1 입니다");
        params.put("imageUrl",
                "https://images.unsplash.com/photo-1649798511342-b468e770c222?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80");
        AcceptanceUtil.postRequest(params, "/images");

        // when
        ExtractableResponse<Response> response = AcceptanceUtil.getRequest("/images");
        int actualStatusCode = response.statusCode();
        List<ImageResponse> actualImageResponses = new ArrayList<>(
                response.jsonPath().getList(".", ImageResponse.class));

        // then
        Assertions.assertAll(
                () -> assertThat(actualStatusCode).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actualImageResponses).hasSize(1)
        );
    }
}
