package likelion.gallery.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import likelion.gallery.domain.comment.Author;
import likelion.gallery.domain.comment.Body;
import likelion.gallery.domain.comment.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDao {
    private static final String TABLE_NAME = "COMMENT";
    private static final String KEY_COLUMN = "id";

    private static final RowMapper<Comment> COMMENT_ROW_MAPPER = (rs, rowNum) -> new Comment(
            rs.getLong("id"),
            new Author(rs.getString("author")),
            new Body(rs.getString("body")),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public CommentDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN);
    }

    public Long save(Long imageId, Comment comment) {
        Map<String, Object> params = new HashMap<>();
        params.put("image_id", imageId);
        params.put("author", comment.getAuthor().getValue());
        params.put("body", comment.getBody().getValue());
        params.put("created_at", comment.getCreatedAt());

        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Comment findById(Long id) {
        String sql = "SELECT id, author, body, created_at FROM COMMENT WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, COMMENT_ROW_MAPPER, id);
    }

    public List<Comment> findByImageId(Long imageId) {
        String sql = "SELECT id, author, body, created_at FROM COMMENT WHERE image_id = ?";
        return jdbcTemplate.query(sql, COMMENT_ROW_MAPPER, imageId);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM COMMENT WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
