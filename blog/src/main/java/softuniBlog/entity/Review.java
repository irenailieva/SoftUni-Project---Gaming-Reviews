package softuniBlog.entity;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reviews")
public class Review {

    private int id;

    private String title;

    private int score;

    private String imageUrl;

    private String content;

    private Date date;

    private User author;

    private int upvoteCount;

    private String tags;

    @Transient
    public String getSummary() {
        if (this.getContent().length() > 140) {
            return this.getContent().substring(0, 140).trim() + "...";
        }
        else {
            return this.getContent();
        }
    }

    public Review(String title, int score, String imageUrl, String content, User author, int upvoteCount, String tags) {

        this.title = title;
        this.score = score;
        this.imageUrl = imageUrl;
        this.content = content;
        this.author = author;
        this.date = new Date();
        this.upvoteCount = upvoteCount;
        this.tags = tags;
    }

    public Review() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "score", nullable = false)
    @Range(min = 1, max = 10)
    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    @URL
    @Column(name = "imageUrl", nullable = false)
    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Column(name = "content", nullable = false, columnDefinition = "text")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne
    @JoinColumn(nullable = false, name = "authorId")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Column(name = "upvoteCount")
    public int getUpvoteCount() { return upvoteCount; }

    public void setUpvoteCount(int upvoteCount) { this.upvoteCount = upvoteCount; }

    @Column(name = "tags", nullable = false)
    public String getTags() { return tags; }

    public void setTags(String tags) { this.tags = tags; }
}

