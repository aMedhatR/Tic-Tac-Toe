package handlePlayerDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class PlayerDAO {

    public Connection c;
    public PreparedStatement stmt;

    public void PlayerDAO() throws Exception {Connect();}


    private void Connect() throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tictactoe",
                    "postgres", "1571997n");
            System.out.println("connected");
        } catch (Exception e) {
            System.out.println("javadb.JavaDB"+e);
        }

    }


    private static final String SQL_CREATE = "CREATE TABLE players"
            + "("
            + " id serial,"
            + " name varchar(100) unique NOT NULL,"
            + " password varchar(15) NOT NULL,"
            + " email varchar(100) unique NOT NULL,"
            + " score integer,"
            + " PRIMARY KEY (id)"
            + ")";

    public void CreateDBT() throws SQLException {
        try {
//            c.setAutoCommit(false);
            stmt = c.prepareStatement(SQL_CREATE);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean CreatePlayer(Player player) throws SQLException {

        try {
//            c.setAutoCommit(false);
            stmt = c.prepareStatement("INSERT INTO Players (name , password , email , score) VALUES (? , ? , ? , ?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.setString(1, player.getName());
            stmt.setString(2, player.getPassword());
            stmt.setString(3, player.getEmail());
            stmt.setInt(4, player.getScore());
            stmt.executeUpdate();

            //stmt.close();
//            c.commit();
//            c.close();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            System.out.println("code: "+e.getErrorCode());
            System.out.println("message: "+ e.getMessage());

            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    Vector<Player> v = new Vector<Player>();
    Player player;

    public Vector<Player> getPlayer(String name, String password) {
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM players WHERE name=? AND password=?");
            stmt.setString(1, name);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            if (rs.getString("name").equals(name) && rs.getString("password").equals(password)) {

                Player player = new Player(
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getInt("score"));
                //v.add(player);
                //rs.close();
                //stmt.close();
                //c.close();
                System.out.println(rs.getString("name"));

            }
        } catch (SQLException ex) {
//            Logger.getLogger(PlayerModel.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
        catch (Exception exc)
        {
            System.out.println(exc);
        }
        System.out.println("signed in successfully ! yay!");
        return v;

    }

//    public static void main(String[] args) throws SQLException, Exception {
//        new ServerSocket();
//        Player player = new Player("nora", "234", "alaa", 22);
//        PlayerDAO tic = new PlayerDAO();
//        tic.Connect();
//        //tic.CreateDBT();
//        tic.getPlayer("nora", "234");
//        //tic.CreatePlayer(player);
//    }
}
