package kitaplik.mbo.com.sosyal_kitaplik.entities;

/**
 * Created by MBORHAN on 27.02.2018.
 */

 public class User {
    private String user_id,email,full_name,password,user_image,user_name;
    private boolean role;

    public User(){

    }

    public User(String user_id, String email, String full_name, String password, String user_comment, String user_image, String user_name, boolean role){
        this.user_id = user_id;
        this.email = email;
        this.full_name = full_name;
        this.password = password;
        this.user_image = user_image;
        this.user_name = user_name;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public boolean isRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
