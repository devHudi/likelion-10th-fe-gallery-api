package likelion.gallery.service;

import java.util.List;
import java.util.stream.Collectors;
import likelion.gallery.dao.CommentDao;
import likelion.gallery.dao.ImageDao;
import likelion.gallery.domain.comment.Author;
import likelion.gallery.domain.comment.Body;
import likelion.gallery.domain.comment.Comment;
import likelion.gallery.dto.request.CommentRequest;
import likelion.gallery.dto.response.CommentResponse;
import likelion.gallery.exception.CommentNotFoundException;
import likelion.gallery.exception.ImageNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentDao commentDao;
    private final ImageDao imageDao;

    public CommentService(CommentDao commentDao, ImageDao imageDao) {
        this.commentDao = commentDao;
        this.imageDao = imageDao;
    }

    public Long createComment(Long imageId, CommentRequest commentRequest) {
        Comment comment = convertRequestToComment(commentRequest);
        return commentDao.save(imageId, comment);
    }

    public List<CommentResponse> findCommentsByImageId(Long imageId) {
        try {
            imageDao.findById(imageId);
        } catch (EmptyResultDataAccessException e) {
            throw new ImageNotFoundException();
        }

        return commentDao.findByImageId(imageId)
                .stream()
                .map(this::convertCommentToResponse)
                .collect(Collectors.toList());
    }

    public void deleteComment(Long imageId, Long commentId) {
        validateCommentExists(commentId);
        validateCorrectImageId(imageId, commentId);

        commentDao.delete(commentId);
    }

    private void validateCommentExists(Long commentId) {
        try {
            commentDao.findById(commentId);
        } catch (EmptyResultDataAccessException e) {
            throw new CommentNotFoundException();
        }
    }

    private void validateCorrectImageId(Long imageId, Long commentId) {
        Comment comment = commentDao.findById(commentId);
        if (imageId.equals(comment.getImageId())) {
            throw new CommentNotFoundException();
        }
    }

    private Comment convertRequestToComment(CommentRequest commentRequest) {
        Author author = new Author(commentRequest.getAuthor());
        Body body = new Body(commentRequest.getBody());
        return new Comment(author, body);
    }

    private CommentResponse convertCommentToResponse(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getAuthor().getValue(), comment.getBody().getValue(),
                comment.getCreatedAt());
    }
}
