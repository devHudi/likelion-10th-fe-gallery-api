package likelion.gallery.controller;

import java.net.URI;
import java.util.List;
import likelion.gallery.dto.request.ImageRequest;
import likelion.gallery.dto.response.ImageResponse;
import likelion.gallery.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<Void> createImage(@RequestBody ImageRequest imageRequest) {
        Long id = imageService.createImage(imageRequest);
        return ResponseEntity.created(URI.create("/images/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<ImageResponse>> getAllImages() {
        List<ImageResponse> allImages = imageService.findAllImages();
        return ResponseEntity.ok(allImages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageResponse> getImage(@PathVariable Long id) {
        ImageResponse image = imageService.findImageById(id);
        return ResponseEntity.ok(image);
    }
}
