package kitaplik.mbo.com.sosyal_kitaplik.entities;

public class Library {
    private String user_id, book_id, started_date;
    private Integer readed_page, total_reading_time;

    public Library(){

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getStarted_date() {
        return started_date;
    }

    public void setStarted_date(String started_date) {
        this.started_date = started_date;
    }

    public Integer getReaded_page() {
        return readed_page;
    }

    public void setReaded_page(Integer readed_page) {
        this.readed_page = readed_page;
    }

    public Integer getTotal_reading_time() {
        return total_reading_time;
    }

    public void setTotal_reading_time(Integer total_reading_time) {
        this.total_reading_time = total_reading_time;
    }
}
