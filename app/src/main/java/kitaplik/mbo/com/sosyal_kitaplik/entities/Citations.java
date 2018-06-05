package kitaplik.mbo.com.sosyal_kitaplik.entities;

/**
 * Created by MBORHAN on 11.03.2018.
 */

public class Citations {
    private String citation_id, book_id, content, user_id, citation_Date;
    private Integer citation_page;

    public Citations(){

    }

    public String getCitation_id() {
        return citation_id;
    }

    public void setCitation_id(String citation_id) {
        this.citation_id = citation_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCitation_Date() {
        return citation_Date;
    }

    public void setCitation_Date(String citation_Date) {
        this.citation_Date = citation_Date;
    }

    public Integer getCitation_page() {
        return citation_page;
    }

    public void setCitation_page(Integer citation_page) {
        this.citation_page = citation_page;
    }
}
