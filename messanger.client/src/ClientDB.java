import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ClientDB {

    final String DB_CLIENT = "C:/Users/ivan5/IdeaProjects/Messanger+DB/messanger.client/db/client.db";

    // sqlite Connection
    Connection connection = null;

    private ClientDB() {
        open();
    }

    private static ClientDB instance;

    public static ClientDB getInstance() {
        if (instance == null) {
            instance = new ClientDB();
        }
        return instance;
    }

    public boolean open() {

        try {
            // создаём одну БД, конектимся к ней
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_CLIENT);

            // создаём таблицы: users, messages, account
            createTableAccount();
            createTableUsers();
            createTableMessages();

            System.out.println("Connected to local DB");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("don`t open");
            return false;
        }
    }

    private void createTableAccount() {
        try {
            Statement statement = connection.createStatement();

            String query = "CREATE TABLE IF NOT EXISTS account( " +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "name TEXT KEY NOT NULL)";

            statement.execute(query);
            System.out.println("table users was created");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void createTableUsers() {
        try {
            Statement statement = connection.createStatement();

            String query = "CREATE TABLE IF NOT EXISTS users( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT KEY NOT NULL,  " +
                    "last_msg timestamp)";

            statement.execute(query);
            System.out.println("table users was created");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void createTableMessages() {
        try {
            Statement statement = connection.createStatement();

            String query = "CREATE TABLE IF NOT EXISTS messages( " +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "sender TEXT NOT NULL, " +
                    "recipient TEXT NOT NULL, " +
                    "message TEXT NOT NULL, " +
                    "time timestamp, " +
                    "flag boolean) ";

            statement.execute(query);
            System.out.println("table messages was created");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addUsers(String name, String last_msg) {

        try {

            String query = "INSERT INTO users(name, last_msg)" +
                    "VALUES ('" + name + "', '" + last_msg + "')";

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            System.out.println("users was added");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Rows dont  added");
        }
    }

    public void selectAllUsers() {
        try {
            Statement statement = connection.createStatement();

            String query =
                    "SELECT id, name, last_msg " +
                            "FROM users;";

            ResultSet resultSet = statement.executeQuery(query);
            int id;
            String name;
            Timestamp last_msg;

            while (resultSet.next()) {

                id = Integer.parseInt(resultSet.getString("id"));
                name = resultSet.getString("name");
                last_msg = resultSet.getTimestamp(3);

                System.out.print(id + " | ");
                System.out.print(name + " | ");
                System.out.print(last_msg + " | ");
                System.out.println();
            }



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
        }

    }

    public void insertMessages(int id, String sender, String recipient, String message, String time, boolean flag) {

        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO messages(id, sender, recipient, message, time, flag) " +
                    "VALUES ((?), (?), (?), (?), (?), (?))")) {

            statement.setInt(1, id);
            statement.setString(2, sender);
            statement.setString(3, recipient);
            statement.setString(4, message);
            statement.setString(5, time);
            statement.setBoolean(6, flag);

            statement.executeUpdate();

            System.out.println("msg added");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("msg dont  added");
        }
    }

    public void selectAllMessages() {
        try {
            Statement statement = connection.createStatement();

            String query =
                    "SELECT * " +
                            "FROM messages;";

            ResultSet resultSet = statement.executeQuery(query);
            int id;
            String sender,
                    recipient,
                    message;
            Timestamp last_msg;
            boolean flag;

            while (resultSet.next()) {

                id = Integer.parseInt(resultSet.getString(1));
                sender = resultSet.getString(2);
                recipient = resultSet.getString(3);
                message = resultSet.getString(4);
                last_msg = resultSet.getTimestamp(5);
                flag = resultSet.getBoolean(6);

                System.out.print(id + " | ");
                System.out.print(sender + " | ");
                System.out.print(recipient + " | ");
                System.out.print(message + " | ");
                System.out.print(last_msg + " | ");
                System.out.println(flag);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
        }

    }

    public void changeFriendsList(String name, String time) {

        try {
            Statement statement = connection.createStatement();

            String query = "SELECT EXISTS( " +
                    "    SELECT * " +
                    "    FROM users " +
                    "    WHERE name = '" + name + "' " +
                    "    LIMIT 1)";

            ResultSet resultSet = statement.executeQuery(query);
            int exist;

            resultSet.next();
            if(resultSet.getInt(1) == 1) {
                // нужно изменить запить
                System.out.println("Существует");

                query = "UPDATE users " +
                        "SET last_msg = '" + time + "' " +
                        "WHERE name = '" + name + "'";

            }else {
                // нужно создать запись
                System.out.println("не существует");

                query = "INSERT INTO users (name, last_msg) " +
                        "VALUES ('" + name + "', '" + time + "')";

            }
            statement.executeUpdate(query);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String getTimeOfLastMessage() {
        String last_msg = null;
        try {
            Statement statement = connection.createStatement();

            String query = "SELECT EXISTS( " +
                    "    SELECT * " +
                    "    FROM users " +
                    "    LIMIT 1)";

            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();
            if (resultSet.getInt(1) == 1) {
                query = "SELECT last_msg FROM users " +
                            "WHERE last_msg = (SELECT MAX(last_msg) FROM users)";

                resultSet = statement.executeQuery(query);
                last_msg = resultSet.getString(1);
            } else {
                last_msg = "2020-01-01 00:00:00.0000";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            return  last_msg;
        }

    }

    public ArrayList getFriendsList() {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();

            String query =
                    "SELECT name FROM users " +
                            "ORDER BY last_msg DESC";

            ResultSet resultSet = statement.executeQuery(query);
            String name;


            while (resultSet.next()) {
                name = resultSet.getString(1);
                arrayList.add(name);
                System.out.println(name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            return arrayList;
        }

    }

    public ArrayList getDialog(String name1, String name2) {

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT sender, recipient, message FROM messages " +
                        "WHERE (sender = (?) AND recipient = (?)) " +
                        "OR (sender = (?) AND recipient = (?))")) {

            statement.setString(1, name1);
            statement.setString(2, name2);
            statement.setString(3, name2);
            statement.setString(4, name1);

            final ResultSet resultSet = statement.executeQuery();

            ArrayList<String[]> arrayList = new ArrayList<>();
            while (resultSet.next()) {
                arrayList.add(new String[]
                        {resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3)});
            }
            return arrayList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // для авторизации
    public String checkAuth() {
        try {
            Statement statement = connection.createStatement();

            String query =
                    "SELECT name " +
                            "FROM account;";

            ResultSet resultSet = statement.executeQuery(query);
            String name = null;

            while (resultSet.next()) {
                name = resultSet.getString(1);
            }
            return name;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
        }
    }

    public void logIn(String name) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO account(id, name) " +
                        "VALUES ((?), (?))")) {

            statement.setInt(1, 1);
            statement.setString(2, name);

            statement.executeUpdate();

            System.out.println("account added");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("account dont  added");
        }
    }

    public void logOut() {

        try (PreparedStatement statement = connection.prepareStatement(
                " DROP TABLE IF EXISTS users")) {

            statement.executeUpdate();
            System.out.println("logOut +");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("logOut -");
        }

        try (PreparedStatement statement = connection.prepareStatement(
                " DROP TABLE IF EXISTS messages")) {

            statement.executeUpdate();
            System.out.println("logOut +");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("logOut -");
        }

        try (PreparedStatement statement = connection.prepareStatement(
                " DROP TABLE IF EXISTS account")) {

            statement.executeUpdate();
            System.out.println("logOut +");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("logOut -");
        }
    }





    // вызывать когда открывается переписка
    public String getTimeOfLastWroughtReadMsg(String accountName, String interlocutor) {


        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT time FROM messages " +
                        "WHERE time = " +
                        "(SELECT MAX(time) FROM messages " +
                        "WHERE flag = TRUE AND (sender = (?) AND recipient = (?)) )")) {

            statement.setString(1, interlocutor);
            statement.setString(2, accountName);


            ResultSet resultSet = statement.executeQuery();


            if( resultSet.next()) {
                return resultSet.getString(1);
            } else {
                return "2020-01-01 00:00:00";

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }

    }

    // вызывать когда выводится список друззей
    public int countOfUnreadMessages(String accountName, String interlocutor) {

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(id) FROM messages " +
                        "WHERE sender = (?) AND recipient = (?) AND flag = false ")) {

            statement.setString(1, interlocutor);
            statement.setString(2, accountName);

            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            return resultSet.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("msg dont  added");
            return -1;
        }

    }

    // вызывать когда открывается переписка
    public void setFlagTrueForMessages(String accountName, String interlocutor) {

        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE messages SET flag = true " +
                        "WHERE sender = (?) AND recipient = (?) AND flag = false")) {

            statement.setString(1, interlocutor);
            statement.setString(2, accountName);

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("msg dont  added");
        }

    }


