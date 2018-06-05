package kitaplik.mbo.com.sosyal_kitaplik.entities;

public class Statistic {
    private String user_id, readed_date, book_id;
    private Integer readed_page, like_count, user_page_number, total_citation, read_time;

    public Statistic(){

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReaded_date() {
        return readed_date;
    }

    public void setReaded_date(String readed_date) {
        this.readed_date = readed_date;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public Integer getReaded_page() {
        return readed_page;
    }

    public void setReaded_page(Integer readed_page) {
        this.readed_page = readed_page;
    }

    public Integer getLike_count() {
        return like_count;
    }

    public void setLike_count(Integer like_count) {
        this.like_count = like_count;
    }

    public Integer getUser_page_number() {
        return user_page_number;
    }

    public void setUser_page_number(Integer user_page_number) {
        this.user_page_number = user_page_number;
    }

    public Integer getTotal_citation() {
        return total_citation;
    }

    public void setTotal_citation(Integer total_citation) {
        this.total_citation = total_citation;
    }

    public Integer getRead_time() {
        return read_time;
    }

    public void setRead_time(Integer read_time) {
        this.read_time = read_time;
    }
}
