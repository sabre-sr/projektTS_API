package ts.projekt.API_Gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Post implements Serializable {
    public User author;
    private int id;
    private String postBody;
    private LocalDate creationDate;
    private Post repliedTo;
    private String filename;

    public Post() {
    }

    public Post(int id) {
        this.id = id;
    }

    public Post(User author, int id, String postBody, LocalDate creationDate) {
        this.author = author;
        this.id = id;
        this.postBody = postBody;
        this.creationDate = creationDate;
    }

    public Post(User author, int id, String postBody, LocalDate creationDate, Post repliedTo) {
        this.author = author;
        this.id = id;
        this.postBody = postBody;
        this.creationDate = creationDate;
        this.repliedTo = repliedTo;
    }

    public Post(User author, String postBody, Post repliedTo) {
        this.author = author;
        this.postBody = postBody;
        this.repliedTo = repliedTo;
    }

    public Post(User author, String postBody) {
        this.author = author;
        this.postBody = postBody;
        this.repliedTo = null;
    }

    public User getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }

    public String getPostBody() {
        return postBody;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Post getRepliedTo() {
        return repliedTo;
    }

    public void setRepliedTo(Post repliedTo) {
        this.repliedTo = repliedTo;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
