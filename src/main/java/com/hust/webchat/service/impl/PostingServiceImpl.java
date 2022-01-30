package com.hust.webchat.service.impl;

import com.hust.webchat.domain.Emotion;
import com.hust.webchat.domain.Posting;
import com.hust.webchat.domain.User;
import com.hust.webchat.dto.CommentDTO;
import com.hust.webchat.dto.PostingDTO;
import com.hust.webchat.dto.UserDTO;
import com.hust.webchat.dto.request.PostingRequest;
import com.hust.webchat.mapper.PostingMapper;
import com.hust.webchat.repository.EmotionRepository;
import com.hust.webchat.repository.PostingRepository;
import com.hust.webchat.service.PostingService;
import com.hust.webchat.service.UserService;
import com.hust.webchat.util.Constants;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostingServiceImpl implements PostingService {

    private final String URL_IMG = "http://localhost:8081/api/public/files/";

    private final UserService userService;

    private final PostingRepository postingRepository;

    private final EmotionRepository emotionRepository;

    private final PostingMapper postingMapper;

    public PostingServiceImpl(UserService userService, PostingRepository postingRepository, EmotionRepository emotionRepository, PostingMapper postingMapper) {
        this.userService = userService;
        this.postingRepository = postingRepository;
        this.emotionRepository = emotionRepository;
        this.postingMapper = postingMapper;
    }

    @Override
    public PostingDTO createPosting(PostingRequest request) {
        // create url file posting
        User user = userService.getCurrentUser();
        String postingUrlImg = URL_IMG + request.getFileId().toString() + "/view";
        PostingDTO postingDTO = new PostingDTO();
        postingDTO.setContent(request.getContent());
        postingDTO.setUserId(user.getId());
        postingDTO.setStatus(Constants.ENTITY_STATUS.ACTIVE);
        postingDTO.setFileUrl(postingUrlImg);

        postingRepository.save(postingMapper.toEntity(postingDTO));
        return postingDTO;
    }

    @Override
    public List<PostingDTO> getAllPostingOfFriends() {
        User user = userService.getCurrentUser();
        List<UserDTO> friends = userService.getFriendsOfCurrentUser();
        List<Long> friendIds = friends.stream().map(UserDTO::getId).collect(Collectors.toList());
        friendIds.add(user.getId());
        List<Posting> postings = postingRepository.getPostingByIds(friendIds);
        List<PostingDTO> postingDTOS = postingMapper.toDto(postings);

        List<UserDTO> userDTOS = userService.getAllUser();

        // list like posting
        List<Emotion> emotions = emotionRepository.findAll();

        // enrich thong tin user vao bai viet
        postingDTOS.forEach(postingDTO -> {
            UserDTO userOfPosting = userDTOS.stream()
                    .filter(userDTO -> Objects.equals(userDTO.getId(), postingDTO.getUserId()))
                    .findFirst().get();
            postingDTO.setUserDTO(userOfPosting);
            postingDTO.setNumberComment(postingDTO.getComments().size());
            List<CommentDTO> commentDTOS = postingDTO.getComments();

            // enrich thong tin user vao comment
            commentDTOS.stream().forEach(commentDTO -> {
                UserDTO userComment = userDTOS.stream()
                        .filter(userDTO -> Objects.equals(userDTO.getId(), commentDTO.getUserId()))
                        .findFirst().get();
                commentDTO.setUserComment(userComment);
            });

            // enrich so like cua tung bai viet
            Long totalEmotionOfPosting = emotions.stream().filter(emotion -> Objects.equals(emotion.getPostingId(), postingDTO.getId())).count();
            Optional<Emotion> emotionCurrentUser = emotions.stream()
                    .filter(emotion -> (Objects.equals(emotion.getUserId(), user.getId())
                            && Objects.equals(emotion.getPostingId(), postingDTO.getId())))
                    .findFirst();
            if (emotionCurrentUser.isPresent()) {
                postingDTO.setLiked(Boolean.TRUE);
            } else {
                postingDTO.setLiked(Boolean.FALSE);
            }

            postingDTO.setNumberLike(totalEmotionOfPosting);
        });
        return postingDTOS.stream().sorted(Comparator.comparing(PostingDTO::getCreatedDate).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<PostingDTO> getAllPosingOfFriend(Long userId) {
        List<Posting> postings = postingRepository.getPostingByIds(new ArrayList<>(Collections.singleton(userId)));
        return postingMapper.toDto(postings);
    }

    @Override
    public List<PostingDTO> getAllPostingOfCurrentUser() {
        User user = userService.getCurrentUser();
        List<Posting> postings = postingRepository.getPostingByIds(new ArrayList<>(Collections.singleton(user.getId())));
        return postingMapper.toDto(postings);
    }

    @Override
    public void likePosting(Long postingId) {
        User currentUser = userService.getCurrentUser();
        Optional<Emotion> optionalEmotion = emotionRepository.findByUserIdAndPostingId(currentUser.getId(), postingId);
        if (optionalEmotion.isPresent()) {
            emotionRepository.delete(optionalEmotion.get());
        } else {
            Emotion emotion = new Emotion();
            emotion.setUserId(currentUser.getId());
            emotion.setPostingId(postingId);
            emotion.setType(Constants.EMOTION_TYPE.LIKE);
            emotionRepository.save(emotion);
        }
    }
}
