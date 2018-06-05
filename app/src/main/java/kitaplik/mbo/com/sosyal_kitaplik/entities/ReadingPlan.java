package kitaplik.mbo.com.sosyal_kitaplik.entities;

public class ReadingPlan {

    private String user_id, book_id, planning_time, planning_date, push_id;
    private Integer planning_page;

    public ReadingPlan(){

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

    public String getPlanning_time() {
        return planning_time;
    }

    public void setPlanning_time(String planning_time) {
        this.planning_time = planning_time;
    }

    public String getPlanning_date() {
        return planning_date;
    }

    public void setPlanning_date(String planning_date) {
        this.planning_date = planning_date;
    }

    public Integer getPlanning_page() {
        return planning_page;
    }

    public void setPlanning_page(Integer planning_page) {
        this.planning_page = planning_page;
    }

    public String getPush_id() {
        return push_id;
    }

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }
}
