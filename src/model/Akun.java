package model;

public class Akun {
    protected String idUser;
    protected String username;

    public Akun(String idUser, String username) {
        this.idUser = idUser;
        this.username = username;
    }

    public String getIdUser() { return idUser; }
    public void setIdUser(String idUser) { this.idUser = idUser; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}