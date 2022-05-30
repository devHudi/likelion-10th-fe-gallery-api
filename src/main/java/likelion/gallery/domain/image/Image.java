package likelion.gallery.domain.image;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Image {
    private final Long id;
    private final Title title;
    private final Description description;
    private final ImageUrl imageUrl;
    private final LocalDateTime createdAt;
}
