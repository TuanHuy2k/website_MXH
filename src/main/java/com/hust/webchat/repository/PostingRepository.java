package com.hust.webchat.repository;

import com.hust.webchat.domain.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostingRepository extends JpaRepository<Posting, Long> {

    @Query("from Posting p where p.userId in (:ids) and p.status = 1")
    List<Posting> getPostingByIds(List<Long> ids);
}
