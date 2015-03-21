package com.finki.shc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.finki.shc.domain.QuestionImage;
import com.finki.shc.repository.QuestionImageRepository;
import com.finki.shc.security.AuthoritiesConstants;
import com.finki.shc.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
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

    @Inject
    private QuestionService questionService;

    /**
     * POST  /questionImages -> Create a new questionImage.
     */
    @RequestMapping(value = "/questions/images",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> create(@RequestParam("id") String id, @RequestParam("file") List<MultipartFile> file) {
        log.debug("REST request to update Question : {} files: {}", id, file);
        if(questionService.uploadImages(Long.parseLong(id), file)) {
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * GET  /questionImages -> get all the questionImages.
     */
    @RequestMapping(value = "/questions/{id}/images",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<QuestionImage> getAll(@PathVariable Long id) {
        log.debug("REST request to get all QuestionImages");
        return questionImageRepository.findAllByQuestionId(id);
    }

    /**
     * GET  /questionImages/:id -> get the "id" questionImage.
     */
    @RequestMapping(value = "/questions/{id}/images/{imageId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QuestionImage> get(@PathVariable Long id, @PathVariable Long imageId) {
        log.debug("REST request to get QuestionImage : {}, questionId : {}", imageId, id);
        return Optional.ofNullable(questionImageRepository.findOne(imageId))
            .map(questionImage -> new ResponseEntity<>(
                questionImage,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /images/:id -> delete the "id" questionImage.
     */
    @RequestMapping(value = "/questions/{id}/images/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id, @PathVariable Long imageId) {
        log.debug("REST request to delete QuestionImage : {}", imageId);
        questionImageRepository.delete(imageId);
    }
}
