package kitaplik.mbo.com.sosyal_kitaplik.entities;

public class LikedBooks {
    private String book_id, liked_date, user_id;

    public LikedBooks(){

    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getLiked_date() {
        return liked_date;
    }

    public void setLiked_date(String liked_date) {
        this.liked_date = liked_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
