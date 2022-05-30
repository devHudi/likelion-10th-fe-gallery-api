package likelion.gallery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.sql.DataSource;
import likelion.gallery.dao.CommentDao;
import likelion.gallery.dto.request.CommentRequest;
import likelion.gallery.dto.response.CommentResponse;
import likelion.gallery.exception.CommentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class CommentServiceTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private CommentService commentService;

    @BeforeEach
    void setUp() {
        CommentDao commentDao = new CommentDao(jdbcTemplate, dataSource);
        commentService = new CommentService(commentDao);
    }

    @DisplayName("이미지를 생성한다.")
    @Test
    void createComment() {
        // given
        CommentRequest commentRequest = new CommentRequest("익명", "안녕하세요");

        // when
        Long id = commentService.createComment(commentRequest);

        // then
        assertThat(id).isNotNull();
    }

    @DisplayName("전체 이미지를 조회한다.")
    @Test
    void findAllComments() {
        // given
        CommentRequest commentRequest1 = new CommentRequest("익명1", "안녕하세요1");
        CommentRequest commentRequest2 = new CommentRequest("익명2", "안녕하세요2");

        commentService.createComment(commentRequest1);
        commentService.createComment(commentRequest2);

        // when
        List<CommentResponse> actual = commentService.findAllComments();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("단일 이미지를 Id로 조회한다.")
    @Test
    void findCommentById() {
        // given
        CommentRequest commentRequest = new CommentRequest("익명", "안녕하세요");
        Long id = commentService.createComment(commentRequest);

        // when
        CommentResponse actual = commentService.findCommentById(id);

        // then
        assertAll(
                () -> assertThat(actual.getAuthor()).isEqualTo("익명"),
                () -> assertThat(actual.getBody()).isEqualTo("안녕하세요")
        );
    }

    @DisplayName("이미지를 제거한다.")
    @Test
    void deleteComment() {
        // given
        CommentRequest commentRequest = new CommentRequest("익명", "안녕하세요");
        Long id = commentService.createComment(commentRequest);

        // when
        commentService.deleteComment(id);

        // then
        assertThatThrownBy(() -> commentService.findCommentById(id))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @DisplayName("존재하지 않는 이미지를 제거할때 예외가 발생한다.")
    @Test
    void deleteComment_throwsExceptionIfCommentIsNotExisting() {
        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(999L))
                .isInstanceOf(CommentNotFoundException.class);
    }
}
