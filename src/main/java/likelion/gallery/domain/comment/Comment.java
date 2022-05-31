package likelion.gallery.domain.comment;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Comment {
    private final Long id;
    private final Long imageId;
    private final Author author;
    private final Body body;
    private final LocalDateTime createdAt;

    public Comment(Long id, Long imageId, Author author, Body body, LocalDateTime createdAt) {
        this.id = id;
        this.imageId = imageId;
        this.author = author;
        this.body = body;
        this.createdAt = createdAt;
    }

    public Comment(Long id, Author author, Body body, LocalDateTime createdAt) {
        this(id, null, author, body, createdAt);
    }

    public Comment(Long id, Author author, Body body) {
        this(id, author, body, LocalDateTime.now());
    }

    public Comment(Author author, Body body) {
        this(null, author, body, LocalDateTime.now());
    }
}
