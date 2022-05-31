package likelion.gallery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import javax.sql.DataSource;
import likelion.gallery.dao.CommentDao;
import likelion.gallery.dao.ImageDao;
import likelion.gallery.dto.request.CommentRequest;
import likelion.gallery.dto.request.ImageRequest;
import likelion.gallery.dto.response.CommentResponse;
import likelion.gallery.exception.CommentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class CommentServiceTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private CommentService commentService;

    private CommentDao commentDao;

    private Long imageId;

    @BeforeEach
    void setUp() {
        commentDao = new CommentDao(jdbcTemplate, dataSource);
        ImageDao imageDao = new ImageDao(jdbcTemplate, dataSource);
        commentService = new CommentService(commentDao, imageDao);

        ImageService imageService = new ImageService(imageDao);
        imageId = imageService.createImage(new ImageRequest("이미지 제목", "이미지 설명", "http://google.com/"));
    }

    @DisplayName("댓글을 생성한다.")
    @Test
    void createComment() {
        // given
        CommentRequest commentRequest = new CommentRequest("익명", "안녕하세요");

        // when
        Long id = commentService.createComment(imageId, commentRequest);

        // then
        assertThat(id).isNotNull();
    }

    @DisplayName("특정 이미지의 전체 댓글을 조회한다.")
    @Test
    void findAllComments() {
        // given
        CommentRequest commentRequest1 = new CommentRequest("익명1", "안녕하세요1");
        CommentRequest commentRequest2 = new CommentRequest("익명2", "안녕하세요2");

        commentService.createComment(imageId, commentRequest1);
        commentService.createComment(imageId, commentRequest2);

        // when
        List<CommentResponse> actual = commentService.findCommentsByImageId(imageId);

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("댓글을 제거한다.")
    @Test
    void deleteComment() {
        // given
        CommentRequest commentRequest = new CommentRequest("익명", "안녕하세요");
        Long id = commentService.createComment(imageId, commentRequest);

        // when
        commentService.deleteComment(imageId, id);

        // then
        assertThatThrownBy(() -> commentDao.findById(id))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("존재하지 않는 댓글을 제거할때 예외가 발생한다.")
    @Test
    void deleteComment_throwsExceptionIfCommentIsNotExisting() {
        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(999L, 999L))
                .isInstanceOf(CommentNotFoundException.class);
    }
}
