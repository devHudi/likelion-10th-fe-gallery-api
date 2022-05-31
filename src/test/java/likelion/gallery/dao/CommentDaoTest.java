package likelion.gallery.dao;

import static likelion.gallery.Fixtures.IMAGE_URL_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import javax.sql.DataSource;
import likelion.gallery.domain.comment.Author;
import likelion.gallery.domain.comment.Body;
import likelion.gallery.domain.comment.Comment;
import likelion.gallery.domain.image.Description;
import likelion.gallery.domain.image.Image;
import likelion.gallery.domain.image.ImageUrl;
import likelion.gallery.domain.image.Title;
import likelion.gallery.dto.request.ImageRequest;
import likelion.gallery.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class CommentDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private CommentDao commentDao;
    private ImageDao imageDao;

    private Long imageId;

    @BeforeEach
    void setUp() {
        commentDao = new CommentDao(jdbcTemplate, dataSource);
        imageDao = new ImageDao(jdbcTemplate, dataSource);

        ImageService imageService = new ImageService(imageDao);
        imageId = imageService.createImage(new ImageRequest("이미지 제목", "이미지 설명", "http://google.com/"));
    }

    @DisplayName("댓글을 저장한다.")
    @Test
    void save() {
        // given
        Comment comment = new Comment(new Author("익명"), new Body("안녕하세요"));

        // when
        Long id = commentDao.save(imageId, comment);

        // then
        assertThat(id).isNotNull();
    }

    @DisplayName("이미지 아이디로 전체 댓글을 조회한다.")
    @Test
    void findAll() {
        // given
        Image image = new Image(new Title("제목"), new Description("설명"), new ImageUrl(IMAGE_URL_1));
        Long imageId = imageDao.save(image);

        Comment comment1 = new Comment(imageId, new Author("익명1"), new Body("안녕하세요1"));
        Comment comment2 = new Comment(imageId, new Author("익명2"), new Body("안녕하세요2"));

        commentDao.save(imageId, comment1);
        commentDao.save(imageId, comment2);

        // when
        List<Comment> actual = commentDao.findByImageId(imageId);

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("Id로 단일 코멘트를 조회한다.")
    @Test
    void findById() {
        // given
        Comment comment = new Comment(new Author("익명1"), new Body("안녕하세요1"));
        Long id = commentDao.save(imageId, comment);

        // when
        Comment actual = commentDao.findById(id);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("Id로 이미지를 제거한다.")
    @Test
    void delete() {
        // given
        Comment comment = new Comment(new Author("익명1"), new Body("안녕하세요1"));
        Long id = commentDao.save(imageId, comment);

        // when
        commentDao.delete(id);

        // then
        assertThatThrownBy(() -> commentDao.findById(id))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
