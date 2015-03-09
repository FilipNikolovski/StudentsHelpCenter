package com.finki.shc.repository;

import com.finki.shc.domain.AnswerVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the AnswerVote entity.
 */
public interface AnswerVoteRepository extends JpaRepository<AnswerVote,Long>{
    Optional<AnswerVote> findOneByAnswerIdAndUserId(Long answerId, Long userId);

}
