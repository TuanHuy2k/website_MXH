package com.hust.webchat.service.impl;

import com.hust.webchat.domain.Comment;
import com.hust.webchat.domain.Posting;
import com.hust.webchat.domain.User;
import com.hust.webchat.dto.CommentDTO;
import com.hust.webchat.dto.request.CommentRequest;
import com.hust.webchat.mapper.CommentMapper;
import com.hust.webchat.repository.CommentRepository;
import com.hust.webchat.repository.PostingRepository;
import com.hust.webchat.service.CommentService;
import com.hust.webchat.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final PostingRepository postingRepository;

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final UserService userService;

    public CommentServiceImpl(PostingRepository postingRepository, CommentRepository commentRepository, CommentMapper commentMapper, UserService userService) {
        this.postingRepository = postingRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userService = userService;
    }

    @Override
    public CommentDTO writeComment(CommentRequest request) {
        User currentUser = userService.getCurrentUser();
        Optional<Posting> posting = postingRepository.findById(request.getPostingId());
        if (posting.isPresent()) {
            Comment comment = new Comment();
            comment.setContent(request.getContent());
            comment.setUserId(currentUser.getId());
            comment.setPosting(posting.get());
            Comment commentSaved = commentRepository.save(comment);
            return commentMapper.toDto(commentSaved);
        }
        return null;
    }
}
