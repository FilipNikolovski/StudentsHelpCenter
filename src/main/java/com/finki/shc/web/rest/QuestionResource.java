package com.finki.shc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.finki.shc.domain.Question;
import com.finki.shc.domain.User;
import com.finki.shc.repository.QuestionRepository;
import com.finki.shc.repository.UserRepository;
import com.finki.shc.security.AuthoritiesConstants;
import com.finki.shc.security.SecurityUtils;
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
 * REST controller for managing Question.
 */
@RestController
@RequestMapping("/api")
public class QuestionResource {

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /questions -> Create a new question.
     */
    @RequestMapping(value = "/questions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN })
    public ResponseEntity<?> create(@RequestBody Question question) {
        log.debug("REST request to save Question : {}", question);
        if (!SecurityUtils.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        question.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        questionRepository.save(question);
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    /**
     * GET  /questions -> get all the questions.
     */
    @RequestMapping(value = "/questions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Question> getAll() {
        log.debug("REST request to get all Questions");
        return questionRepository.findAll();
    }

    /**
     * GET  /questions/:id -> get the "id" question.
     */
    @RequestMapping(value = "/questions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Question> get(@PathVariable Long id) {
        log.debug("REST request to get Question : {}", id);
        return Optional.ofNullable(questionRepository.findOne(id))
            .map(question -> new ResponseEntity<>(
                question,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /questions/:id -> delete the "id" question.
     */
    @RequestMapping(value = "/questions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed({ AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN })
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.debug("REST request to delete Question : {}", id);

        if (!SecurityUtils.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        User u = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();

        if(SecurityUtils.checkAuthority(AuthoritiesConstants.ADMIN)) { //Delete question if the user is administrator
            questionRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        questionRepository.deleteByIdAndUserId(id, u.getId()); //Delete the question if the current logged in user is the creator of that question
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
