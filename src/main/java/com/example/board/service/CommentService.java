package com.example.board.service;

import com.example.board.entity.Comment;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.board.entity.CommentLike;
import com.example.board.repository.CommentLikeRepository;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;

    private final PostRepository postRepository; // Post 조회용

    public List<Comment> getComments(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }
    private final NotificationService notificationService;

    public Comment createComment(Comment comment) {
        Comment saved = commentRepository.save(comment);

        // 게시글 작성자 조회
        postRepository.findById(comment.getPostId()).ifPresent(post -> {
            // 본인 글에 본인 댓글 제외
            if (!post.getUserId().equals(comment.getUserId())) {
                String message = comment.getAuthor() + "님이 댓글을 달았습니다.";
                notificationService.createNotification(
                        post.getUserId(),      // 알림 받을 사람 (글 작성자)
                        comment.getUserId(),   // 알림 발생시킨 사람
                        comment.getAuthor(),   // 닉네임
                        post.getId(),          // 게시글 id
                        post.getTitle(),       // 게시글 제목
                        saved.getId(),         // 댓글 id
                        message
                );
            }
        });

        return saved;
    }

    public Comment updateComment(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        // 자식 댓글들 먼저 재귀 삭제
        List<Comment> children = commentRepository.findByParentId(id);
        for (Comment child : children) {
            deleteComment(child.getId()); // 재귀
        }
        commentRepository.deleteById(id);
    }

    public Comment likeComment(Long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        Optional<CommentLike> existing = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);
        if (existing.isPresent()) {
            // 이미 좋아요 했으면 취소
            commentLikeRepository.delete(existing.get());
            comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
        } else {
            // 좋아요 추가
            CommentLike like = new CommentLike();
            like.setCommentId(commentId);
            like.setUserId(userId);
            commentLikeRepository.save(like);
            comment.setLikeCount(comment.getLikeCount() + 1);
        }
        return commentRepository.save(comment);
    }
}