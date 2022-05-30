package likelion.gallery.domain.comment;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Comment {
    private final Long id;
    private final Author author;
    private final Body body;
    private final LocalDateTime createdAt;
}
