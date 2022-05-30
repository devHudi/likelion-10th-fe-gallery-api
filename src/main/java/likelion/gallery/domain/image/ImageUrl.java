package likelion.gallery.domain.image;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class ImageUrl {
    private static final String URL_REGEX = "^(http(s)?:\\/\\/)[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.([a-zA-Z])+.*";

    private final String value;

    public ImageUrl(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (!value.matches(URL_REGEX)) {
            throw new IllegalArgumentException("올바른 URL 형식이 아닙니다.");
        }
    }
}
