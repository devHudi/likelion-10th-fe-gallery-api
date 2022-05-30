package likelion.gallery.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import javax.sql.DataSource;
import likelion.gallery.domain.comment.Author;
import likelion.gallery.domain.comment.Body;
import likelion.gallery.domain.comment.Comment;
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

    @BeforeEach
    void setUp() {
        commentDao = new CommentDao(jdbcTemplate, dataSource);
    }

    @DisplayName("댓글을 저장한다.")
    @Test
    void save() {
        // given
        Comment comment = new Comment(new Author("익명"), new Body("안녕하세요"));

        // when
        Long id = commentDao.save(comment);

        // then
        assertThat(id).isNotNull();
    }

    @DisplayName("전체 이미지를 조회한다.")
    @Test
    void findAll() {
        // given
        Comment comment1 = new Comment(new Author("익명1"), new Body("안녕하세요1"));
        Comment comment2 = new Comment(new Author("익명2"), new Body("안녕하세요2"));

        commentDao.save(comment1);
        commentDao.save(comment2);

        // when
        List<Comment> actual = commentDao.findAll();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("Id로 단일 이미지를 조회한다.")
    @Test
    void findById() {
        // given
        Comment comment = new Comment(new Author("익명1"), new Body("안녕하세요1"));
        Long id = commentDao.save(comment);

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
        Long id = commentDao.save(comment);

        // when
        commentDao.delete(id);

        // then
        assertThatThrownBy(() -> commentDao.findById(id))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
