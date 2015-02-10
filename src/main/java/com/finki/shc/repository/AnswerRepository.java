package com.finki.shc.repository;

import com.finki.shc.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Answer entity.
 */
public interface AnswerRepository extends JpaRepository<Answer,Long>{

}
