package com.finki.shc.repository;

import com.finki.shc.domain.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Spring Data JPA repository for the Answer entity.
 */
public interface AnswerRepository extends JpaRepository<Answer,Long>{

    Page<Answer> findAllByQuestionId(Long id, Pageable pageable);

    @Modifying
    @Transactional
    Long deleteByIdAndUserId(Long answerId, Long userId);

}
