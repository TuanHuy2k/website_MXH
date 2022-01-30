package com.hust.webchat.service;

import com.hust.webchat.dto.CommentDTO;
import com.hust.webchat.dto.request.CommentRequest;

public interface CommentService {
    /**
     * comment
     *
     * @param request
     * @return
     */
    CommentDTO writeComment(CommentRequest request);
}
