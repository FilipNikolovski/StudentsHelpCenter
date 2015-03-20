package com.finki.shc.repository;

import com.finki.shc.domain.QuestionVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data JPA repository for the QuestionVote entity.
 */
public interface QuestionVoteRepository extends JpaRepository<QuestionVote, Long> {

    Optional<QuestionVote> findOneByQuestionIdAndUserId(Long questionId, Long userId);
    List<QuestionVote> findAllByQuestionId(Long id);
}
