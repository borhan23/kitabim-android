package kitaplik.mbo.com.sosyal_kitaplik.entities;

public class RatesLikes {

    private String user_id;
    private Double user_rate;

    public RatesLikes(){

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Double getUser_rate() {
        return user_rate;
    }

    public void setUser_rate(Double user_rate) {
        this.user_rate = user_rate;
    }
}
