package com.finki.shc.repository;

import com.finki.shc.domain.QuestionVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the QuestionVote entity.
 */
public interface QuestionVoteRepository extends JpaRepository<QuestionVote,Long>{
    public List<QuestionVote> findAllByQuestionId(Long id);
    public QuestionVote findByQuestionIdAndUserId(Long idQuestion, Long User);

}
