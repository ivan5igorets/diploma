import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class DataBase {

    private final String user = "postgres";
    private final String password = "12345";
    private final String url = "jdbc:postgresql://localhost:5432/testmessanger";
    private Connection connection;
    public static DataBase instance;

    private DataBase() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    // возвращвет все сообщения переписки указанных пользователей
    private void getDialog(String interlocutor1, String interlocutor2) {

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT users.name, messages.message, messages.time " +
                        "FROM users, messages " +
                        "WHERE users.u_id = messages.sender AND (messages.sender = (?) AND messages.recipient = (?) OR messages.sender = (?) AND messages.recipient = (?)) " +
                        "ORDER BY time ASC")) {

            statement.setInt(1, getId(interlocutor1));
            statement.setInt(2, getId(interlocutor2));
            statement.setInt(3, getId(interlocutor2));
            statement.setInt(4, getId(interlocutor1));
            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.print(resultSet.getString(1) + "   ");
                System.out.print(resultSet.getString(2) + "   ");
                System.out.println(resultSet.getTimestamp(3) + "   ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // возвращает список собеседников
//    private void getFriends(String user) {
//        connect();
//
//        try (PreparedStatement statement = connection.prepareStatement(
//                "SELECT users.name " +
//                        "FROM users, (SELECT (messages.sender) as friends FROM messages WHERE messages.recipient = (?) " +
//                        "UNION " +
//                        "SELECT (messages.recipient) as friends FROM messages WHERE messages.sender = (?))AS friends " +
//                        "WHERE users.u_id = friends ")) {
//
//            statement.setInt(1, getId(user));
//            statement.setInt(2, getId(user));
//
//            final ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                System.out.println(resultSet.getString(1) + "   ");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        closeConnection();
//
//    }

    // возвращает список всех пользователей
    private void getAllUsers() {

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users")) {
            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString(2) + "   ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private Timestamp getLastSeenTime(String name) {

        try (PreparedStatement statement = connection.prepareStatement("SELECT last_seen FROM users " +
                "WHERE u_id = (?)")) {

            statement.setInt(1, getId(name));

            final ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            System.out.print(resultSet.getString(1) + " | ");
            return Timestamp.valueOf(resultSet.getString(1));

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getRsCount(ResultSet rs) {
        int rsCount = 0;

        try {
            while (rs.next()) {
                ++rsCount;
            }

            rs.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsCount;
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
//    public void getFriends(String name) {
//
//        // функция делающаяя запрос в БД, результатом которого является список пользователей, чьи имемена содержат
//        // полученную подстроку
//            ArrayList arrayList = friendSearch(name);
//
//            for (int i = 0; i < arrayList.size(); ++i) {
//                System.out.println(arrayList.get(i) +  " | ");
//                // здесь должна быть отправка на клиент
//            }
//
//    }


    public static void main(String[] args) {

        DataBase app = DataBase.getInstance();



//        String[] arr = app.logIn("Пятигорец Иван");
//        System.out.println(arr[0]);
//        System.out.println(arr[1]);

//        System.out.println(app.isExist("Пятигорец Иван"));

//        System.out.println(app.newUserRegistration("kaban", "123456qwerty"));

//        app.setFlagTrueByTime("Пятигорец Иван", "Пятигорец Алексей", "2020-04-11 19:19:36.36498");

//        ArrayList<String> arrayList = new ArrayList();
//
//        arrayList.add("01");
//        arrayList.add("0wefd");
//        arrayList.add("sfd");
//        String temp;

//        for(int u = 0; u < arrayList.size(); ++u) {
//            temp = arrayList.get(u);
//
//            System.out.println(temp);
//        }

//        app.friendSearch("ан");
//        app.getAllUsers();

//        app.friendSearch("Ал");


//        DataBase bf = DataBase.getInstance();

//
//        ArrayList<String[]> arrayList = app.getListMessages("Пятигорец Иван", "2020-01-01 17:49:03.186956");
//        String[] arr = null;
//
//        System.out.println("-----------------------------------------");
//
//        for (int i = 0; i < arrayList.size(); ++i) {
//
//            arr = arrayList.get(i);
//            System.out.print(arr[0] + " | ");
//            System.out.print(arr[1] + " | ");
//            System.out.print(arr[2] + " | ");
//            System.out.print(arr[3] + " | ");
//            System.out.print(arr[4] + " | ");
//            System.out.println();
//
//        }


            // При отправке сообщения поле last_seen в таблице users следует обновить текущими [датой и временем]
//        String[] arr = app.insertMessage( "Пятигорец Иван", "Пятигорец Алексей", "шото тут не как всё просто как изначально казалось.");
//        System.out.println(arr[0]);
//        System.out.println(arr[1]);


    }
}