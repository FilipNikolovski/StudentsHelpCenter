package com.finki.shc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.finki.shc.domain.Answer;
import com.finki.shc.domain.Question;
import com.finki.shc.domain.User;
import com.finki.shc.repository.AnswerRepository;
import com.finki.shc.repository.QuestionRepository;
import com.finki.shc.repository.UserRepository;
import com.finki.shc.security.AuthoritiesConstants;
import com.finki.shc.security.SecurityUtils;
import com.finki.shc.service.AnswerService;
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
 * REST controller for managing Answer.
 */
@RestController
@RequestMapping("/api/questions/{id}")
public class AnswerResource {

    private final Logger log = LoggerFactory.getLogger(AnswerResource.class);

    @Inject
    private AnswerRepository answerRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AnswerService answerService;

    /**
     * POST  /answers -> Create a new answer.
     */
    @RequestMapping(value = "/answers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN })
    public ResponseEntity<?> create(@PathVariable Long id, @RequestBody Answer answer) {
        log.debug("REST request to save Answer : {}, Question : {}", answer, id);
        return Optional.ofNullable(answerService.createAnswer(id, answer))
            .map(responseAnswer -> new ResponseEntity<>(responseAnswer, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }

    /**
     * GET  /answers -> get all the answers.
     */
    @RequestMapping(value = "/answers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Answer> getAll(@PathVariable Long id) {
        log.debug("REST request to get all Answers");
        return answerRepository.findAllByQuestionId(id);
    }

    /**
     * GET  /answers/:id -> get the "id" answer.
     */
    @RequestMapping(value = "/answers/{answerId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Answer> get(@PathVariable Long id, @PathVariable Long answerId) {
        log.debug("REST request to get Answer : {}", answerId);
        return Optional.ofNullable(answerRepository.findOne(answerId))
            .map(answer -> new ResponseEntity<>(
                answer,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /answers/:id -> delete the "id" answer.
     */
    @RequestMapping(value = "/answers/{answerId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> delete(@PathVariable Long id, @PathVariable Long answerId) {
        log.debug("REST request to delete Answer : {}", answerId);
        if (!SecurityUtils.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        User u = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();

        if(SecurityUtils.checkAuthority(AuthoritiesConstants.ADMIN)) { //Delete answer if the user is administrator
            answerRepository.delete(answerId);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        answerRepository.deleteByIdAndUserId(answerId, u.getId()); //Delete the answer if the current logged in user is the creator of that answer
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
