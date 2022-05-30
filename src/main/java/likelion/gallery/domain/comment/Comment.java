package likelion.gallery.domain.comment;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString(includeFieldNames = true)
@EqualsAndHashCode
@RequiredArgsConstructor
public class Comment {
    private final Long id;
    private final Author author;
    private final Body body;
    private final LocalDateTime createdAt;
}
