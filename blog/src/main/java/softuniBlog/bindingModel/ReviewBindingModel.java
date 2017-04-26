package softuniBlog.bindingModel;

import org.hibernate.validator.constraints.Length;
import org.w3c.dom.ranges.Range;

import javax.validation.constraints.NotNull;

public class ReviewBindingModel {

    @NotNull
    private String title;

    @NotNull
    private int score;

    @NotNull
    private String imageUrl;

    @NotNull
    private String tags;

    @NotNull
    private String content;

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public int getScore() { return score; }

    public void setScore(String score) { this.score = Integer.parseInt(score); }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getTags() { return tags; }

    public void setTags(String tags) { this.tags = tags; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }
}
