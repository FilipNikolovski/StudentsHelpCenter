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
import com.finki.shc.domain.AnswerVote;
import com.finki.shc.repository.AnswerVoteRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AnswerVoteResource REST controller.
 *
 * @see AnswerVoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AnswerVoteResourceTest {


    private static final Integer DEFAULT_VOTE = 0;
    private static final Integer UPDATED_VOTE = 1;

    @Inject
    private AnswerVoteRepository answerVoteRepository;

    private MockMvc restAnswerVoteMockMvc;

    private AnswerVote answerVote;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnswerVoteResource answerVoteResource = new AnswerVoteResource();
        ReflectionTestUtils.setField(answerVoteResource, "answerVoteRepository", answerVoteRepository);
        this.restAnswerVoteMockMvc = MockMvcBuilders.standaloneSetup(answerVoteResource).build();
    }

    @Before
    public void initTest() {
        answerVote = new AnswerVote();
        answerVote.setVote(DEFAULT_VOTE);
    }

    @Test
    @Transactional
    public void createAnswerVote() throws Exception {
        // Validate the database is empty
        assertThat(answerVoteRepository.findAll()).hasSize(0);

        // Create the AnswerVote
        restAnswerVoteMockMvc.perform(post("/api/answerVotes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(answerVote)))
                .andExpect(status().isOk());

        // Validate the AnswerVote in the database
        List<AnswerVote> answerVotes = answerVoteRepository.findAll();
        assertThat(answerVotes).hasSize(1);
        AnswerVote testAnswerVote = answerVotes.iterator().next();
        assertThat(testAnswerVote.getVote()).isEqualTo(DEFAULT_VOTE);
    }

    @Test
    @Transactional
    public void getAllAnswerVotes() throws Exception {
        // Initialize the database
        answerVoteRepository.saveAndFlush(answerVote);

        // Get all the answerVotes
        restAnswerVoteMockMvc.perform(get("/api/answerVotes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(answerVote.getId().intValue()))
                .andExpect(jsonPath("$.[0].vote").value(DEFAULT_VOTE));
    }

    @Test
    @Transactional
    public void getAnswerVote() throws Exception {
        // Initialize the database
        answerVoteRepository.saveAndFlush(answerVote);

        // Get the answerVote
        restAnswerVoteMockMvc.perform(get("/api/answerVotes/{id}", answerVote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(answerVote.getId().intValue()))
            .andExpect(jsonPath("$.vote").value(DEFAULT_VOTE));
    }

    @Test
    @Transactional
    public void getNonExistingAnswerVote() throws Exception {
        // Get the answerVote
        restAnswerVoteMockMvc.perform(get("/api/answerVotes/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnswerVote() throws Exception {
        // Initialize the database
        answerVoteRepository.saveAndFlush(answerVote);

        // Update the answerVote
        answerVote.setVote(UPDATED_VOTE);
        restAnswerVoteMockMvc.perform(post("/api/answerVotes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(answerVote)))
                .andExpect(status().isOk());

        // Validate the AnswerVote in the database
        List<AnswerVote> answerVotes = answerVoteRepository.findAll();
        assertThat(answerVotes).hasSize(1);
        AnswerVote testAnswerVote = answerVotes.iterator().next();
        assertThat(testAnswerVote.getVote()).isEqualTo(UPDATED_VOTE);
    }

    @Test
    @Transactional
    public void deleteAnswerVote() throws Exception {
        // Initialize the database
        answerVoteRepository.saveAndFlush(answerVote);

        // Get the answerVote
        restAnswerVoteMockMvc.perform(delete("/api/answerVotes/{id}", answerVote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AnswerVote> answerVotes = answerVoteRepository.findAll();
        assertThat(answerVotes).hasSize(0);
    }
}
