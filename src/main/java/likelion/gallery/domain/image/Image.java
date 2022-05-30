package likelion.gallery.domain.image;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Image {
    private final Title title;
    private final Description description;
    private final ImageUrl imageUrl;
    private final LocalDateTime createdAt;
    private final Long id;

    public Image(Title title, Description description, ImageUrl imageUrl, LocalDateTime createdAt, Long id) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.id = id;
    }

    public Image(Title title, Description description, ImageUrl imageUrl, Long id) {
        this(title, description, imageUrl, LocalDateTime.now(), id);
    }

    public Image(Title title, Description description, ImageUrl imageUrl) {
        this(title, description, imageUrl, LocalDateTime.now(), null);
    }
}
