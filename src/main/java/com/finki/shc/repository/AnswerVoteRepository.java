package com.finki.shc.repository;

import com.finki.shc.domain.AnswerVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the AnswerVote entity.
 */
public interface AnswerVoteRepository extends JpaRepository<AnswerVote,Long>{
    public List<AnswerVote> findAllByAnswerId(Long id);
    public AnswerVote findByAnswerIdAndUserId(Long idAnswer, Long User);

}
