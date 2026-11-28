package model;

public class Pengguna extends Akun {
    private String password;

    public Pengguna(String idUser, String username, String password) {
        super(idUser, username);
        this.password = password;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}