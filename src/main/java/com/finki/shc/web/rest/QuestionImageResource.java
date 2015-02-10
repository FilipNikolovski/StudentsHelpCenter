package com.finki.shc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.finki.shc.domain.QuestionImage;
import com.finki.shc.repository.QuestionImageRepository;
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
 * REST controller for managing QuestionImage.
 */
@RestController
@RequestMapping("/api")
public class QuestionImageResource {

    private final Logger log = LoggerFactory.getLogger(QuestionImageResource.class);

    @Inject
    private QuestionImageRepository questionImageRepository;

    /**
     * POST  /questionImages -> Create a new questionImage.
     */
    @RequestMapping(value = "/questionImages",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody QuestionImage questionImage) {
        log.debug("REST request to save QuestionImage : {}", questionImage);
        questionImageRepository.save(questionImage);
    }

    /**
     * GET  /questionImages -> get all the questionImages.
     */
    @RequestMapping(value = "/questionImages",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<QuestionImage> getAll() {
        log.debug("REST request to get all QuestionImages");
        return questionImageRepository.findAll();
    }

    /**
     * GET  /questionImages/:id -> get the "id" questionImage.
     */
    @RequestMapping(value = "/questionImages/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QuestionImage> get(@PathVariable Long id) {
        log.debug("REST request to get QuestionImage : {}", id);
        return Optional.ofNullable(questionImageRepository.findOne(id))
            .map(questionImage -> new ResponseEntity<>(
                questionImage,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /questionImages/:id -> delete the "id" questionImage.
     */
    @RequestMapping(value = "/questionImages/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete QuestionImage : {}", id);
        questionImageRepository.delete(id);
    }
}
