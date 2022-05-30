package likelion.gallery.domain.image;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString(includeFieldNames = true)
@EqualsAndHashCode
@RequiredArgsConstructor
public class Title {
    private final String value;
}
