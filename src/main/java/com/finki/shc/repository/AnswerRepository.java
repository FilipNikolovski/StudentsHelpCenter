package com.finki.shc.repository;

import com.finki.shc.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Spring Data JPA repository for the Answer entity.
 */
public interface AnswerRepository extends JpaRepository<Answer,Long>{

    List<Answer> findAllByQuestionId(Long id);

    @Modifying
    @Transactional
    @Query("delete from Answer a where a.id = ?1 and a.user.id = ?2")
    void deleteByIdAndUserId(Long answerId, Long userId);

    @Modifying
    @Transactional
    @Query("delete from Answer a where a.id = ?1")
    void delete(Long answerId);

}