//    public void close() {
//        try {
//            connection.close();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }


    public static void main(String[] args) {

        ClientDB dataBase = ClientDB.getInstance();


        dataBase.logOut();
//
//        System.out.println(dataBase.checkAuth());
//
//        dataBase.logIn("Саня_гатило");
//
//        System.out.println(dataBase.checkAuth());


//        System.out.println(dataBase.countOfUnreadMessages( "Пятигорец Алексей", "Пятигорец Иван"));


//        dataBase.addUsers("Щитинкин Доместос", new Timestamp(System.currentTimeMillis()).toString());

//        ClientDB.getInstance().insertMessages(1, "asdg", "szgfdh", "asdfghjklhgfds0", new Timestamp(System.currentTimeMillis()).toString(), false);
//        dataBase.changeFriendsList("Аллочка", "2020-03-25 13:54:56.158");

//        long a = new Date().getTime();

//        ArrayList<String[]> arrayList = dataBase.getDialog("Пятигорец Иван", "Пятигорец Алексей");
//
//        for (int i = 0; i < arrayList.size(); ++i ) {
//            String[] arr = arrayList.get(i);
//
//            System.out.print(arr[0] + " | ");
//            System.out.print(arr[1] + " | ");
//            System.out.println(arr[2] + " | ");
//        }
//
//
//
//        dataBase.selectAllUsers();
//
//        dataBase.selectAllMessages();
//
//        System.out.println(dataBase.getTimeOfLastWroughtReadMsg("Пятигорец Иван", "Пятигорец Алексей"));

//
//        long b = new Date().getTime();
//
//
//        System.out.println("запись и запрос в бд " + (b - a));
////
////
////
////        System.out.println(dataBase.getTimeOfLastMessage());
////
////        dataBase.getFriendsList();



//        dataBase.openUsers();
//        dataBase.createTableUsers();

    }
}
