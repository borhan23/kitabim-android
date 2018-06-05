package kitaplik.mbo.com.sosyal_kitaplik.entities;

public class BookStatistic {
    private String book_id;
    private Integer total_citation, total_like, total_rate_number;

    public BookStatistic(){

    }

    public Integer getTotal_citation() {
        return total_citation;
    }

    public void setTotal_citation(Integer total_citation) {
        this.total_citation = total_citation;
    }

    public Integer getTotal_like() {
        return total_like;
    }

    public void setTotal_like(Integer total_like) {
        this.total_like = total_like;
    }

    public Integer getTotal_rate_number() {
        return total_rate_number;
    }

    public void setTotal_rate_number(Integer total_rate_number) {
        this.total_rate_number = total_rate_number;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }
}
