package com.finki.shc.repository;

import com.finki.shc.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Tag entity.
 */
public interface TagRepository extends JpaRepository<Tag,Long>{
    List<Tag> findByNameContainingIgnoreCase(String search);
}
