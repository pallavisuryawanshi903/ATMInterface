package atm;
import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    JTextField userField;
    JPasswordField pinField;

    public LoginFrame() {
        setTitle("ATM Login");
        setSize(350,220);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        userField = new JTextField(15);
        pinField = new JPasswordField(15);
        JButton loginBtn = new JButton("Login");

        gbc.gridx=0; gbc.gridy=0;
        add(new JLabel("User ID:"),gbc);
        gbc.gridx=1;
        add(userField,gbc);

        gbc.gridx=0; gbc.gridy=1;
        add(new JLabel("PIN:"),gbc);
        gbc.gridx=1;
        add(pinField,gbc);

        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2;
        add(loginBtn,gbc);

        loginBtn.addActionListener(e -> login());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    void login() {
        try {
            int id = Integer.parseInt(userField.getText());
            String pin = new String(pinField.getPassword());

            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM users WHERE user_id=? AND pin=?"
            );
            ps.setInt(1,id);
            ps.setString(2,pin);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                JOptionPane.showMessageDialog(this,"Login Successful");
                new DashboardFrame(id);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,"Invalid Login");
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
