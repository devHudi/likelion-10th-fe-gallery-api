package likelion.gallery.domain.image;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ImageUrlTest {
    @DisplayName("올바른 URL 형식으로 생성할 수 있다.")
    @ValueSource(strings = {"http://naver.com", "https://naver.com",
            "https://images.unsplash.com/photo-1642557581363-0ab2f77764f3?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1287&q=80"})
    @ParameterizedTest
    void constructor_withValidUrlFormat(String input) {
        // given & when
        ImageUrl actual = new ImageUrl(input);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("URL 형식이 아닌 문자열이 전달될 경우 예외가 발생한다.")
    @ValueSource(strings = {"", "naver.com", "http://naver",})
    @ParameterizedTest
    void constructor_throwsExceptionIfValueIsNotFormatOfUrl(String input) {
        // when & then
        assertThatThrownBy(() -> new ImageUrl(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바른 URL 형식이 아닙니다.");
    }
}
