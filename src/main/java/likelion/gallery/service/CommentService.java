package likelion.gallery.service;

import java.util.List;
import java.util.stream.Collectors;
import likelion.gallery.dao.CommentDao;
import likelion.gallery.domain.comment.Author;
import likelion.gallery.domain.comment.Body;
import likelion.gallery.domain.comment.Comment;
import likelion.gallery.dto.request.CommentRequest;
import likelion.gallery.dto.response.CommentResponse;
import likelion.gallery.exception.CommentNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentDao commentDao;

    public CommentService(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public Long createComment(CommentRequest commentRequest) {
        Comment comment = convertRequestToComment(commentRequest);
        return commentDao.save(comment);
    }

    public List<CommentResponse> findAllComments() {
        List<Comment> comments = commentDao.findAll();
        return comments.stream()
                .map(this::convertCommentToResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse findCommentById(Long id) {
        try {
            return convertCommentToResponse(commentDao.findById(id));
        } catch (EmptyResultDataAccessException e) {
            throw new CommentNotFoundException();
        }
    }

    public void deleteComment(Long id) {
        findCommentById(id);
        commentDao.delete(id);
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
