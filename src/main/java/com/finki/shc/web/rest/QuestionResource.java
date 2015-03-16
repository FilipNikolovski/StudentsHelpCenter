package com.finki.shc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.finki.shc.domain.Question;
import com.finki.shc.domain.User;
import com.finki.shc.repository.QuestionRepository;
import com.finki.shc.repository.UserRepository;
import com.finki.shc.security.AuthoritiesConstants;
import com.finki.shc.security.SecurityUtils;
import com.finki.shc.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import java.util.*;

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

    @Inject
    private QuestionService questionService;

    /**
     * POST  /questions -> Create a new question.
     */
    @RequestMapping(value = "/questions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN})
    public ResponseEntity<?> create(@Valid @RequestBody Question question) {
        log.debug("REST request to save Question : {}", question);
        return Optional.ofNullable(questionService.createQuestion(question))
            .map(q -> new ResponseEntity<>(q.get(), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }

    /**
     * GET  /questions -> get all the questions.
     */
    @RequestMapping(value = "/questions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<Question> getAll(Pageable pageable, @RequestParam(required = false) String search, @RequestParam(required = false) Boolean solved, @RequestParam(required = false) String tags) {
        Set<Question> questions = new HashSet<>();

        if (!search.isEmpty() || solved != null || !tags.isEmpty()) {
            log.debug("REST request to get all questions with request params - search: {} solved: {} tags:{}", search, solved, tags);
            if (!search.isEmpty())
                questions.addAll(questionRepository.findByTitleContainingIgnoreCase(search.trim()));
            if(solved != null)
                questions.addAll(questionRepository.findBySolvedIs(solved));
            if(tags != null)
                questions.addAll(questionRepository.findByTagsNameIn(tags.split(",")));

            return new PageImpl<>(new ArrayList<>(questions), pageable, questions.size());
        }

        log.debug("REST request to get all Questions");
        return questionRepository.findAll(pageable);
    }

    @RequestMapping(value = "/my-questions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Page<Question>> getUser(Pageable pageable) {
        if (!SecurityUtils.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User u = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        return new ResponseEntity<>(questionRepository.findAllByUserId(pageable, u.getId()), HttpStatus.OK);
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
    @RolesAllowed({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.debug("REST request to delete Question : {}", id);

        if (!SecurityUtils.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        User u = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();

        if (SecurityUtils.checkAuthority(AuthoritiesConstants.ADMIN)) { //Delete question if the user is administrator
            questionRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        questionRepository.deleteByIdAndUserId(id, u.getId()); //Delete the question if the current logged in user is the creator of that question
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * UPDATE  /questions/:id -> update the "id" question.
     */
    @RequestMapping(value = "/questions/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN})
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Question question) {
        log.debug("REST request to update Question : {}", id);
        return Optional.ofNullable(questionService.createQuestion(question))
            .map(q -> new ResponseEntity<>(q.get(), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }
}
