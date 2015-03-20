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
import org.springframework.web.multipart.MultipartFile;

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

            if (tags != null)
                questions.addAll(questionRepository.findByTagsNameIn(tags.split(",")));

            questions.addAll(questionRepository.findBySolvedIs(solved));

            List<Question> distinctQuestions = new ArrayList<>(questions);

            if (pageable.getPageSize() >= distinctQuestions.size()) {
                return new PageImpl<>(distinctQuestions, pageable, distinctQuestions.size());
            }

            if (pageable.getOffset() + pageable.getPageSize() >= distinctQuestions.size()) {
                int offset = (pageable.getOffset() == distinctQuestions.size() - 1) ? pageable.getOffset() - 1 : pageable.getOffset();
                return new PageImpl<>(distinctQuestions.subList(offset, distinctQuestions.size() - 1), pageable, distinctQuestions.size());
            }

            return new PageImpl<>(distinctQuestions.subList(pageable.getOffset(), pageable.getOffset() + pageable.getPageSize() - 1), pageable, distinctQuestions.size());
        }

        log.debug("REST request to get all Questions");
        return questionRepository.findAll(pageable);
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

        if(questionService.deleteQuestion(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    @RequestMapping(value = "/my-questions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Page<Question>> getUserQuestions(Pageable pageable) {
        if (!SecurityUtils.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User u = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        return new ResponseEntity<>(questionRepository.findAllByUserId(pageable, u.getId()), HttpStatus.OK);
    }

    @RequestMapping(value = "/questions/upload-images",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN})
    public ResponseEntity<?> update(@RequestParam("id") String id, @RequestParam("file") List<MultipartFile> file) {
        log.debug("REST request to update Question : {} files: {}", id, file);

        if(questionService.uploadImages(Long.parseLong(id), file)) {
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
