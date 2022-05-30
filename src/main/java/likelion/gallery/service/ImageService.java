package likelion.gallery.service;

import java.util.List;
import java.util.stream.Collectors;
import likelion.gallery.dao.ImageDao;
import likelion.gallery.domain.image.Description;
import likelion.gallery.domain.image.Image;
import likelion.gallery.domain.image.ImageUrl;
import likelion.gallery.domain.image.Title;
import likelion.gallery.dto.request.ImageRequest;
import likelion.gallery.dto.response.ImageResponse;
import likelion.gallery.exception.ImageNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    private final ImageDao imageDao;

    public ImageService(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    public Long createImage(ImageRequest imageRequest) {
        Image image = convertRequestToImage(imageRequest);
        return imageDao.save(image);
    }

    public List<ImageResponse> findAllImages() {
        List<Image> images = imageDao.findAll();
        return images.stream()
                .map(this::convertImageToResponse)
                .collect(Collectors.toList());
    }

    public ImageResponse findImageById(Long id) {
        try {
            return convertImageToResponse(imageDao.findById(id));
        } catch (EmptyResultDataAccessException e) {
            throw new ImageNotFoundException();
        }
    }

    public void deleteImage(Long id) {
        findImageById(id);
        imageDao.delete(id);
    }

    private Image convertRequestToImage(ImageRequest imageRequest) {
        Title title = new Title(imageRequest.getTitle());
        Description description = new Description(imageRequest.getDescription());
        ImageUrl imageUrl = new ImageUrl(imageRequest.getImageUrl());
        return new Image(title, description, imageUrl);
    }

    private ImageResponse convertImageToResponse(Image image) {
        return new ImageResponse(image.getId(), image.getTitle().getValue(), image.getDescription().getValue(),
                image.getCreatedAt());
    }
}
