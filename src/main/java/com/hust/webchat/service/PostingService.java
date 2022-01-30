package com.hust.webchat.service;

import com.hust.webchat.dto.PostingDTO;
import com.hust.webchat.dto.request.PostingRequest;

import java.util.List;

public interface PostingService {
    /**
     * tao bai viet
     *
     * @param request
     * @return
     */
    PostingDTO createPosting(PostingRequest request);

    /**
     * lay danh sach tat ca bai dang cua nhung nguoi ban cua minh
     *
     * @return
     */
    List<PostingDTO> getAllPostingOfFriends();

    /**
     * lay danh sach nhung bai dang cua 1 nguoi ban
     *
     * @param userId
     * @return
     */
    List<PostingDTO> getAllPosingOfFriend(Long userId);

    /**
     * lay danh sach tat ca bai dang cua user hien tai
     *
     * @return
     */
    List<PostingDTO> getAllPostingOfCurrentUser();

    /**
     * thich hoac khong thich bai viet
     *
     * @param postingId
     */
    void likePosting(Long postingId);
}
