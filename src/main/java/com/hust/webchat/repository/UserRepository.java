package com.hust.webchat.repository;

import com.hust.webchat.domain.User;
import com.hust.webchat.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByUsernameAndStatus(String login, Integer status);

    Optional<User> findByEmail(String email);

    Optional<User> findByActivationKey(String key);

    @Query("from User u where u.id not in (:userIds)")
    List<User> getUserByIdsNotIn(List<Long> userIds);

    @Query("from User u where u.id in (:userIds)")
    List<User> getUserByIds(List<Long> userIds);

    @Query("from User u where lower(u.username) like lower(:keyword) and u.status = 1")
    List<User> searchUser(@Param("keyword") String keyword);
}
