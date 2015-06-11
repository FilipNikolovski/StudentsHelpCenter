package com.finki.shc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.finki.shc.domain.Tag;
import com.finki.shc.repository.TagRepository;
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
 * REST controller for managing Tag.
 */
@RestController
@RequestMapping("/api")
public class TagResource {

    private final Logger log = LoggerFactory.getLogger(TagResource.class);

    @Inject
    private TagRepository tagRepository;

    /**
     * POST  /tags -> Create a new tag.
     */
    @RequestMapping(value = "/tags",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Tag tag) {
        log.debug("REST request to save Tag : {}", tag);
        tagRepository.save(tag);
    }

    /**
     * GET  /tags -> get all the tags.
     */
    @RequestMapping(value = "/tags",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tag> getAll(@RequestParam(required = false) String search) {
        log.debug("REST request to get all Tags");
        if(!search.isEmpty())
            return tagRepository.findByNameContainingIgnoreCase(search);
        return tagRepository.findAll();
    }

    /**
     * GET  /tags/:id -> get the "id" tag.
     */
    @RequestMapping(value = "/tags/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tag> get(@PathVariable Long id) {
        log.debug("REST request to get Tag : {}", id);
        return Optional.ofNullable(tagRepository.findOne(id))
            .map(tag -> new ResponseEntity<>(
                tag,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tags/:id -> delete the "id" tag.
     */
    @RequestMapping(value = "/tags/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Tag : {}", id);
        tagRepository.delete(id);
    }
}
