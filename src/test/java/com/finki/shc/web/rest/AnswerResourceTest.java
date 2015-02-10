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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import com.finki.shc.Application;
import com.finki.shc.domain.Answer;
import com.finki.shc.repository.AnswerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AnswerResource REST controller.
 *
 * @see AnswerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AnswerResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_ANSWER_TEXT = "SAMPLE_TEXT";
    private static final String UPDATED_ANSWER_TEXT = "UPDATED_TEXT";

    private static final DateTime DEFAULT_DATE_POSTED = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATE_POSTED = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATE_POSTED_STR = dateTimeFormatter.print(DEFAULT_DATE_POSTED);

    @Inject
    private AnswerRepository answerRepository;

    private MockMvc restAnswerMockMvc;

    private Answer answer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnswerResource answerResource = new AnswerResource();
        ReflectionTestUtils.setField(answerResource, "answerRepository", answerRepository);
        this.restAnswerMockMvc = MockMvcBuilders.standaloneSetup(answerResource).build();
    }

    @Before
    public void initTest() {
        answer = new Answer();
        answer.setAnswerText(DEFAULT_ANSWER_TEXT);
        answer.setDatePosted(DEFAULT_DATE_POSTED);
    }

    @Test
    @Transactional
    public void createAnswer() throws Exception {
        // Validate the database is empty
        assertThat(answerRepository.findAll()).hasSize(0);

        // Create the Answer
        restAnswerMockMvc.perform(post("/api/answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(answer)))
                .andExpect(status().isOk());

        // Validate the Answer in the database
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers).hasSize(1);
        Answer testAnswer = answers.iterator().next();
        assertThat(testAnswer.getAnswerText()).isEqualTo(DEFAULT_ANSWER_TEXT);
        assertThat(testAnswer.getDatePosted().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DATE_POSTED);
    }

    @Test
    @Transactional
    public void getAllAnswers() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answers
        restAnswerMockMvc.perform(get("/api/answers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(answer.getId().intValue()))
                .andExpect(jsonPath("$.[0].answerText").value(DEFAULT_ANSWER_TEXT.toString()))
                .andExpect(jsonPath("$.[0].datePosted").value(DEFAULT_DATE_POSTED_STR));
    }

    @Test
    @Transactional
    public void getAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get the answer
        restAnswerMockMvc.perform(get("/api/answers/{id}", answer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(answer.getId().intValue()))
            .andExpect(jsonPath("$.answerText").value(DEFAULT_ANSWER_TEXT.toString()))
            .andExpect(jsonPath("$.datePosted").value(DEFAULT_DATE_POSTED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingAnswer() throws Exception {
        // Get the answer
        restAnswerMockMvc.perform(get("/api/answers/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Update the answer
        answer.setAnswerText(UPDATED_ANSWER_TEXT);
        answer.setDatePosted(UPDATED_DATE_POSTED);
        restAnswerMockMvc.perform(post("/api/answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(answer)))
                .andExpect(status().isOk());

        // Validate the Answer in the database
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers).hasSize(1);
        Answer testAnswer = answers.iterator().next();
        assertThat(testAnswer.getAnswerText()).isEqualTo(UPDATED_ANSWER_TEXT);
        assertThat(testAnswer.getDatePosted().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DATE_POSTED);
    }

    @Test
    @Transactional
    public void deleteAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get the answer
        restAnswerMockMvc.perform(delete("/api/answers/{id}", answer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers).hasSize(0);
    }
}
