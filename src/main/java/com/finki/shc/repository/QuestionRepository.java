package com.finki.shc.repository;

import com.finki.shc.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Question entity.
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findAll(Pageable pageable);

    Set<Question> findByTitleContainingIgnoreCase(String search);

    Set<Question> findBySolvedIs(Boolean solved);

    Set<Question> findByTagsNameIn(String[] tags);

    @Modifying
    @Transactional
    Long deleteByIdAndUserId(Long questionId, Long userId);

    Page<Question> findAllByUserId(Pageable pageable, Long userId);
}
