public class Pengguna {
    private String idUser;
    private String username;
    private String password;
    private boolean isLogin;

    public Pengguna(String idUser, String username, String password) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.isLogin = false;
    }

    public boolean login(String inputUsername, String inputPassword) {
        if (this.username.equals(inputUsername) && this.password.equals(inputPassword)) {
            this.isLogin = true;
            System.out.println("Login berhasil! Selamat datang, " + this.username);
            return true;
        } else {
            System.out.println("Login gagal! Username atau password salah.");
            return false;
        }
    }

    public void logout() {
        this.isLogin = false;
        System.out.println(this.username + " telah logout.");
    }
    
    public String getUsername() { return username; }
}
