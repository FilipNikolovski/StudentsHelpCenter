package com.finki.shc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.finki.shc.domain.QuestionTag;
import com.finki.shc.repository.QuestionTagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing QuestionTag.
 */
@RestController
@RequestMapping("/api")
public class QuestionTagResource {

    private final Logger log = LoggerFactory.getLogger(QuestionTagResource.class);

    @Inject
    private QuestionTagRepository questionTagRepository;

    /**
     * POST  /questionTags -> Create a new questionTag.
     */
    @RequestMapping(value = "/questionTags",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody QuestionTag questionTag) {
        log.debug("REST request to save QuestionTag : {}", questionTag);
        questionTagRepository.save(questionTag);
    }

    /**
     * GET  /questionTags -> get all the questionTags.
     */
    @RequestMapping(value = "/questionTags",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<QuestionTag> getAll() {
        log.debug("REST request to get all QuestionTags");
        return questionTagRepository.findAll();
    }

    /**
     * GET  /questionTags/:id -> get the "id" questionTag.
     */
    @RequestMapping(value = "/questionTags/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QuestionTag> get(@PathVariable Long id) {
        log.debug("REST request to get QuestionTag : {}", id);
        return Optional.ofNullable(questionTagRepository.findOne(id))
            .map(questionTag -> new ResponseEntity<>(
                questionTag,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /questionTags/:id -> delete the "id" questionTag.
     */
    @RequestMapping(value = "/questionTags/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete QuestionTag : {}", id);
        questionTagRepository.delete(id);
    }
}
