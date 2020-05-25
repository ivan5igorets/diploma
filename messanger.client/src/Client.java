import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class Client implements ClientConnectionListener {


    private static final String IP_ADDR = "127.0.0.1";
//    private static final String IP_ADDR = "52.19.225.66";
    private static final int PORT = 443;

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client();
            }
        });
    }

    // инициализация компонентов для Авторизации/Регестрации
    JFrame client = new JFrame();
    JFrame auth = new JFrame();
    private JPanel authPanel1;
    private JButton btnLogIn;
    private JTextField tfLogin1;
    private JPasswordField passwordField;

    private JPanel authPanel2;
    private JTextField tfLogin2;
    private JTextField tfPassword1;
    private JTextField tfPassword2;
    private JButton btnSignUp;

    // инициалиация компонентов для Клиента
    private String nameOfUser;

    private final JTextArea log = new JTextArea();
    private final JScrollPane scrLog = new JScrollPane(log);
    private JLabel lblNickname;
    private JLabel lblInterlocutor;
    private final JTextField fieldInput = new JTextField();

    private final JTextField fieldSearch = new JTextField();
    private final String searchHint = "поиск собеседника";

    // панель собственного имени и имени собеседника
    private final JPanel upPanel = new JPanel();
    private final JPanel leftPanel = new JPanel();
    private final JButton btnLogOut = new JButton("выйти");

    private final DefaultListModel dfm = new DefaultListModel();
    private final JList listFriends = new JList(dfm);
    private final JScrollPane myScrollPaneList = new JScrollPane(listFriends);

    private boolean statusFriendSearch = false;

    private final ClientDB dataBase = ClientDB.getInstance();

    private Connection connection;


    private Client() {

        try {
            connection = new Connection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataBase.open();

        boolean bol = dataBase.checkAuth();
        System.out.println(bol);

        initAuth();

        if (bol) {
            nameOfUser = dataBase.getUserName();
            initClient(nameOfUser);
        } else {
            auth.setVisible(true);
        }

    }

    private void initAuth() {

        auth.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        auth.setVisible(true);
        auth.setSize(new Dimension(WIDTH, HEIGHT));
        auth.setLocationRelativeTo(null);
        auth.setResizable(false);
        auth.setVisible(false);

        authPanel1 = new JPanel();
        auth.setTitle("Авторизация");

        auth.getContentPane().add(authPanel1);
        initPanelLogIn(authPanel1, connection);

        authPanel2 = new JPanel();
        initPanelSignUp(authPanel2, connection);

        auth.validate();
        auth.repaint();
    }

    private void initPanelLogIn(JPanel panel, Connection connection) {

        final JLabel lblEnterLogin = new JLabel("Введите логин");
        final JLabel lblEnterPass1 = new JLabel("Введите пароль");
        final JLabel lblPrompt = new JLabel("нет аккаунта?");

        JButton btnBack;

        panel.setLayout(null);
        panel.add(lblEnterLogin);
        lblEnterLogin.setBounds(450, 150, 100, 30);

        panel.add(lblEnterLogin);
        lblEnterLogin.setBounds(450, 150, 100, 30);

        tfLogin1 = new JTextField();
        panel.add(tfLogin1);
        tfLogin1.setBounds(400,185,200,30);

        panel.add(lblEnterPass1);
        lblEnterPass1.setBounds(450, 220, 100, 30);

        passwordField = new JPasswordField();
        panel.add(passwordField);
        passwordField.setBounds(400,250,200,30);

        btnLogIn = new JButton("войти");
        panel.add(btnLogIn);
        btnLogIn.setBounds(400, 290, 200, 30);
        btnLogIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (tfLogin1.getText().equals("") || passwordField.getPassword().equals("") ) {
                    fieldTinting(tfLogin1.getBackground(), tfLogin1);
                    fieldTinting(tfLogin1.getBackground(), passwordField);
                    return;
                }

                if(tfLogin1.getText().length() < 5) {
                    showHind("Количество символов имени должно быть не меньше 5", authPanel1);
                    return;
                }


                // проверяет количество символов в имени
                if ((tfLogin1.getText().length() > 30)) {
                    showHind("Количество символов имени не должно привышать 30", authPanel1);
                    return;
                }

                // проверяет существование подстроки состоящей из пробела " "
                if (tfLogin1.getText().indexOf(" ") != -1) {
                    showHind("Вместо пробела используйте '_'", authPanel1);
                    return;
                }

                if (passwordField.getPassword().length < 6 ) {
                    showHind("Пароль должен составлять не меньше 6 символов", authPanel1);
                    return;
                }


                if (spaceCharacterSearch(passwordField.getPassword())) {
                    showHind("Пробел является запрещённым символом", authPanel1);
                    return;
                }


                // тут должно быть отправление на клиент
                connection.sendString("/LOG_IN");
                nameOfUser = tfLogin1.getText();
                connection.sendString(nameOfUser);

                String password = new String(passwordField.getPassword());



                connection.sendString(password);

                System.out.println(tfLogin1.getText());
                System.out.println(password);

                tfLogin1.setText("");
                passwordField.setText("");

            }
        });


        panel.add(lblPrompt);
        lblPrompt.setBounds(455, 370, 100,30);

        btnBack = new JButton("зарегестрироваться");
        panel.add(btnBack);
        btnBack.setBounds(400,400,200,30);
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auth.getContentPane().removeAll();
                auth.getContentPane().add(authPanel2);
                auth.setTitle("Регистрация");

                auth.validate();
                auth.repaint();
            }
        });

        panel.setVisible(true);

    }

    private void initPanelSignUp(JPanel panel, Connection connection) {

        final JLabel lblEnterLogin = new JLabel("Введите логин");
        final JLabel lblEnterPass1 = new JLabel("Введите пароль");
        final JLabel lblEnterPass2 = new JLabel("Введите пароль повторно");

        JButton btnBack;

        panel.setLayout(null);
        panel.add(lblEnterLogin);
        lblEnterLogin.setBounds(450, 150, 100, 30);

        panel.add(lblEnterLogin);
        lblEnterLogin.setBounds(450, 150, 100, 30);

        tfLogin2 = new JTextField();
        panel.add(tfLogin2);
        tfLogin2.setBounds(400,185,200,30);

        panel.add(lblEnterPass1);
        lblEnterPass1.setBounds(450, 220, 100, 30);

        tfPassword1 = new JTextField();
        panel.add(tfPassword1);
        tfPassword1.setBounds(400,250,200,30);


        panel.add(lblEnterPass2);
        lblEnterPass2.setBounds(420, 285, 160, 30);

        tfPassword2 = new JTextField();
        panel.add(tfPassword2);
        tfPassword2.setBounds(400,315,200,30);


        btnSignUp = new JButton("Регистрация");
        panel.add(btnSignUp);
        btnSignUp.setBounds(400, 350, 200, 30);
        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = tfLogin2.getText();

                if (name.equals("") || tfPassword1.getText().equals("") || tfPassword2.getText().equals("")) {

                    fieldTinting(tfLogin2.getBackground(), tfLogin2);
                    fieldTinting(tfLogin2.getBackground(), tfPassword1);
                    fieldTinting(tfLogin2.getBackground(), tfPassword2);

                    return;
                }

                // удаляет пробелы в начале и в конце строки
                name = name.trim();

                // в случае сработываения ифов должны выскакивать подсказки
                if(name.length() < 5) {
                    showHind("Количество символов имени должно быть не меньше 5", authPanel2);
                    return;
                }


                // проверяет количество символов в имени
                if ((name.length() > 30)) {
                    showHind("Количество символов имени не должно привышать 30", authPanel2);
                    return;
                }

                // проверяет существование подстроки состоящей из пробела " "
                if(name.indexOf(" ") != -1) {
                    showHind("Вместо пробела используйте '_'", authPanel2);
                    return;
                }

                if(tfPassword1.getText().length() < 6) {
                    showHind("Пароль должен составлять не меньше 6 символов", authPanel2);
                    return;
                }


                if (tfPassword1.getText().indexOf(" ") != -1 || tfPassword2.getText().indexOf(" ") != -1) {
                    showHind("Пробел является запрещённым символом", authPanel2);
                    return;
                }

                if (!tfPassword1.getText().equals(tfPassword2.getText())) {
                    showHind("Пароли не совпадают", authPanel2);
                    return;
                }

                // тут должно быть отправление на клиент
                connection.sendString("/SIGN_UP");

                nameOfUser = tfLogin2.getText();
                connection.sendString(nameOfUser);

                connection.sendString(tfPassword1.getText());

                tfLogin2.setText("");
                tfPassword1.setText("");
                tfPassword2.setText("");
            }
        });

        btnBack = new JButton("назад");
        panel.add(btnBack);
        btnBack.setBounds(450,400,100,30);
        btnBack.setToolTipText("вернуться к авторизации");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                auth.getContentPane().removeAll();
                auth.getContentPane().add(authPanel1);
                auth.setTitle("Авторизация");

                tfLogin2.setText("");
                tfPassword1.setText("");
                tfPassword2.setText("");

                auth.validate();
                auth.repaint();
            }
        });

        panel.setVisible(true);
    }

    // подкрашивание текстового поля
    private void fieldTinting(Color background, JTextField textField) {

        if (textField.getText().equals("")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Color naturalTFColor = background;

                    textField.setBackground(Color.RED);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    textField.setBackground(naturalTFColor);
                }
            }).start();
        }
    }

    private void paintOverTheButton(JButton btn) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Color natural = btn.getBackground();

                for (int i = 0; i < 3; ++i) {
                    btn.setBackground(new Color(255, 0, 0));
                    btn.setForeground(Color.WHITE);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    btn.setBackground(natural);
                    btn.setForeground(Color.BLACK);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void showHind(String promptWord, JPanel panel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JLabel lblPrompt = new JLabel(promptWord);
                panel.add(lblPrompt);

                lblPrompt.setBounds(500 - (int) ( promptWord.length()*7.7)/2 , 10, (int) ( promptWord.length()*7.6),30);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                panel.remove(lblPrompt);

                auth.validate();
                auth.repaint();
            }
        }).start();
        return;
    }

    private boolean spaceCharacterSearch(char[] arr) {
        for(int i = 0; i < arr.length; ++i) {
            if(arr[i] == ' ') {
                return true;
            }
        }
        return false;
    }

    // создние клиента
    @Override
    public void onConnection(String name) {
        connection.sendString("/ON_CONNECTION");
        connection.sendString(name);
    }

    @Override
    public synchronized void  logInError() {
        showHind("ошибка авторизации: Неверный логин или пароль", authPanel1);
        paintOverTheButton(btnLogIn);
    }

    @Override
    public synchronized void signUpError() {
        showHind("ошибка регистрации: Данное имя уже занято", authPanel2);
        paintOverTheButton(btnSignUp);
    }

    @Override
    public void runClient() {
        dataBase.logIn(nameOfUser);
        initClient(nameOfUser);

        // пробуем по другому
//        auth.dispose();
        auth.setVisible(false);
        System.out.println("run client");

    }

    private void initClient(String name) {
        client.setTitle(name);
        client.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        client.setSize(WIDTH, HEIGHT);
        client.setLocationRelativeTo(null);

        onConnection(name);

        lblNickname = new JLabel(name);
        lblInterlocutor = new JLabel("      Собеседник");

        // верхняя панель
        client.add(upPanel, BorderLayout.NORTH);
        upPanel.setBackground(Color.blue);
        upPanel.setLayout(new BorderLayout());

        fieldSearch.setPreferredSize(new Dimension(310,20));
        upPanel.add(fieldSearch, BorderLayout.WEST);
        fieldSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fieldSearch.getText().equals("") || fieldSearch.getText().equals(" ")) return;

                // тут нужны доработаки
                System.out.println("/FRIEND_SEARCH" + "\n" + fieldSearch.getText());
                friendSearch(fieldSearch.getText());

                statusFriendSearch = true;
            }
        });

        Color color = fieldSearch.getCaretColor();

        fieldSearch.setForeground(Color.LIGHT_GRAY);
        fieldSearch.setText(searchHint);
        fieldSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                fieldSearch.setForeground(color);
                if (fieldSearch.getText().equals(searchHint)) {
                    fieldSearch.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (fieldSearch.getText().isEmpty()) {
                    fieldSearch.setForeground(Color.LIGHT_GRAY);
                    fieldSearch.setText(searchHint);
                }
            }
        });

        upPanel.add(lblInterlocutor, BorderLayout.CENTER);
        lblInterlocutor.setForeground(Color.WHITE);

        upPanel.add(btnLogOut, BorderLayout.EAST);
        btnLogOut.setBackground(Color.BLUE);
        btnLogOut.setForeground(Color.WHITE);
        btnLogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.setText("");
                clearFriendsList();
                client.dispose();
                lblInterlocutor.setText("");
                dataBase.logOut();

                connection.sendString("/LOG_OUT");

                auth.setVisible(true);
            }
        });

        // централььная панель
        client.add(scrLog, BorderLayout.CENTER);
        log.setEditable(false);
        log.setLineWrap(true);
        log.setFocusable(false);

        fieldInput.requestFocusInWindow();

        client.add(fieldInput, BorderLayout.SOUTH);
        fieldInput.setBackground(new Color(238, 239, 255));
        fieldInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lblInterlocutor.getText() == "      Собеседник") {
                    fieldInput.setText("");
                    return;
                }
                sendMessage();
            }
        });


        // левая панель
        client.add(leftPanel, BorderLayout.WEST);
        leftPanel.setBackground(Color.CYAN);
        leftPanel.setLayout(new BorderLayout());



        myScrollPaneList.setPreferredSize(new Dimension(310,740));
        listFriends.setBackground(new Color(195, 255, 237));
        listFriends.setLayoutOrientation(JList.VERTICAL);
        leftPanel.add(myScrollPaneList, BorderLayout.CENTER);
        listFriends.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1 && statusFriendSearch == false) {
                    int selected = listFriends.locationToIndex(e.getPoint());
                    System.out.println(selected);

                    // проверить этот момент
                    System.out.println(dfm.getElementAt(selected).toString());

                    if(dfm.getElementAt(selected).toString().equals(lblInterlocutor.getText().trim())) return;
                    fieldSearch.setForeground(Color.LIGHT_GRAY);
                    fieldSearch.setText(searchHint);
                    log.setText("");
                    fieldInput.setText("");

                    String temp = dfm.getElementAt(selected).toString();
                    if (temp.indexOf("   ") != -1) {
                        temp = temp.substring(0, temp.indexOf("   "));
                        dfm.set(selected, temp);

                        connection.sendString("/SET_FLAG_TRUE_BY_TIME");
                        connection.sendString(client.getTitle());
                        connection.sendString(temp);

                        String outTime = dataBase.getTimeOfLastWroughtReadMsg(client.getTitle(),temp);
                        System.out.println(outTime);
                        connection.sendString(outTime);
                    }

                    lblInterlocutor.setText("      " + temp);
                    dataBase.setFlagTrueForMessages(client.getTitle(), lblInterlocutor.getText().trim());

                    printDialog(lblInterlocutor.getText().trim());

//                    clearFriendsList();
//                    printFriendList();
                } else if(e.getClickCount() == 1 && statusFriendSearch == true) {

                    int selected = listFriends.locationToIndex(e.getPoint());
                    System.out.println(selected);

                    fieldSearch.setForeground(Color.LIGHT_GRAY);
                    fieldSearch.setText(searchHint);
                    log.setText("");

                    lblInterlocutor.setText("      " + dfm.getElementAt(selected).toString());
                    statusFriendSearch = false;

                    clearFriendsList();
                    printFriendList();
                }
            }
        });

        client.setVisible(true);

        printFriendList();
        // запись подключения

        // получение списка сообщений
        getListOfMessages();
    }

    private synchronized void printMsg(String sender, String recipient, String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if( (sender.equals(lblInterlocutor.getText().trim()) && recipient.equals(lblNickname.getText())) || (recipient.equals(lblInterlocutor.getText().trim()) && sender.equals(lblNickname.getText())) ) {
                    log.append(sender + ": " + msg + "\n");
                    log.setCaretPosition(log.getDocument().getLength());
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
        if(sender.equals(lblInterlocutor.getText().trim())) {
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
        msg = msg.trim();
        if(msg.equals("") || lblInterlocutor.getText().trim().equals("")) return;

        connection.sendString("/NEW_MESSAGE");
        connection.sendString(lblNickname.getText());
        connection.sendString(lblInterlocutor.getText().trim());
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
                    int count = dataBase.countOfUnreadMessages(client.getTitle(), arrayList.get(i).toString());

                    if(!(count == 0)) {
                        el = el + "   (" + count + ")";
                    }
                    dfm.addElement(el);
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

    private void printDialog(String name) {
        // нужно изменить функцию
        ArrayList arrayList = dataBase.getDialog(client.getTitle(), name);

        for(int i = 0; i < arrayList.size(); ++i) {
            String[] arr = (String[]) arrayList.get(i);
            printMsg(arr[0], arr[1], arr[2]);
        }
    }

}
