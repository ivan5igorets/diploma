import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class TCPConnection {

    private final Socket socket;
    private final Thread rxThread;
    private final TCPConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    // нужно хранить имя, это ключ к значению в хэш-таблице
    // для быстрого удаления
    private String name = null;


    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        // time 32:51
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!rxThread.isInterrupted()) {
                        operation(in.readLine());
                    }
                } catch (IOException e) {
                    System.out.println(e);
//                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this, name);
                }
            }
        });
        rxThread.start();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void operation(String opr) throws IOException {
        switch (opr) {

            // написать правильный вызов onConnectionReady
            case "/ON_CONNECTION": name = in.readLine();
                eventListener.onConnectionReady(name, TCPConnection.this);
                break;
            case "/NEW_MESSAGE": eventListener.addNewMessage(in.readLine(), in.readLine(), in.readLine());
                break;
            case "/GET_LIST_OF_MESSAGES": eventListener.listOfMessages(in.readLine(), in.readLine());
                break;
            case "/FRIEND_SEARCH": getFriends(in.readLine());
                break;
            case "/SET_FLAG_TRUE_BY_ID": eventListener.setFlagTrueById(in.readLine());
                break;
            case "/SET_FLAG_TRUE_BY_TIME": eventListener.setFlagTrueByTime(in.readLine(), in.readLine(), in.readLine());
                break;
            case "/SIGN_UP": signUp(in.readLine(), in.readLine());
                break;
            case "/LOG_IN": logIn(in.readLine(), in.readLine());
                break;
            case "/LOG_OUT": eventListener.onDisconnect(TCPConnection.this, name);
                break;
        }
    }


    public void getFriends(String name) {

        // функция делающаяя запрос в БД, результатом которого является список пользователей, чьи имемена содержат
        // полученную подстроку
        DataBase dataBase = DataBase.getInstance();
        ArrayList arrayList = dataBase.friendSearch(name);

            sendString("/POSSIBLE_FRIENDS");

        for (int i = 0; i < arrayList.size(); ++i) {
            System.out.println(arrayList.get(i) +  " | ");

            // здесь должна быть отправка на клиент
            sendString(arrayList.get(i).toString());
        }

        sendString("/END");

        System.out.println("TCPConnection.getFriends()");


    }

    public synchronized void sendString(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();
        }catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
//            disconnect();
        }
    }

    // протестировать
    private void signUp(String login, String password) {
        if (eventListener.signUp(login, password)) {
        sendString("/RUN_CLIENT");
        } else {
        sendString("/SIGN_UP_ERROR");
        }
    }

    //
    private void logIn(String login, String password) {
        if (eventListener.logIn(login, password)) {
            sendString("/RUN_CLIENT");
        } else {
            sendString("/LOG_IN_ERROR");
        }
    }

}
