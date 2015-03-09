package com.finki.shc.repository;

import com.finki.shc.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Spring Data JPA repository for the Answer entity.
 */
public interface AnswerRepository extends JpaRepository<Answer,Long>{

    List<Answer> findAllByQuestionId(Long id);

    @Modifying
    @Transactional
    void deleteByIdAndUserId(Long answerId, Long userId);
}
