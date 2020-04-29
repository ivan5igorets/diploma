import java.io.*;
import java.net.Socket;

public class Connection {

    private final Socket socket;
    private final Thread rxThread;
    private final ClientConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public Connection (ClientConnectionListener eventListener, String ipAddr, int port) throws IOException {
        this(eventListener, new Socket(ipAddr, port));
    }

    public Connection (ClientConnectionListener eventListener, Socket socket) throws IOException {
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
                        String temp = in.readLine();
                        System.out.println("connection: " + temp);
                        operation(temp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
//                    eventListener.onException(TCPConnection.this, e);
                } finally {
//                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        rxThread.start();
    }

    public void operation(String opr) throws IOException {
        switch (opr) {
            case "/NEW_MESSAGE": eventListener.receivingMessage(in.readLine(), in.readLine(), in.readLine(), in.readLine(), in.readLine(), in.readLine());
                break;
            case "/POSSIBLE_FRIENDS": gettingListOfPossibleFriends();
                break;
            case "/RUN_CLIENT": eventListener.runClient();
                break;
            // продумать релизауию (не знаю как сделать)
            case "/SIGN_UP_ERROR": System.out.println("должно нарисовать подсказку на фрэйме и подсветить клавиши");
                break;
        }
    }

//    public synchronized void getFriends() throws IOException {
//        System.out.println("getFriends client");
//
//        eventListener.clearFriendsList();
//
//        String flag = in.readLine();
//        while(!flag.equals("/END")) {
//            eventListener.printFriends(flag);
//            flag = in.readLine();
//        }
//    }

    public synchronized void sendString(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();
        }catch (IOException e) {
            e.printStackTrace();
//            eventListener.onException(TCPConnection.this, e);
//            disconnect();
        }
    }

    public void gettingListOfMessages() {

        String id, sender,recipient, msg, time, flag;
        // данная функция срабатывает только после вызова getListOfMessages()
        try {
            while (true) {
                if (in.readLine().equals("/END")) {
                    break;
                }

                id = in.readLine();
                sender = in.readLine();
                recipient = in.readLine();
                msg = in.readLine();
                time = in.readLine();
                flag = in.readLine();

                // возмжно всё ниже перечисленное можно уместьть в функцию receivingMessage(...);

                // здесь должна быть функция записывающая сообщения в локальную БД(SQLite)
                // также здесь должна быть функция анализиующая полученное сообщение и в зависимости от того менять время
                // в табличке user или создавать новую запись в ней

                System.out.print(id + " | ");
                System.out.print(sender + " | ");
                System.out.print(recipient + " | ");
                System.out.print(msg + " | ");
                System.out.print(time + " | ");
                System.out.println();

                eventListener.receivingMessage(id, sender, recipient, msg, time, flag);

            }

            // функция делающая запрос на вывод отсортированного списка собеседников в локальную БД(sSQLite) и печатающая его

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gettingListOfPossibleFriends() {

        try {
            String name = in.readLine();

            if(name.equals("/END")) {
                eventListener.setStatusFriendSearchFalse();
                return;
            }

            eventListener.clearFriendsList();
            while (!name.equals("/END")) {

                // тут должна быть функция добавляющая имя возможного друга с список который будет напечатан;
                eventListener.printPossibleFriend(name);

                name = in.readLine();
            }

            // напечатать список если он не пустой;

        } catch (IOException e) {
            e.printStackTrace();
        }



    }






}
