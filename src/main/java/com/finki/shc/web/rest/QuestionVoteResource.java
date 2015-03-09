package com.finki.shc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.finki.shc.domain.QuestionVote;
import com.finki.shc.repository.QuestionVoteRepository;
import com.finki.shc.security.AuthoritiesConstants;
import com.finki.shc.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing QuestionVote.
 */
@RestController
@RequestMapping("/api/questions/{id}")
public class QuestionVoteResource {

    private final Logger log = LoggerFactory.getLogger(QuestionVoteResource.class);

    @Inject
    private QuestionVoteRepository questionVoteRepository;

    @Inject
    private QuestionService questionService;

    /**
     * POST  /votes -> Create a new questionVote.
     */
    @RequestMapping(value = "/votes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN })
    public ResponseEntity<?> create(@PathVariable Long id, @RequestBody QuestionVote questionVote) {
        log.debug("REST request to save QuestionVote : {}", questionVote);
        return Optional.ofNullable(questionService.addVote(id, questionVote))
            .map(responseVote -> new ResponseEntity<>(responseVote, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }

    /**
     * GET  /votes -> get all the questionVotes.
     */
    @RequestMapping(value = "/votes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<QuestionVote> getAll() {
        log.debug("REST request to get all QuestionVotes");
        return questionVoteRepository.findAll();
    }

    /**
     * GET  /votes/:id -> get the "id" questionVote.
     */
    @RequestMapping(value = "/votes/{voteId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QuestionVote> get(@PathVariable Long id, @PathVariable Long voteId) {
        log.debug("REST request to get QuestionVote : {}", voteId);
        return Optional.ofNullable(questionVoteRepository.findOne(voteId))
            .map(questionVote -> new ResponseEntity<>(
                questionVote,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /votes/:id -> delete the "id" questionVote.
     */
    @RequestMapping(value = "/votes/{voteId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id, @PathVariable Long voteId) {
        log.debug("REST request to delete QuestionVote : {}", voteId);
        questionVoteRepository.delete(voteId);
    }
}
