package com.finki.shc.repository;

import com.finki.shc.domain.QuestionVote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the QuestionVote entity.
 */
public interface QuestionVoteRepository extends JpaRepository<QuestionVote,Long>{

}
