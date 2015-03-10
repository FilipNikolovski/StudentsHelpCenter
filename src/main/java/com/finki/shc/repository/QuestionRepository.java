package com.finki.shc.repository;

import com.finki.shc.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * Spring Data JPA repository for the Question entity.
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Modifying
    @Transactional
    @Query("delete from Question q where q.id = ?1 and q.user.id = ?2")
    void deleteByIdAndUserId(Long questionId, Long userId);

}
