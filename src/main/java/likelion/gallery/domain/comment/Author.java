package likelion.gallery.domain.comment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString(includeFieldNames = true)
@EqualsAndHashCode
@RequiredArgsConstructor
public class Author {
    private final String value;
}
