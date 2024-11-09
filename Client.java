import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client implements ActionListener, Runnable {

    private final String serverAddress;
    private final int serverPort;
    public final String username;
    public final Color themeColor;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private JFrame frame;
    private JPanel topPanel;
    private JTextField msgField;
    private JButton sendButton;
    private JPanel textArea;
    private Box vertical;
    static String mymsg;

    private final static Color purple = new Color(138, 37, 196);
    private final static Color pinkish = new Color(245, 48, 94);
    private final static Color yellowColor = new Color(246, 181, 0);

    public final Color blackForBg = new Color(26, 32, 47);
    public final Color blackForMsg = new Color(57, 71, 101);

    public Client(String serverAddress, int serverPort, String username, Color themeColor) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.username = username;
        this.themeColor = themeColor;

        initializeGUI(username);
    }

    private void initializeGUI(String username) {
        frame = new JFrame(username);
        topPanel = new JPanel();
        msgField = new JTextField();
        sendButton = new JButton("Send");
        textArea = new JPanel();
        vertical = Box.createVerticalBox();

        frame.getContentPane().setBackground(blackForBg);
        frame.setLayout(null);
        frame.setSize(450, 700);
        frame.setLocation(10, 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        topPanel.setLayout(null);
        topPanel.setBackground(themeColor);
        topPanel.setBounds(0, 0, 450, 70);

        ImageIcon backIcon = new ImageIcon("icons/3.png");
        Image backIconSized = backIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel label1 = new JLabel(new ImageIcon(backIconSized));
        label1.setBounds(5, 17, 30, 30);

        ImageIcon dpIcon = new ImageIcon("icons/grp_icon.png");
        Image dpIconSized = dpIcon.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
        JLabel label2 = new JLabel(new ImageIcon(dpIconSized));
        label2.setBounds(40, 5, 60, 60);

        JLabel name = new JLabel("Chat Group");
        name.setFont(new Font("Arial", Font.BOLD, 18));
        name.setForeground(Color.WHITE);
        name.setBounds(110, 15, 100, 18);

        JLabel activeStatus = new JLabel("You, other members");
        activeStatus.setFont(new Font("Arial", Font.PLAIN, 14));
        activeStatus.setForeground(Color.WHITE);
        activeStatus.setBounds(110, 35, 110, 20);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(0, 70, 431, 545);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        msgField.setBorder(BorderFactory.createEmptyBorder());
        msgField.setBackground(blackForMsg);
        msgField.setForeground(Color.WHITE);
        msgField.setBounds(10, 615, 290, 40);
        msgField.setFont(new Font("Arial", Font.PLAIN, 16));

        sendButton.setBounds(310, 615, 123, 40);
        sendButton.setForeground(Color.WHITE);
        sendButton.setBackground(themeColor);
        sendButton.setBorder(new RoundedBorder(10));
        frame.getRootPane().setDefaultButton(sendButton);
        sendButton.addActionListener(this);

        topPanel.add(label1);
        topPanel.add(label2);
        topPanel.add(name);
        topPanel.add(activeStatus);

        frame.add(topPanel);
        frame.add(scrollPane);
        frame.add(msgField);
        frame.add(sendButton);

        frame.setVisible(true);

        textArea.setBackground(blackForBg);

        // Adding rounded borders for textArea
        textArea.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(15, 15, 15, 15),
                new RoundedBorder(15)
        ));
    }

    public void connectToServer() {
        try {
            socket = new Socket(serverAddress, serverPort);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Thread thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    public static JPanel formatLabel(String out, Color themeColor) {
        JPanel panel3 = new JPanel();
        JLabel l1 = new JLabel("<html><p style=\"width:150px;\">" + out + "</p></html>");
        l1.setBackground(themeColor);
        l1.setForeground(Color.WHITE);
        l1.setOpaque(true);
        l1.setBorder(new RoundedBorder(15));
        mymsg = out;
        panel3.add(l1);
        panel3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel
        return panel3;
    }

    public static JPanel formatLabelReceived(String out) {
        JPanel panel3 = new JPanel();
        JLabel l1 = new JLabel("<html><p style=\"width:150px;\">" + out + "</p></html>");
        l1.setBackground(new Color(57, 71, 101));
        l1.setForeground(Color.WHITE);
        l1.setOpaque(true);
        l1.setBorder(new RoundedBorder(15));
        mymsg = out;
        panel3.add(l1);
        panel3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel
        return panel3;
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            textArea.setLayout(new BorderLayout());
            JPanel right = new JPanel(new BorderLayout());

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String timestamp = sdf.format(new Date());

            String out = "<b style=\"color:white;\"><u>" + username + ":</u></b><br>" + msgField.getText() + "<br><i style=\"font-size:10px;color:gray;\">" + timestamp + "</i>";

            JPanel p4 = formatLabel(out, themeColor);
            p4.setBackground(blackForBg);
            right.setBackground(blackForBg);
            right.add(p4, BorderLayout.LINE_END);
            vertical.add(right);
            textArea.add(vertical, BorderLayout.PAGE_START);
            frame.validate();

            msgField.setText("");
            dataOutputStream.writeUTF(out);

            System.out.println("message sent successfully");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                String receivedMessage = dataInputStream.readUTF();
                System.out.println("received message - " + receivedMessage);
                displayReceivedMessage(receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayReceivedMessage(String message) {
        int startIndex = message.indexOf("<u>") + 3;
        int endIndex = message.indexOf(":", startIndex);
        String senderUsername = message.substring(startIndex, endIndex);
        System.out.println("Received message from: " + senderUsername);
        System.out.println("sender - " + senderUsername + ", username - " + username);

        if (senderUsername.equals(username)) {
            frame.validate();
            JPanel p2 = formatLabel(message, themeColor);
            JPanel left = new JPanel(new BorderLayout());
            left.add(p2, BorderLayout.LINE_START);
            System.out.println("I removed the sent message");
            vertical.remove(left);
            frame.validate();
        } else {
            frame.validate();
            JPanel p2 = formatLabelReceived(message);
            JPanel left = new JPanel(new BorderLayout());
            p2.setBackground(blackForBg);
            left.setBackground(blackForBg);
            left.add(p2, BorderLayout.LINE_START);
            vertical.add(left);
            frame.validate();
            System.out.println(username + " : " + message);
        }

        frame.repaint();
    }
}
