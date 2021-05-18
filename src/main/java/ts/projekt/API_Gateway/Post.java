package ts.projekt.API_Gateway;

import java.util.Date;

public class Post {
    public User author;
    private int id;
    private String postBody;
    private Date creationDate;

    public Post(User author, int id, String postBody, Date creationDate) {
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

    public Date getCreationDate() {
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

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
