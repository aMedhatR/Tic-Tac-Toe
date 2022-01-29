package player;

public class Player {

    private int id;
    private String name;
    private String password;
    private String email;
    private int score;

    //used in retrive in signin
    public Player(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
//        this.score = score;
    }

    public Player(String name, String password, String email, int score) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.score = score;
    }


    //used to retrive data from db to leaderboard
    public Player(int id, String name, String email, int score) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.score = score;
    }

    Player() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
