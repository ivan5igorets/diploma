import java.util.ArrayList;

public interface TCPConnectionListener {
    // ++
    void onConnectionReady(String login, TCPConnection tcpConnection);

    // +-
    // данная функция принимает сообщение, записывает его в БД и рассылает его на клиенты
    void addNewMessage(String sender, String recipient, String msg);

    // +-
    // Отправляет на клиент список сообщений которые были отправлены позже получего, времени, последнего сообщения хранящегося на клиенте
    void listOfMessages(String user, String time);

    // непосредственно отправка сообщения
    void sendingMessage(String sendTo, String id, String sender, String recipient, String msg, String time, String flag);

    // поиск друззей реализован в классе соединения
    void onDisconnect(TCPConnection tcpConnection, String name);
    void onException(TCPConnection tcpConnection, Exception e);

    void setFlagTrueById(String id);
    void setFlagTrueByTime(String user1, String user2, String time);

    boolean signUp(String login, String password);

    boolean logIn(String login, String password);

//    void searchFriends(String recipient, String desired);
}
