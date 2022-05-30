package likelion.gallery.service;

import static likelion.gallery.Fixtures.IMAGE_URL_1;
import static likelion.gallery.Fixtures.IMAGE_URL_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.sql.DataSource;
import likelion.gallery.dao.ImageDao;
import likelion.gallery.dto.request.ImageRequest;
import likelion.gallery.dto.response.ImageResponse;
import likelion.gallery.exception.ImageNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ImageServiceTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        ImageDao imageDao = new ImageDao(jdbcTemplate, dataSource);
        imageService = new ImageService(imageDao);
    }

    @DisplayName("이미지를 생성한다.")
    @Test
    void createImage() {
        // given
        ImageRequest imageRequest = new ImageRequest("이미지1", "이미지 설명", IMAGE_URL_1);

        // when
        Long id = imageService.createImage(imageRequest);

        // then
        assertThat(id).isNotNull();
    }

    @DisplayName("전체 이미지를 조회한다.")
    @Test
    void findAllImages() {
        // given
        ImageRequest imageRequest1 = new ImageRequest("이미지1", "이미지 설명", IMAGE_URL_1);
        ImageRequest imageRequest2 = new ImageRequest("이미지2", "이미지 설명", IMAGE_URL_2);

        imageService.createImage(imageRequest1);
        imageService.createImage(imageRequest2);

        // when
        List<ImageResponse> actual = imageService.findAllImages();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("단일 이미지를 Id로 조회한다.")
    @Test
    void findImageById() {
        // given
        ImageRequest imageRequest = new ImageRequest("이미지", "이미지 설명", IMAGE_URL_1);
        Long id = imageService.createImage(imageRequest);

        // when
        ImageResponse actual = imageService.findImageById(id);

        // then
        assertAll(
                () -> assertThat(actual.getTitle()).isEqualTo("이미지"),
                () -> assertThat(actual.getDescription()).isEqualTo("이미지 설명")
        );
    }

    @DisplayName("이미지를 제거한다.")
    @Test
    void deleteImage() {
        // given
        ImageRequest imageRequest = new ImageRequest("이미지", "이미지 설명", IMAGE_URL_1);
        Long id = imageService.createImage(imageRequest);

        // when
        imageService.deleteImage(id);

        // then
        assertThatThrownBy(() -> imageService.findImageById(id))
                .isInstanceOf(ImageNotFoundException.class);
    }

    @DisplayName("존재하지 않는 이미지를 제거할때 예외가 발생한다.")
    @Test
    void deleteImage_throwsExceptionIfImageIsNotExisting() {
        // when & then
        assertThatThrownBy(() -> imageService.deleteImage(999L))
                .isInstanceOf(ImageNotFoundException.class);
    }
}
