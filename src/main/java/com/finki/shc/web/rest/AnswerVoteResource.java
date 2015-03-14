package com.finki.shc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.finki.shc.domain.AnswerVote;
import com.finki.shc.repository.AnswerVoteRepository;
import com.finki.shc.service.AnswerService;
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
 * REST controller for managing AnswerVote.
 */
@RestController
@RequestMapping("/api/answers/{id}")
public class AnswerVoteResource {

    private final Logger log = LoggerFactory.getLogger(AnswerVoteResource.class);

    @Inject
    private AnswerVoteRepository answerVoteRepository;

    @Inject
    private AnswerService answerService;

    /**
     * POST  /answerVotes -> Create a new answerVote.
     */
    @RequestMapping(value = "/votes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> create(@PathVariable Long id, @RequestBody AnswerVote answerVote) {
        log.debug("REST request to save AnswerVote : {} answer id: {}", answerVote, id);
        return Optional.ofNullable(answerService.addVote(id, answerVote))
            .map(responseVote -> new ResponseEntity<>(responseVote, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }

    /**
     * GET  /answerVotes -> get all the answerVotes.
     */
    @RequestMapping(value = "/votes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AnswerVote> getAll() {
        log.debug("REST request to get all AnswerVotes");
        return answerVoteRepository.findAll();
    }

    /**
     * GET  /answerVotes/:id -> get the "id" answerVote.
     */
    @RequestMapping(value = "/votes/{voteId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AnswerVote> get(@PathVariable Long id, @PathVariable Long voteId) {
        log.debug("REST request to get AnswerVote : {}", id);
        return Optional.ofNullable(answerVoteRepository.findOne(voteId))
            .map(questionVote -> new ResponseEntity<>(
                questionVote,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /answerVotes/:id -> delete the "id" answerVote.
     */
    @RequestMapping(value = "/votes/{voteId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id, @PathVariable Long voteId) {
        log.debug("REST request to delete AnswerVote : {}", voteId);
        answerVoteRepository.delete(voteId);
    }
}
