package likelion.gallery.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import likelion.gallery.domain.image.Description;
import likelion.gallery.domain.image.Image;
import likelion.gallery.domain.image.ImageUrl;
import likelion.gallery.domain.image.Title;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDao {
    private static final String TABLE_NAME = "IMAGE";
    private static final String KEY_COLUMN = "id";

    private static final RowMapper<Image> IMAGE_ROW_MAPPER = (rs, rowNum) -> new Image(
            new Title(rs.getString("title")),
            new Description(rs.getString("description")),
            new ImageUrl(rs.getString("image_url")),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getLong("id")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ImageDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN);
    }

    public Long save(Image image) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", image.getTitle().getValue());
        params.put("description", image.getDescription().getValue());
        params.put("image_url", image.getImageUrl().getValue());
        params.put("created_at", image.getCreatedAt());

        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<Image> findAll() {
        String sql = "SELECT id, title, description, image_url, created_at FROM IMAGE";
        return jdbcTemplate.query(sql, IMAGE_ROW_MAPPER);
    }

    public Image findById(Long id) {
        String sql = "SELECT id, title, description, image_url, created_at FROM IMAGE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, IMAGE_ROW_MAPPER, id);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM IMAGE WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
