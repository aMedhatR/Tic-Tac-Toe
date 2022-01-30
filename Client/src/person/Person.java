package person;

public class Person {
    private static int Id;
    private static String Name;
    private static int Score ;
    private static boolean Status;

    public static void setId(int id) {
        Id = id;
    }

    public static void setName(String name) {
        Name = name;
    }

    public static void setScore(int score) {
        Score = score;
    }

    public static void setStatus(boolean status) {
        Status = status;
    }

    public static int getId() {
        return Id;
    }

    public static String getName() {
        return Name;
    }

    public static int getScore() {
        return Score;
    }

    public static boolean isStatus() {
        return Status;
    }
}
