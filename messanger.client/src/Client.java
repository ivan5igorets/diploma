import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Client extends JFrame implements ClientConnectionListener {

    private static final String IP_ADDR = "127.0.0.1";
    private static final int PORT = 8189;

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client("Пятигорец Иван");
            }
        });
    }

    private final JTextArea log = new JTextArea();
    private final JScrollPane scrLog = new JScrollPane(log);
    private JLabel lblNickname;
    private JLabel lblinterlocutor;
    private final JTextField fieldInput = new JTextField();
    private final JTextField fieldSearch = new JTextField();

    // панель собственного имени и имени собеседника
    private final JPanel upPanel = new JPanel();

    // panel LEFT/friends
    private final JPanel leftPanel = new JPanel();

    private final DefaultListModel dfm = new DefaultListModel();
    private final JList listFriends = new JList(dfm);
    private final JScrollPane myScrollPaneList = new JScrollPane(listFriends);

    private boolean statusFriendSearch = false;

    private final ClientDB dataBase = ClientDB.getInstance();

    private Connection connection;



    // нужно переписать с учётом изменений "onConnectionReady"
    private Client(String name) {

        try {
            connection = new Connection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataBase.open();


        new Auth(connection);


    }


    // нужно вынести из кнлиента весь код в отдельную функцию
    @Override
    public void runClient(String name) {
//        new Client()
        initClient(name);
        System.out.println("run client");
    }

    private void initClient(String name) {
        setTitle(name);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
//        setAlwaysOnTop(true);

        lblNickname = new JLabel(name);
        lblinterlocutor = new JLabel("Собеседник");


        // верхняя панель
        add(upPanel, BorderLayout.NORTH);
        upPanel.setBackground(Color.blue);

        fieldSearch.setPreferredSize(new Dimension(300,20));
        upPanel.add(fieldSearch, BorderLayout.WEST);
        fieldSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fieldSearch.getText().equals("") || fieldSearch.getText().equals(" ")) return;

                // тут нужны доработаки
                System.out.println("/FRIEND_SEARCH" + "\n" + fieldSearch.getText());
                friendSearch(fieldSearch.getText());

                statusFriendSearch = true;
//                getFriends();
            }
        });

        upPanel.add(lblinterlocutor, BorderLayout.CENTER);
        lblinterlocutor.setForeground(Color.WHITE);


        // централььная панель
        add(scrLog, BorderLayout.CENTER);
        log.setEditable(false);
        log.setLineWrap(true);
        log.setFocusable(false);

        add(fieldInput, BorderLayout.SOUTH);
        fieldInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });


        // левая панель
        add(leftPanel, BorderLayout.WEST);
        leftPanel.setBackground(Color.CYAN);

        myScrollPaneList.setPreferredSize(new Dimension(300,740));
        listFriends.setLayoutOrientation(JList.VERTICAL);
        leftPanel.add(myScrollPaneList, BorderLayout.CENTER);
        listFriends.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1 && statusFriendSearch == false) {
                    int selected = listFriends.locationToIndex(e.getPoint());
                    System.out.println(selected);

                    if(dfm.getElementAt(selected).toString().equals(lblinterlocutor)) return;
                    fieldSearch.setText("");
                    log.setText("");

                    String temp = dfm.getElementAt(selected).toString();
                    if (temp.indexOf("   ") != -1) {
                        temp = temp.substring(0, temp.indexOf("   "));
                        dfm.set(selected, temp);

                        connection.sendString("/SET_FLAG_TRUE_BY_TIME");
                        connection.sendString(getTitle());
                        connection.sendString(temp);

                        String outTime = dataBase.getTimeOfLastWroughtReadMsg(getTitle(),temp);
                        System.out.println(outTime);
                        connection.sendString(outTime);
                    }

                    lblinterlocutor.setText(temp);
                    dataBase.setFlagTrueForMessages(getTitle(), lblinterlocutor.getText());

                    printDialog(lblinterlocutor.getText());

//                    clearFriendsList();
//                    printFriendList();
                } else if(e.getClickCount() == 1 && statusFriendSearch == true) {

                    int selected = listFriends.locationToIndex(e.getPoint());
                    System.out.println(selected);

                    fieldSearch.setText("");
                    log.setText("");

                    lblinterlocutor.setText(dfm.getElementAt(selected).toString());
                    statusFriendSearch = false;

                    clearFriendsList();
                    printFriendList();
                }
            }
        });

        setVisible(true);


        printFriendList();
        // запись подключения

        // получение списка сообщений
        getListOfMessages();
    }

    private synchronized void printMsg(String sender, String recipient, String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if( (sender.equals(lblinterlocutor.getText()) && recipient.equals(lblNickname.getText())) || (recipient.equals(lblinterlocutor.getText()) && sender.equals(lblNickname.getText())) ) {
                    log.append(sender + ": " + msg + "\n");
                    log.setCaretPosition(log.getDocument().getLength());
                }else {
//                    friendListSorting(sender);
                }
            }
        });
    }

    @Override
    public void receivingMessage(String id, String sender, String recipient, String msg, String time, String flag) {
        System.out.println(flag);


        boolean boolFlag;
        if(flag.equals("t") ) {
           boolFlag = true;
        }else {
            boolFlag =false;
        }

        // печатает сообщение если открыта переписка с отправителем
        printMsg(sender, recipient, msg);


        // записывает сообщение в локальную БД с учетом статуса прочитанно не прочитанно
        if(sender.equals(lblinterlocutor.getText())) {
            dataBase.insertMessages(Integer.parseInt(id), sender, recipient, msg, time, true);

            // запрос на изменение статуса сообщения на сервере
            connection.sendString("/SET_FLAG_TRUE_BY_ID");
            connection.sendString(id);
        } else {
            dataBase.insertMessages(Integer.parseInt(id), sender, recipient, msg, time, boolFlag);
        }

        // анализирует сообщение и изменяет или добавляет новую запись в таблице users
        if(sender.equals(lblNickname.getText())) {
            dataBase.changeFriendsList(recipient, time);
        } else {
            dataBase.changeFriendsList(sender, time);
        }

        if(!statusFriendSearch) {
            // очищает список друзей
            clearFriendsList();

            // функция делающая запрос с локальную БД и печатающая список друзей, список отсортированный (сверху новые)
            printFriendList();
        }

    }

    @Override
    public void getListOfMessages() {

        // отправка запроса, на получение списка сообщений
        connection.sendString("/GET_LIST_OF_MESSAGES");

        connection.sendString(lblNickname.getText());

        // запрос и отправление даты и времени последнего сообщения хранящегося на клиенте
        connection.sendString(dataBase.getTimeOfLastMessage());

        System.out.println("/GET_LIST_OF_MESSAGES");
    }

    @Override
    public void friendSearch(String searchName) {
        connection.sendString("/FRIEND_SEARCH");
        connection.sendString(searchName);
    }

    @Override
    public void sendMessage() {
        String msg = fieldInput.getText();
        if(msg.equals("") || lblinterlocutor.getText().equals("")) return;

        connection.sendString("/NEW_MESSAGE");
        connection.sendString(lblNickname.getText());
        connection.sendString(lblinterlocutor.getText());
        connection.sendString(msg);

        fieldInput.setText(null);
    }

    // возможно нужно изменить
    @Override
    public synchronized void printFriendList() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ArrayList arrayList = dataBase.getFriendsList();
                for(int i = 0; i < arrayList.size(); ++i) {
                    String el = arrayList.get(i).toString();
                    int count = dataBase.countOfUnreadMessages(getTitle(), arrayList.get(i).toString());
//                    while (el.length() < 55) {
//                        el = el + " ";
//                    }

                    if(!(count == 0)) {
                        el = el + "   (" + count + ")";
                    }
                    dfm.addElement(el);
//                    dfm.addElement(dataBase.countOfUnreadMessages(getTitle(), arrayList.get(i).toString()) + " " + arrayList.get(i).toString());

                }


            }
        });
    }

    @Override
    public synchronized void clearFriendsList() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Clear friends");
                listFriends.setSelectedIndex(-1);

                if(dfm.size() == 0) return;
                dfm.removeAllElements();
            }
        });
    }

    @Override
    public synchronized void printPossibleFriend(String name) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dfm.addElement(name);
            }
        });
    }

    @Override
    public void setStatusFriendSearchFalse() {
        statusFriendSearch = false;
    }

    private void getFriends() {
        connection.sendString("/GET_FRIENDS");
        connection.sendString(fieldSearch.getText());
    }

    private void printDialog(String name) {

        // нужно изменить функцию
        ArrayList arrayList = dataBase.getDialog(getTitle(), name);

        for(int i = 0; i < arrayList.size(); ++i) {
            String[] arr = (String[]) arrayList.get(i);
            printMsg(arr[0], arr[1], arr[2]);
        }

    }

//    private void friendListSorting(String sender) {
//
//        for(int i = 0; i < dfm.size(); ++i) {
//
//            if(dfm.getElementAt(i).toString().equals(sender)) {
//                String[] arr = new String[dfm.size() -1];
//
//                for(int j = 0; j < i; ++j) {
//                        arr[j] = dfm.getElementAt(j).toString();
//                }
//
//                for(int j = i; j < arr.length; ++j) {
//                    arr[j] = dfm.getElementAt(j+1).toString();
//                }
//
//                clearFriendsList();
//
//                dfm.addElement(sender + "   *");
//                for(int j = 0; j < arr.length; ++j) {
//                    dfm.addElement(arr[j]);
//                }
//
//                return;
//            } else {
//                String[] arr = new String[dfm.size()];
//
//                for(int j = 0; j < arr.length; ++j) {
//                    arr[j] = dfm.getElementAt(j).toString();
//                }
//
//                clearFriendsList();
//                dfm.addElement(sender);
//                for(int j = 0; j < arr.length; ++j) {
//                    dfm.addElement(arr[j]);
//                }
//
//            }
//        }
//
//    }

}
