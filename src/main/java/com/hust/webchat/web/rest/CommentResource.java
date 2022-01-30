package com.hust.webchat.web.rest;

import com.hust.webchat.dto.CommentDTO;
import com.hust.webchat.dto.request.CommentRequest;
import com.hust.webchat.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api")
public class CommentResource {

    private final CommentService commentService;

    public CommentResource(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<CommentDTO> createPosting(@RequestBody @Valid CommentRequest request) {
        return ResponseEntity.ok(commentService.writeComment(request));
    }
}
