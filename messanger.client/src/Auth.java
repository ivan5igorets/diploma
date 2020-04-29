//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class Auth extends JFrame {
//
//    private static final int WIDTH = 1000;
//    private static final int HEIGHT = 600;
////    private JLabel lblTitle;
////
////    private JPanel panelSignIn;
////    private JPanel panelLogIn;
////
//    private final JLabel lblEnterLogin = new JLabel("Введите логин");
//    private final JLabel lblEnterPass1 = new JLabel("Введите пароль");
//    private final JLabel lblEnterPass2 = new JLabel("Введите пароль повторно");
//
//    private JTextField tfLogin;
//    private JTextField tfPassword1;
//    private JTextField tfPassword2;
//
//    private JButton btnRegistration;
//    private JButton btnBack;
//
//    public Auth() {
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setSize(WIDTH, HEIGHT);
//        setLocationRelativeTo(null);
//        setResizable(false);
//        setVisible(true);
//
//        signUp();
//
//    }
//
//    private void signUp() {
//        JPanel panelSignUp = new JPanel();
//
//
//
//        add(panelSignUp);
//        panelSignUp.setLayout(null);
//
//        setTitle("Регистрация");
//        panelSignUp.add(lblEnterLogin);
//        lblEnterLogin.setBounds(450, 150, 100, 30);
//
//        tfLogin = new JTextField();
//        panelSignUp.add(tfLogin);
//        tfLogin.setBounds(400, 185, 200, 30);
//
//
//        panelSignUp.add(lblEnterPass1);
//        lblEnterPass1.setBounds(450, 220, 100, 30);
//
//        tfPassword1 = new JTextField();
//        panelSignUp.add(tfPassword1);
//        tfPassword1.setBounds(400, 250, 200, 30);
//
//
//        panelSignUp.add(lblEnterPass2);
//        lblEnterPass2.setBounds(420, 285, 160, 30);
//
//        tfPassword2 = new JTextField();
//        panelSignUp.add(tfPassword2);
//        tfPassword2.setBounds(400, 315, 200, 30);
//
//
//        btnRegistration = new JButton("Регистрация");
//        panelSignUp.add(btnRegistration);
//        btnRegistration.setBounds(400, 350, 200, 30);
//        btnRegistration.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                if (tfLogin.getText().equals("") || tfPassword1.getText().equals("") || tfPassword2.getText().equals("")) {
//                    return;
//                }
//
//                if (!tfPassword1.getText().equals(tfPassword2.getText())) {
//                    return;
//                }
//
//                // тут должно быть отправление на клиент
////                connection.sendString("/REGISTRATION");
////                connection.sendString(tfLogin.getText());
////                connection.sendString(tfPassword1.getText());
//
//
//                // переход на авторизацию
////                remove(panelSignIn);
//                removeAll();
//
//                validate();
//                repaint();
//
//                System.out.println("mast working");
//
//            }
//        });
//
//    }
//
//
////    private void signUp() {
////
////        panelSignIn = new JPanel();
////        add(panelSignIn);
////        panelSignIn.setLayout(null);
////
////        setTitle("Регистрация");
////        panelSignIn.add(lblEnterLogin);
////        lblEnterLogin.setBounds(450, 150, 100, 30);
////
////        tfLogin = new JTextField();
////        panelSignIn.add(tfLogin);
////        tfLogin.setBounds(400,185,200,30);
////
////
////        panelSignIn.add(lblEnterPass1);
////        lblEnterPass1.setBounds(450, 220, 100, 30);
////
////        tfPassword1 = new JTextField();
////        panelSignIn.add(tfPassword1);
////        tfPassword1.setBounds(400,250,200,30);
////
////
////        panelSignIn.add(lblEnterPass2);
////        lblEnterPass2.setBounds(420, 285, 160, 30);
////
////        tfPassword2 = new JTextField();
////        panelSignIn.add(tfPassword2);
////        tfPassword2.setBounds(400,315,200,30);
////
////
////        btnRegistration = new JButton("Регистрация");
////        panelSignIn.add(btnRegistration);
////        btnRegistration.setBounds(400,350,200,30);
////        btnRegistration.addActionListener(new ActionListener() {
////            @Override
////            public void actionPerformed(ActionEvent e) {
////
////                if(tfLogin.getText().equals("") || tfPassword1.getText().equals("") || tfPassword2.getText().equals("")) {
////                    return;
////                }
////
////                if(!tfPassword1.getText().equals(tfPassword2.getText()) ) {
////                    return;
////                }
////
////                // тут должно быть отправление на клиент
//////                connection.sendString("/REGISTRATION");
//////                connection.sendString(tfLogin.getText());
//////                connection.sendString(tfPassword1.getText());
////
////
////                // переход на авторизацию
//////                remove(panelSignIn);
////
////            }
////        });
////
////        btnBack = new JButton("Назад");
////        panelSignIn.add(btnBack);
////        btnBack.setBounds(450, 400,100,30);
////        btnBack.addActionListener(new ActionListener() {
////            @Override
////            public void actionPerformed(ActionEvent e) {
////                // переход на авторизацию
////            }
////        });
////
//
//
//
////    private void logIn() {
////        setTitle("Авторизация");
////        panelLogIn = new JPanel();
////        panelLogIn.setLayout(null);
////        add(panelLogIn);
////
////        panelLogIn.add(lblEnterLogin);
////        lblEnterLogin.setBounds(450, 150, 100, 30);
////
////        tfLogin = new JTextField();
////        panelLogIn.add(tfLogin);
////        tfLogin.setBounds(400,185,200,30);
////
////
////        panelLogIn.add(lblEnterPass1);
////        lblEnterPass1.setBounds(450, 220, 100, 30);
////
////        tfPassword1 = new JTextField();
////        panelLogIn.add(tfPassword1);
////        tfPassword1.setBounds(400,250,200,30);
////    }
//
//    public static void main(String[] args) {
//        new Auth();
//    }
//}
//
//class SignUp {
//
//    private JPanel panelSignIn;
//    private JPanel panelLogIn;
//
//    private final JLabel lblEnterLogin = new JLabel("Введите логин");
//    private final JLabel lblEnterPass1 = new JLabel("Введите пароль");
//    private final JLabel lblEnterPass2 = new JLabel("Введите пароль повторно");
//
//    private JTextField tfLogin;
//    private JTextField tfPassword1;
//    private JTextField tfPassword2;
//
//    private JButton btnRegistration;
//    private JButton btnBack;
//
//    SignUp(Auth auth) {
//
//        panelSignIn = new JPanel();
//        auth.add(panelSignIn);
//        panelSignIn.setLayout(null);
//
//        auth.setTitle("Регистрация");
//        panelSignIn.add(lblEnterLogin);
//        lblEnterLogin.setBounds(450, 150, 100, 30);
//
//        tfLogin = new JTextField();
//        panelSignIn.add(tfLogin);
//        tfLogin.setBounds(400, 185, 200, 30);
//
//
//        panelSignIn.add(lblEnterPass1);
//        lblEnterPass1.setBounds(450, 220, 100, 30);
//
//        tfPassword1 = new JTextField();
//        panelSignIn.add(tfPassword1);
//        tfPassword1.setBounds(400, 250, 200, 30);
//
//
//        panelSignIn.add(lblEnterPass2);
//        lblEnterPass2.setBounds(420, 285, 160, 30);
//
//        tfPassword2 = new JTextField();
//        panelSignIn.add(tfPassword2);
//        tfPassword2.setBounds(400, 315, 200, 30);
//
//
//        btnRegistration = new JButton("Регистрация");
//        panelSignIn.add(btnRegistration);
//        btnRegistration.setBounds(400, 350, 200, 30);
//        btnRegistration.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                if (tfLogin.getText().equals("") || tfPassword1.getText().equals("") || tfPassword2.getText().equals("")) {
//                    return;
//                }
//
//                if (!tfPassword1.getText().equals(tfPassword2.getText())) {
//                    return;
//                }
//
//                // тут должно быть отправление на клиент
////                connection.sendString("/REGISTRATION");
////                connection.sendString(tfLogin.getText());
////                connection.sendString(tfPassword1.getText());
//
//
//                // переход на авторизацию
////                remove(panelSignIn);
//                System.out.println("mast working");
//                auth.removeAll();
//
//
//
//
//            }
//        });
//
//    }
//}
//
//class LogIn {
//    JPanel panel2 = new JPanel();
//
//    JButton btn = new JButton("privet");
//
//
//    LogIn(Auth auth) {
//        System.out.println("LogIn: is started");
//
//        auth.add(panel2);
//
//        panel2.add(btn);
//
//        auth.validate();
//        auth.repaint();
//
//    }
//}
//
//
import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Auth extends JFrame {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;

    private JPanel panel1;
    private JPanel panel2;
    private ClientConnectionListener eventListener;


    public Auth(Connection connection) {

        // проверка авторизации пользователя, если авторизирован то запускаем клиент
        // если не авторизирован то запукаем авторизацию

        // нужно изменить
        if( ClientDB.getInstance().checkAuth() /* если пользователь авторизирован */) {
            // запускаем клиент с именем пользователя
            System.out.println("запуск клиента");
            eventListener.runClient();

            return;
        } else {
            initAuth(connection);
        }
    }

    private void initAuth(Connection connection) {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(WIDTH,HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);


        panel1 = new JPanel();
        setTitle("Авторизация");
        getContentPane().add(panel1);
        initPanelLogIn(panel1,connection);

        panel2 = new JPanel();
        initPanelSignUp(panel2, connection);
    }

    private void initPanelLogIn(JPanel panel, Connection connection) {

        final JLabel lblEnterLogin = new JLabel("Введите логин");
        final JLabel lblEnterPass1 = new JLabel("Введите пароль");
        final JLabel lblPrompt = new JLabel("нет аккаунта?");

        JTextField tfLogin;
        JPasswordField passwordField;

        JButton btnRegistration;
        JButton btnBack;

        panel.setLayout(null);
        panel.add(lblEnterLogin);
        lblEnterLogin.setBounds(450, 150, 100, 30);

        panel.add(lblEnterLogin);
        lblEnterLogin.setBounds(450, 150, 100, 30);

        tfLogin = new JTextField();
        panel.add(tfLogin);
        tfLogin.setBounds(400,185,200,30);

        panel.add(lblEnterPass1);
        lblEnterPass1.setBounds(450, 220, 100, 30);

        passwordField = new JPasswordField();
        panel.add(passwordField);
        passwordField.setBounds(400,250,200,30);



        btnRegistration = new JButton("войти");
        panel.add(btnRegistration);
        btnRegistration.setBounds(400, 290, 200, 30);
        btnRegistration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (tfLogin.getText().equals("") || passwordField.getPassword().equals("") ) {
                    fieldTinting(tfLogin.getBackground(), tfLogin);
                    fieldTinting(tfLogin.getBackground(), passwordField);
                    return;
                }

                if(tfLogin.getText().length() < 6) {
                    showHind("Количество символов имени должно быть не меньше 6", panel1);
                    return;
                }


                // проверяет количество символов в имени
                if ((tfLogin.getText().length() > 30)) {
                    showHind("Количество символов имени не должно привышать 30", panel1);
                    return;
                }

                // проверяет существование подстроки состоящей из пробела " "
                if (tfLogin.getText().indexOf(" ") != -1) {
                    showHind("Вместо пробела используйте '_'", panel1);
                    return;
                }

                if (passwordField.getPassword().length < 6 ) {
                    showHind("Пароль должен составлять не меньше 6 символов", panel1);
                    return;
                }


                if (spaceCharacterSearch(passwordField.getPassword())) {
                    showHind("Пробел является запрещённым символом", panel1);
                    return;
                }


                // тут должно быть отправление на клиент
                connection.sendString("/LOG_IN");
                connection.sendString(tfLogin.getText());

                String password = new String(passwordField.getPassword());

                System.out.println(tfLogin.getText());
                System.out.println(password);

                connection.sendString(password);


                // получить ответ с сервера
                if (false) {
                    // запускаем клиент
                } else {
                    showHind("ошибка авторизации", panel1);
                    paintOverTheButton(btnRegistration);
                }

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

                getContentPane().removeAll();
                getContentPane().add(panel2);
                setTitle("Регистрация");

                validate();
                repaint();

            }
        });


    }

    private void initPanelSignUp(JPanel panel, Connection connection) {

        final JLabel lblEnterLogin = new JLabel("Введите логин");
        final JLabel lblEnterPass1 = new JLabel("Введите пароль");
        final JLabel lblEnterPass2 = new JLabel("Введите пароль повторно");

        JTextField tfLogin;
        JTextField tfPassword1;
        JTextField tfPassword2;

        JButton btnRegistration;
        JButton btnBack;

        panel.setLayout(null);
        panel.add(lblEnterLogin);
        lblEnterLogin.setBounds(450, 150, 100, 30);

        panel.add(lblEnterLogin);
        lblEnterLogin.setBounds(450, 150, 100, 30);

        tfLogin = new JTextField();
        panel.add(tfLogin);
        tfLogin.setBounds(400,185,200,30);

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


        btnRegistration = new JButton("Регистрация");
        panel.add(btnRegistration);
        btnRegistration.setBounds(400, 350, 200, 30);
        btnRegistration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = tfLogin.getText();

                if (name.equals("") || tfPassword1.getText().equals("") || tfPassword2.getText().equals("")) {

                    fieldTinting(tfLogin.getBackground(), tfLogin);
                    fieldTinting(tfLogin.getBackground(), tfPassword1);
                    fieldTinting(tfLogin.getBackground(), tfPassword2);

                    return;
                }

                // удаляет пробелы в начале и в конце строки
                name = name.trim();

                // в случае сработываения ифов должны выскакивать подсказки
                if(name.length() < 6) {
                    showHind("Количество символов имени должно быть не меньше 6", panel2);
                    return;
                }


                // проверяет количество символов в имени
                if ((name.length() > 30)) {
                    showHind("Количество символов имени не должно привышать 30", panel2);
                    return;
                }

                // проверяет существование подстроки состоящей из пробела " "
                if(name.indexOf(" ") != -1) {
                    showHind("Вместо пробела используйте '_'", panel2);
                    return;
                }

                if(tfPassword1.getText().length() < 6) {
                    showHind("Пароль должен составлять не меньше 6 символов", panel2);
                    return;
                }


                if (tfPassword1.getText().indexOf(" ") != -1 || tfPassword2.getText().indexOf(" ") != -1) {
                    showHind("Пробел является запрещённым символом", panel2);
                    return;
                }

                if (!tfPassword1.getText().equals(tfPassword2.getText())) {
                    showHind("Пароли не совпадают", panel2);
                    return;
                }

                // тут должно быть отправление на клиент
                connection.sendString("/SIGN_UP");
                connection.sendString(tfLogin.getText());
                connection.sendString(tfPassword1.getText());


                // нужно получить результат регистрации пользователя и сообщить об этом пользователю
                // *здесть получаемм результат*
                // *каким либо обралом сообщеаем об этом пользователю напимер:*



                // нужно доработать
                if(false) {
                    // start client
                } else {
                    showHind("ошибка регистрации", panel2);
                    paintOverTheButton(btnRegistration);
                }


            }
        });

        btnBack = new JButton("назад");
        panel.add(btnBack);
        btnBack.setBounds(450,400,100,30);
        btnBack.setToolTipText("вернуться к авторизации");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                getContentPane().removeAll();
                getContentPane().add(panel1);
                setTitle("Авторизация");

                tfLogin.setText("");
                tfPassword1.setText("");
                tfPassword2.setText("");

                validate();
                repaint();
            }
        });

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

                validate();
                repaint();
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


    public static void main(String[] args) {

//        char[] arr = new char[6];
//        arr[0] = '1';
//        arr[2] = '2';
//        arr[3] = '6';
//        arr[4] = '4';
//        arr[5] = '4';
//        arr[1] = '5';
//
//        System.out.println(new Auth().spaceCharacterSearch(arr));

//        String word = "    ";
//        System.out.println(word.length());
//
//        word = word.trim();
//        System.out.println(word.length());



//
//        // удаляет пробелы в начале и в конце строки
//        word = word.trim();
//
//        // проверяет количество символов в имени
//        if ((word.length() > 30)) {
//            System.out.println("максимальное количество символов: 30");
//            return;
//        }
//
//        // проверяет существование подстроки состоящей из пробела " "
//        if(word.indexOf(" ") != -1) {
//            System.out.println("вместо пробела используйте '_'");
//            return;
//        }
//
//
//
//
//
//        System.out.println("|" + word + "|");

    }
}