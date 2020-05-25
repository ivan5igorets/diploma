import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    private final String user = "tiahlxxslhttfg";
    private final String password = "9ecd8d171fcf24a52f655b611473d05c9b37635f9846c56a64e11879786aaea2";
    private final String url = "jdbc:postgresql://ec2-46-137-84-140.eu-west-1.compute.amazonaws.com:5432/de3muvki3lrj42?sslmode=require";

    private Connection connection;
    public static DataBase instance;

    private DataBase() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connect();
        createTablesIfNotExist();
    }

    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTablesIfNotExist() {

        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS users( " +
                        "u_id SERIAL PRIMARY KEY, " +
                        "name TEXT NOT NULL, " +
                        "last_seen timestamp, " +
                        "password TEXT NOT NULL)")) {

            // выполнение запроса
            statement.executeUpdate();
            System.out.println("table users was created");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("table users does not created");
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS messages( " +
                        "id BIGSERIAL PRIMARY KEY, " +
                        "sender integer REFERENCES users(u_id), " +
                        "recipient integer REFERENCES users(u_id), " +
                        "message text NOT NULL, " +
                        "time timestamp, " +
                        "flag boolean)")) {

            // выполнение запроса
            statement.executeUpdate();
            System.out.println("table messages was created");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("table messages does not created");
        }

    }

    private int getId(String user) {
        int id = -1;

        try (PreparedStatement statement = connection.prepareStatement("SELECT u_id FROM users WHERE name = (?)")) {
            statement.setString(1, user);
            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                id = Integer.parseInt(resultSet.getString("u_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return id;
        }

        return id;
    }

    // в полноценном приложении нужно добавить поле в котором хранится инфа о том прочитано сообщение или нет
    // при записи сообщения должно возвращать id и time
    public String[] insertMessage(String sender, String recipient, String msg) {

        String[] arr = null;

        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO messages(sender, recipient, message, time, flag) " +
                        "VALUES ((?), (?), (?), now(), false ) " +
                        "RETURNING id, time")) {

//             нужно реализовать поиск id пользователя
            statement.setInt(1, getId(sender));
            statement.setInt(2, getId(recipient));
            statement.setString(3, msg);

            // выполнение запроса
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                arr = new String[2];
                arr[0] = resultSet.getString(1);
                arr[1] = resultSet.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("---");
            return arr;
        }

        return arr;
    }

    // поле flag не нужно отправлять на клиент, оно устанавливается автоматически
    public ArrayList getListMessages(String user, String time) {
        String[] column = new String[5];
        ArrayList<String[]> arr = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("select " +
                "    messages.id, " +
                "    user_sender.name as name_sender, " +
                "    user_recipient.name as name_recipient, " +
                "    messages.message, " +
                "    messages.time, " +
                "    messages.flag " +
                "from " +
                "    messages inner join " +
                "    users as user_sender on " +
                "            user_sender.u_id=messages.sender inner join " +
                "    users as user_recipient on " +
                "            user_recipient.u_id=messages.recipient " +
                "where " +
                "(messages.sender=(?) or messages.recipient=(?)) AND time > (?) " +
                " ORDER BY time ASC")) {

            statement.setInt(1, getId(user));
            statement.setInt(2, getId(user));
            statement.setTimestamp(3, Timestamp.valueOf(time));

            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {


                arr.add(new String[]
                        {resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5),
                                resultSet.getString(6)});
            }
            return arr;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    // если эта функция будет использоваться в теле другой функиции
    // то удалить вызовы функций connect() и closeConnection()
    private void setLastSeenCurrentTime(String user) {

        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE users SET last_seen = now() " +
                        "WHERE name = (?);")) {

            statement.setString(1, user);
//            statement.setTimestamp(1, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));

            statement.executeUpdate();
            System.out.println("+++");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList friendSearch(String possibleName) {

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT name FROM users " +
                        "WHERE LOWER(name) LIKE LOWER(?);")) { // like lower('%value%')

            statement.setString(1, "%" + possibleName + "%");
            final ResultSet resultSet = statement.executeQuery();

            ArrayList<String> arrayList = new ArrayList<>();
            while (resultSet.next()) {
                arrayList.add(resultSet.getString(1));
                System.out.println(resultSet.getString(1));
            }

            return arrayList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setFlagTrueById(int id) {

        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE messages SET flag = true " +
                "WHERE id = (?)")) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // разобраться почему не работает
    public void setFlagTrueByTime(String user1, String user2, String time) {
        System.out.println("time: " + time);
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE messages SET flag = true " +
                        "WHERE time > (?) AND ( sender = (?) AND recipient = (?) )")) {

            statement.setTimestamp(1, Timestamp.valueOf(time));
            statement.setInt(2, getId(user2));
            statement.setInt(3, getId(user1));

            statement.executeUpdate();
            System.out.println("+++");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    // нужно переписать, выдаёт ошибку когда нет такой записи
    public boolean isExist(String name) {
        boolean result = false;

        try (PreparedStatement statement = connection.prepareStatement("SELECT EXISTS( " +
                "SELECT name " +
                "FROM users " +
                "WHERE name = (?) " +
                "LIMIT 1)")) {
            statement.setString(1, name);

            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result = resultSet.getBoolean(1);
            }

            System.out.println(result);

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean newUserRegistration(String login, String password) {

        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO users(name, password) " +
                        "VALUES ((?), (?))")) {

//             нужно реализовать поиск id пользователя

            statement.setString(1, login);
            statement.setString(2, password);

            // выполнение запроса
            final long resultSet = statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public String[] logIn(String login) {
        String[] arr = null;

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT name, password " +
                        "FROM users " +
                        "WHERE name = (?)")) {

//             нужно реализовать поиск id пользователя
            statement.setString(1, login);

            // выполнение запроса
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                arr = new String[2];
                arr[0] = resultSet.getString(1);
                arr[1] = resultSet.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return arr;
        }

        return arr;
    }


    public static void main(String[] args) {

        DataBase app = DataBase.getInstance();
    }
}