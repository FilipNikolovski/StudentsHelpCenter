package com.finki.shc.service;

import com.finki.shc.domain.Question;
import com.finki.shc.domain.QuestionVote;
import com.finki.shc.repository.QuestionImageRepository;
import com.finki.shc.repository.QuestionRepository;
import com.finki.shc.repository.QuestionVoteRepository;
import com.finki.shc.repository.UserRepository;
import com.finki.shc.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionService.class);

    @Inject
    private ServletContext context;

    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private QuestionVoteRepository questionVoteRepository;

    @Inject
    private QuestionImageRepository questionImageRepository;

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

    public Boolean uploadImages(Long questionId, List<MultipartFile> files) {
        files.stream().filter(MultipartFile::isEmpty).forEach(f -> {
            try {
                String dest = context.getRealPath("") + File.separator + "assets/images/question-images/" + questionId;
                log.debug("DEST: {}", dest);
                BufferedImage src = ImageIO.read(new ByteArrayInputStream(f.getBytes()));
                File destination = new File(dest + f.getName());
                ImageIO.write(src, "png", destination);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return true;
    }

}
