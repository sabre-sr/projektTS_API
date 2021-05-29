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


    public Post() {
    }

    public Post(User author, int id, String postBody, LocalDate creationDate) {
        this.author = author;
        this.id = id;
        this.postBody = postBody;
        this.creationDate = creationDate;
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
}
