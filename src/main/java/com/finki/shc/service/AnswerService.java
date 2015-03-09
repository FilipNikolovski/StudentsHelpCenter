package com.finki.shc.service;

import com.finki.shc.domain.Answer;
import com.finki.shc.domain.Question;
import com.finki.shc.repository.AnswerRepository;
import com.finki.shc.repository.QuestionRepository;
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
public class AnswerService {

    private final Logger log = LoggerFactory.getLogger(AnswerService.class);

    @Inject
    private AnswerRepository answerRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private QuestionRepository questionRepository;

    public Optional<Answer> createAnswer(Long questionId, Answer answer) {
        return userRepository.findOneByLogin(SecurityUtils.getCurrentLogin())
            .map(user -> {
                Question q = questionRepository.findOne(questionId);
                if (q != null) {
                    answer.setQuestion(q);
                    answer.setUser(user);
                    answerRepository.save(answer);
                    return answer;
                }
                return null;
            });
    }
}
