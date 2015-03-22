package com.finki.shc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.finki.shc.domain.util.CustomDateTimeDeserializer;
import com.finki.shc.domain.util.CustomDateTimeSerializer;
import com.finki.shc.security.SecurityUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Question.
 */
@Entity
@Table(name = "T_QUESTION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Question implements Serializable {

    @Transient
    private final Logger log = LoggerFactory.getLogger(Question.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "date_posted", nullable = false)
    private DateTime datePosted;

    @Column(name = "solved")
    private Boolean solved;

    @ManyToOne
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<QuestionImage> images = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<QuestionVote> votes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "T_QUESTION_TAG",
        joinColumns = {@JoinColumn(name = "question_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tag> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(DateTime datePosted) {
        this.datePosted = datePosted;
    }

    public Boolean getSolved() {
        return solved;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public List<QuestionImage> getImages() {
        return images;
    }

    public void setImages(List<QuestionImage> images) {
        this.images = images;
    }

    public List<QuestionVote> getVotes() {
        return votes;
    }

    public void setVotes(List<QuestionVote> votes) {
        this.votes = votes;
    }

    public Long getUpvotes() {
        return votes.stream().filter(v -> v.getVote() > 0).count();
    }

    public Long getDownvotes() {
        return votes.stream().filter(v -> v.getVote() < 0).count();
    }

    public Integer getUserVoted() {
        if (SecurityUtils.isAuthenticated()) {
            for (QuestionVote v : votes) {
                if (v.getUser().getLogin().equals(SecurityUtils.getCurrentLogin())) {
                    return v.getVote();
                }
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Question question = (Question) o;

        return !(id != null ? !id.equals(question.id) : question.id != null);

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", datePosted='" + datePosted + "'" +
            ", solved='" + solved + "'" +
            ", answers='" + answers.toString() + "'" +
            ", tags='" + tags.toString() + "'" +
            '}';
    }
}
