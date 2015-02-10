package com.finki.shc.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import com.finki.shc.Application;
import com.finki.shc.domain.QuestionTag;
import com.finki.shc.repository.QuestionTagRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the QuestionTagResource REST controller.
 *
 * @see QuestionTagResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class QuestionTagResourceTest {


    @Inject
    private QuestionTagRepository questionTagRepository;

    private MockMvc restQuestionTagMockMvc;

    private QuestionTag questionTag;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QuestionTagResource questionTagResource = new QuestionTagResource();
        ReflectionTestUtils.setField(questionTagResource, "questionTagRepository", questionTagRepository);
        this.restQuestionTagMockMvc = MockMvcBuilders.standaloneSetup(questionTagResource).build();
    }

    @Before
    public void initTest() {
        questionTag = new QuestionTag();
    }

    @Test
    @Transactional
    public void createQuestionTag() throws Exception {
        // Validate the database is empty
        assertThat(questionTagRepository.findAll()).hasSize(0);

        // Create the QuestionTag
        restQuestionTagMockMvc.perform(post("/api/questionTags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionTag)))
                .andExpect(status().isOk());

        // Validate the QuestionTag in the database
        List<QuestionTag> questionTags = questionTagRepository.findAll();
        assertThat(questionTags).hasSize(1);
        QuestionTag testQuestionTag = questionTags.iterator().next();
    }

    @Test
    @Transactional
    public void getAllQuestionTags() throws Exception {
        // Initialize the database
        questionTagRepository.saveAndFlush(questionTag);

        // Get all the questionTags
        restQuestionTagMockMvc.perform(get("/api/questionTags"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(questionTag.getId().intValue()));
    }

    @Test
    @Transactional
    public void getQuestionTag() throws Exception {
        // Initialize the database
        questionTagRepository.saveAndFlush(questionTag);

        // Get the questionTag
        restQuestionTagMockMvc.perform(get("/api/questionTags/{id}", questionTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(questionTag.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingQuestionTag() throws Exception {
        // Get the questionTag
        restQuestionTagMockMvc.perform(get("/api/questionTags/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestionTag() throws Exception {
        // Initialize the database
        questionTagRepository.saveAndFlush(questionTag);

        // Update the questionTag
        restQuestionTagMockMvc.perform(post("/api/questionTags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionTag)))
                .andExpect(status().isOk());

        // Validate the QuestionTag in the database
        List<QuestionTag> questionTags = questionTagRepository.findAll();
        assertThat(questionTags).hasSize(1);
        QuestionTag testQuestionTag = questionTags.iterator().next();
    }

    @Test
    @Transactional
    public void deleteQuestionTag() throws Exception {
        // Initialize the database
        questionTagRepository.saveAndFlush(questionTag);

        // Get the questionTag
        restQuestionTagMockMvc.perform(delete("/api/questionTags/{id}", questionTag.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<QuestionTag> questionTags = questionTagRepository.findAll();
        assertThat(questionTags).hasSize(0);
    }
}
