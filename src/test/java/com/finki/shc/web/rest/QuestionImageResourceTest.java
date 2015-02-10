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
import com.finki.shc.domain.QuestionImage;
import com.finki.shc.repository.QuestionImageRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the QuestionImageResource REST controller.
 *
 * @see QuestionImageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class QuestionImageResourceTest {

    private static final String DEFAULT_IMAGE_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_IMAGE_NAME = "UPDATED_TEXT";

    @Inject
    private QuestionImageRepository questionImageRepository;

    private MockMvc restQuestionImageMockMvc;

    private QuestionImage questionImage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QuestionImageResource questionImageResource = new QuestionImageResource();
        ReflectionTestUtils.setField(questionImageResource, "questionImageRepository", questionImageRepository);
        this.restQuestionImageMockMvc = MockMvcBuilders.standaloneSetup(questionImageResource).build();
    }

    @Before
    public void initTest() {
        questionImage = new QuestionImage();
        questionImage.setImageName(DEFAULT_IMAGE_NAME);
    }

    @Test
    @Transactional
    public void createQuestionImage() throws Exception {
        // Validate the database is empty
        assertThat(questionImageRepository.findAll()).hasSize(0);

        // Create the QuestionImage
        restQuestionImageMockMvc.perform(post("/api/questionImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionImage)))
                .andExpect(status().isOk());

        // Validate the QuestionImage in the database
        List<QuestionImage> questionImages = questionImageRepository.findAll();
        assertThat(questionImages).hasSize(1);
        QuestionImage testQuestionImage = questionImages.iterator().next();
        assertThat(testQuestionImage.getImageName()).isEqualTo(DEFAULT_IMAGE_NAME);
    }

    @Test
    @Transactional
    public void getAllQuestionImages() throws Exception {
        // Initialize the database
        questionImageRepository.saveAndFlush(questionImage);

        // Get all the questionImages
        restQuestionImageMockMvc.perform(get("/api/questionImages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(questionImage.getId().intValue()))
                .andExpect(jsonPath("$.[0].imageName").value(DEFAULT_IMAGE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getQuestionImage() throws Exception {
        // Initialize the database
        questionImageRepository.saveAndFlush(questionImage);

        // Get the questionImage
        restQuestionImageMockMvc.perform(get("/api/questionImages/{id}", questionImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(questionImage.getId().intValue()))
            .andExpect(jsonPath("$.imageName").value(DEFAULT_IMAGE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuestionImage() throws Exception {
        // Get the questionImage
        restQuestionImageMockMvc.perform(get("/api/questionImages/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestionImage() throws Exception {
        // Initialize the database
        questionImageRepository.saveAndFlush(questionImage);

        // Update the questionImage
        questionImage.setImageName(UPDATED_IMAGE_NAME);
        restQuestionImageMockMvc.perform(post("/api/questionImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionImage)))
                .andExpect(status().isOk());

        // Validate the QuestionImage in the database
        List<QuestionImage> questionImages = questionImageRepository.findAll();
        assertThat(questionImages).hasSize(1);
        QuestionImage testQuestionImage = questionImages.iterator().next();
        assertThat(testQuestionImage.getImageName()).isEqualTo(UPDATED_IMAGE_NAME);
    }

    @Test
    @Transactional
    public void deleteQuestionImage() throws Exception {
        // Initialize the database
        questionImageRepository.saveAndFlush(questionImage);

        // Get the questionImage
        restQuestionImageMockMvc.perform(delete("/api/questionImages/{id}", questionImage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<QuestionImage> questionImages = questionImageRepository.findAll();
        assertThat(questionImages).hasSize(0);
    }
}
