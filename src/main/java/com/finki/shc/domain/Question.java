package com.finki.shc.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
 * A Question.
 */
@Entity
@Table(name = "T_QUESTION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Question implements Serializable {

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question", cascade = CascadeType.ALL)
    private List<QuestionImage> images = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question", cascade = CascadeType.ALL)
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

    public Integer getTotalAnswers() { return answers.size(); }

    public Integer getUpvotes() {
        int totalUpvotes = 0;
        for(QuestionVote v : votes) {
            if(v.getVote() > 0) totalUpvotes++;
        }
        return totalUpvotes;
    }

    public Integer getDownvotes() {
        int totalDownvotes = 0;
        for(QuestionVote v : votes) {
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

        Question question = (Question) o;

        if (id != null ? !id.equals(question.id) : question.id != null) return false;

        return true;
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
