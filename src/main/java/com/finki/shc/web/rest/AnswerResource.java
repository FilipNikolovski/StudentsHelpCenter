package com.finki.shc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.finki.shc.domain.Answer;
import com.finki.shc.repository.AnswerRepository;
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
 * REST controller for managing Answer.
 */
@RestController
@RequestMapping("/api")
public class AnswerResource {

    private final Logger log = LoggerFactory.getLogger(AnswerResource.class);

    @Inject
    private AnswerRepository answerRepository;

    /**
     * POST  /answers -> Create a new answer.
     */
    @RequestMapping(value = "/answers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Answer answer) {
        log.debug("REST request to save Answer : {}", answer);
        answerRepository.save(answer);
    }

    /**
     * GET  /answers -> get all the answers.
     */
    @RequestMapping(value = "/answers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Answer> getAll() {
        log.debug("REST request to get all Answers");
        return answerRepository.findAll();
    }

    /**
     * GET  /answers/:id -> get the "id" answer.
     */
    @RequestMapping(value = "/answers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Answer> get(@PathVariable Long id) {
        log.debug("REST request to get Answer : {}", id);
        return Optional.ofNullable(answerRepository.findOne(id))
            .map(answer -> new ResponseEntity<>(
                answer,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /answers/:id -> delete the "id" answer.
     */
    @RequestMapping(value = "/answers/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Answer : {}", id);
        answerRepository.delete(id);
    }
}
