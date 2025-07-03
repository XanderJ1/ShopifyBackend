package shopify.Data.DTOs;

import shopify.Data.Models.User;

public class Frontend {

    private String role;
    private String token;

    public Frontend(String user, String token) {
        this.role = user;
        this.token = token;
    }

    public String  getRole() {
        return role;
    }

    public void getRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
