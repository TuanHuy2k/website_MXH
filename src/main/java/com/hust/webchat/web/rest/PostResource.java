package com.hust.webchat.web.rest;

import com.hust.webchat.dto.PostingDTO;
import com.hust.webchat.dto.request.PostingRequest;
import com.hust.webchat.service.PostingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class PostResource {

    private final PostingService postingService;

    public PostResource(PostingService postingService) {
        this.postingService = postingService;
    }

    @PostMapping(value = "/posting")
    public ResponseEntity<PostingDTO> createPosting(@RequestBody @Valid PostingRequest request) {
        return ResponseEntity.ok(postingService.createPosting(request));
    }

    @PostMapping(value = "/posting/friends")
    public ResponseEntity<List<PostingDTO>> getAllPostingOfFriends() {
        return ResponseEntity.ok(postingService.getAllPostingOfFriends());
    }

    @PostMapping(value = "/posting/like-posting/{postingId}")
    public ResponseEntity<Boolean> likePosting(@PathVariable Long postingId) {
        postingService.likePosting(postingId);
        return ResponseEntity.ok(Boolean.TRUE);
    }

}
