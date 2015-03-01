package com.finki.shc.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.finki.shc.domain.util.CustomDateTimeDeserializer;
import com.finki.shc.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Answer.
 */
@Entity
@Table(name = "T_ANSWER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Answer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "answer_text")
    private String answerText;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "answer", cascade = CascadeType.ALL)
    private List<AnswerVote> votes = new ArrayList<>();

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "date_posted", nullable = false)
    private DateTime datePosted;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonBackReference
    private Question question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public DateTime getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(DateTime datePosted) {
        this.datePosted = datePosted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<AnswerVote> getVotes() {
        return votes;
    }

    public void setVotes(List<AnswerVote> votes) {
        this.votes = votes;
    }

    public Integer getUpvotes() {
        int totalUpvotes = 0;
        for(AnswerVote v : votes) {
            if(v.getVote() > 0) totalUpvotes++;
        }
        return totalUpvotes;
    }

    public Integer getDownvotes() {
        int totalDownvotes = 0;
        for(AnswerVote v : votes) {
            if(v.getVote() < 0) totalDownvotes++;
        }
        return totalDownvotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Answer answer = (Answer) o;

        if (id != null ? !id.equals(answer.id) : answer.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", answerText='" + answerText + "'" +
                ", datePosted='" + datePosted + "'" +
                '}';
    }
}
