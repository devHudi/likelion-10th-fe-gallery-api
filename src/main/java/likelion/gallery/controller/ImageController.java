package likelion.gallery.controller;

import io.swagger.annotations.Api;
import java.net.URI;
import java.util.List;
import likelion.gallery.dto.request.CommentRequest;
import likelion.gallery.dto.request.ImageRequest;
import likelion.gallery.dto.response.CommentResponse;
import likelion.gallery.dto.response.ImageResponse;
import likelion.gallery.service.CommentService;
import likelion.gallery.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "이미지", tags = "이미지와 이미지의 댓글을 관리합니다.")
@Controller
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;
    private final CommentService commentService;

    public ImageController(ImageService imageService, CommentService commentService) {
        this.imageService = imageService;
        this.commentService = commentService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ImageResponse> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> createComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        Long commentId = commentService.createComment(id, commentRequest);
        return ResponseEntity.created(URI.create("/" + id + "/comments/" + commentId)).build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long id) {
        List<CommentResponse> comments = commentService.findCommentsByImageId(id);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Long id, @PathVariable Long commentId) {
        commentService.deleteComment(id, commentId);
        return ResponseEntity.ok().build();
    }
}
