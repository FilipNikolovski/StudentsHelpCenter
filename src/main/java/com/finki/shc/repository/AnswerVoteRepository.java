package com.finki.shc.repository;

import com.finki.shc.domain.AnswerVote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the AnswerVote entity.
 */
public interface AnswerVoteRepository extends JpaRepository<AnswerVote,Long>{

}
