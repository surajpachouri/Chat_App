import java.net.*;
import java.io.*;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Server extends JFrame {
    BufferedReader br;
    PrintWriter out;
    ServerSocket Server;
    Socket socket;
    public JLabel heading = new JLabel("Hi Suraj, This is Ashutosh ");
    public JTextArea messageArea = new JTextArea();
    public JTextField messageInput = new JTextField();
    public Font font = new Font("Roboto", Font.BOLD, 20);
    public Server() {
        try {
            Server = new ServerSocket(6666);
            System.out.println("Server is ready to accept connection!");
            System.out.println("Waiting for client to connect...");
            socket = Server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createGUI() {
        this.setTitle("Server Messager");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("sk.jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setLayout(new BorderLayout());
        this.add(heading, BorderLayout.NORTH);
        this.add(messageArea, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    public void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                }

            }

        });

    }


    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader Started!");
            try {

                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("User terminated the chat !!!");
                        JOptionPane.showMessageDialog(this, "User terminated the chat !!!");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    System.out.println("Ashutosh: " + msg);
                    messageArea.append("Suraj " + msg + "\n");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        new Thread(r1).start();

    }

    public void startWriting() {
        Runnable r2 = () -> {
            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }

                }
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(r2).start();

    }
//Main Method
    public static void main(String args[]) {
        System.out.println("Server Started listening on some random port!");
        System.out.println("Starting Server......");
        new Server();

    }
}