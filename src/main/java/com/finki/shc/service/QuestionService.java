package com.finki.shc.service;

import com.finki.shc.domain.Question;
import com.finki.shc.domain.QuestionVote;
import com.finki.shc.repository.QuestionRepository;
import com.finki.shc.repository.QuestionVoteRepository;
import com.finki.shc.repository.UserRepository;
import com.finki.shc.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

@Service
@Transactional
public class QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionService.class);

    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private QuestionVoteRepository questionVoteRepository;

    @Inject
    private UserRepository userRepository;

    public Optional<Question> createQuestion(Question question) {
        return userRepository.findOneByLogin(SecurityUtils.getCurrentLogin())
            .map(user -> {
                question.setUser(user);
                questionRepository.save(question);
                return question;
            });
    }

    public Optional<QuestionVote> addVote(Long questionId, QuestionVote vote) {
        return userRepository.findOneByLogin(SecurityUtils.getCurrentLogin())
            .map(user -> questionVoteRepository.findOneByQuestionIdAndUserId(questionId, user.getId())
                .map(v -> {
                    log.debug("Updating existing vote : {}", v.getId());
                    v.setVote(vote.getVote());
                    questionVoteRepository.save(v);
                    return v;
                })
                .orElseGet(() -> {
                    log.debug("Creating new vote");
                    Question q = questionRepository.findOne(questionId);
                    if (q != null) {
                        vote.setUser(user);
                        vote.setQuestion(q);
                        questionVoteRepository.save(vote);
                        return vote;
                    }
                    return null;
                }));
    }

}
