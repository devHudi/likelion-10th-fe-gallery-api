package likelion.gallery.domain.image;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString(includeFieldNames = true)
@EqualsAndHashCode
@RequiredArgsConstructor
public class Image {
    private final Long id;
    private final Title title;
    private final Description description;
    private final ImageUrl imageUrl;
    private final LocalDateTime createdAt;
}
