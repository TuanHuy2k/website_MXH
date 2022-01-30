package com.hust.webchat.repository;

import com.hust.webchat.domain.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Long> {
    Optional<Emotion> findByUserIdAndPostingId(Long userId, Long postingId);
}
