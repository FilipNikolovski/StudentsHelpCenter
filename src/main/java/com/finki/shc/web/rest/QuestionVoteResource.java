package com.finki.shc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.finki.shc.domain.QuestionVote;
import com.finki.shc.repository.QuestionVoteRepository;
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
 * REST controller for managing QuestionVote.
 */
@RestController
@RequestMapping("/api")
public class QuestionVoteResource {

    private final Logger log = LoggerFactory.getLogger(QuestionVoteResource.class);

    @Inject
    private QuestionVoteRepository questionVoteRepository;

    /**
     * POST  /questionVotes -> Create a new questionVote.
     */
    @RequestMapping(value = "/questionVotes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody QuestionVote questionVote) {
        log.debug("REST request to save QuestionVote : {}", questionVote);
        questionVoteRepository.save(questionVote);
    }

    /**
     * GET  /questionVotes -> get all the questionVotes.
     */
    @RequestMapping(value = "/questionVotes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<QuestionVote> getAll() {
        log.debug("REST request to get all QuestionVotes");
        return questionVoteRepository.findAll();
    }

    /**
     * GET  /questionVotes/:id -> get the "id" questionVote.
     */
    @RequestMapping(value = "/questionVotes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QuestionVote> get(@PathVariable Long id) {
        log.debug("REST request to get QuestionVote : {}", id);
        return Optional.ofNullable(questionVoteRepository.findOne(id))
            .map(questionVote -> new ResponseEntity<>(
                questionVote,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /questionVotes/:id -> delete the "id" questionVote.
     */
    @RequestMapping(value = "/questionVotes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete QuestionVote : {}", id);
        questionVoteRepository.delete(id);
    }
}
