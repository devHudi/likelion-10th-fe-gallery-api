package likelion.gallery.dao;

import static likelion.gallery.Fixtures.IMAGE_URL_1;
import static likelion.gallery.Fixtures.IMAGE_URL_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import javax.sql.DataSource;
import likelion.gallery.domain.image.Description;
import likelion.gallery.domain.image.Image;
import likelion.gallery.domain.image.ImageUrl;
import likelion.gallery.domain.image.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ImageDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private ImageDao imageDao;

    @BeforeEach
    void setUp() {
        imageDao = new ImageDao(jdbcTemplate, dataSource);
    }

    @DisplayName("이미지를 저장한다.")
    @Test
    void save() {
        // given
        Image image = new Image(new Title("제목"), new Description("설명"), new ImageUrl(IMAGE_URL_1));

        // when
        Long id = imageDao.save(image);

        // then
        assertThat(id).isNotNull();
    }

    @DisplayName("전체 이미지를 조회한다.")
    @Test
    void findAll() {
        // given
        Image image1 = new Image(new Title("제목1"), new Description("설명"), new ImageUrl(IMAGE_URL_1));
        Image image2 = new Image(new Title("제목2"), new Description("설명"), new ImageUrl(IMAGE_URL_2));

        imageDao.save(image1);
        imageDao.save(image2);

        // when
        List<Image> actual = imageDao.findAll();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("Id로 단일 이미지를 조회한다.")
    @Test
    void findById() {
        // given
        Image image = new Image(new Title("제목1"), new Description("설명"), new ImageUrl(IMAGE_URL_1));
        Long id = imageDao.save(image);

        // when
        Image actual = imageDao.findById(id);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("Id로 이미지를 제거한다.")
    @Test
    void delete() {
        // given
        Image image = new Image(new Title("제목1"), new Description("설명"), new ImageUrl(IMAGE_URL_1));
        Long id = imageDao.save(image);

        // when
        imageDao.delete(id);

        // then
        assertThatThrownBy(() -> imageDao.findById(id))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
