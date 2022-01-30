package com.hust.webchat.repository;

import com.hust.webchat.domain.FriendRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRelationRepository extends JpaRepository<FriendRelation, Long> {

    @Query("from FriendRelation fr where (fr.fromUser = :userId and fr.toUser = :userIdRelation) or " +
            "(fr.fromUser = :userIdRelation and fr.toUser = :userId )")
    Optional<FriendRelation> findByUserRelation(Long userId, Long userIdRelation);

    @Query("from FriendRelation fr where (fr.fromUser = :userId or fr.toUser = :userId) and fr.isFriend = true")
    List<FriendRelation> findAllByUser(Long userId);

    @Query("from FriendRelation fr where fr.toUser = :userId and fr.isFriend = false ")
    List<FriendRelation> getAllApproveFriends(Long userId);

    Optional<FriendRelation> findByFromUserAndToUser(Long fromUser, Long toUser);

    void deleteByFromUserAndToUser(Long fromUser, Long toUser);
}
