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
import com.finki.shc.domain.QuestionVote;
import com.finki.shc.repository.QuestionVoteRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the QuestionVoteResource REST controller.
 *
 * @see QuestionVoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class QuestionVoteResourceTest {


    private static final Integer DEFAULT_VOTE = 0;
    private static final Integer UPDATED_VOTE = 1;

    @Inject
    private QuestionVoteRepository questionVoteRepository;

    private MockMvc restQuestionVoteMockMvc;

    private QuestionVote questionVote;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QuestionVoteResource questionVoteResource = new QuestionVoteResource();
        ReflectionTestUtils.setField(questionVoteResource, "questionVoteRepository", questionVoteRepository);
        this.restQuestionVoteMockMvc = MockMvcBuilders.standaloneSetup(questionVoteResource).build();
    }

    @Before
    public void initTest() {
        questionVote = new QuestionVote();
        questionVote.setVote(DEFAULT_VOTE);
    }

    @Test
    @Transactional
    public void createQuestionVote() throws Exception {
        // Validate the database is empty
        assertThat(questionVoteRepository.findAll()).hasSize(0);

        // Create the QuestionVote
        restQuestionVoteMockMvc.perform(post("/api/questionVotes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionVote)))
                .andExpect(status().isOk());

        // Validate the QuestionVote in the database
        List<QuestionVote> questionVotes = questionVoteRepository.findAll();
        assertThat(questionVotes).hasSize(1);
        QuestionVote testQuestionVote = questionVotes.iterator().next();
        assertThat(testQuestionVote.getVote()).isEqualTo(DEFAULT_VOTE);
    }

    @Test
    @Transactional
    public void getAllQuestionVotes() throws Exception {
        // Initialize the database
        questionVoteRepository.saveAndFlush(questionVote);

        // Get all the questionVotes
        restQuestionVoteMockMvc.perform(get("/api/questionVotes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(questionVote.getId().intValue()))
                .andExpect(jsonPath("$.[0].vote").value(DEFAULT_VOTE));
    }

    @Test
    @Transactional
    public void getQuestionVote() throws Exception {
        // Initialize the database
        questionVoteRepository.saveAndFlush(questionVote);

        // Get the questionVote
        restQuestionVoteMockMvc.perform(get("/api/questionVotes/{id}", questionVote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(questionVote.getId().intValue()))
            .andExpect(jsonPath("$.vote").value(DEFAULT_VOTE));
    }

    @Test
    @Transactional
    public void getNonExistingQuestionVote() throws Exception {
        // Get the questionVote
        restQuestionVoteMockMvc.perform(get("/api/questionVotes/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestionVote() throws Exception {
        // Initialize the database
        questionVoteRepository.saveAndFlush(questionVote);

        // Update the questionVote
        questionVote.setVote(UPDATED_VOTE);
        restQuestionVoteMockMvc.perform(post("/api/questionVotes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionVote)))
                .andExpect(status().isOk());

        // Validate the QuestionVote in the database
        List<QuestionVote> questionVotes = questionVoteRepository.findAll();
        assertThat(questionVotes).hasSize(1);
        QuestionVote testQuestionVote = questionVotes.iterator().next();
        assertThat(testQuestionVote.getVote()).isEqualTo(UPDATED_VOTE);
    }

    @Test
    @Transactional
    public void deleteQuestionVote() throws Exception {
        // Initialize the database
        questionVoteRepository.saveAndFlush(questionVote);

        // Get the questionVote
        restQuestionVoteMockMvc.perform(delete("/api/questionVotes/{id}", questionVote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<QuestionVote> questionVotes = questionVoteRepository.findAll();
        assertThat(questionVotes).hasSize(0);
    }
}
