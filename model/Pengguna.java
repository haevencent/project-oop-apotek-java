public class Pengguna {
    private String idUser;
    private String username;
    private String password;


    public Pengguna(String idUser, String username, String password) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
    }

  
    public String getIdUser() { return idUser; }
    public void setIdUser(String idUser) { this.idUser = idUser; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
