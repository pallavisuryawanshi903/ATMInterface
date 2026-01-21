package atm;
import javax.swing.*;
import java.sql.*;

public class WithdrawFrame extends JFrame {

    public WithdrawFrame(int userId) {
        setTitle("Withdraw");
        setSize(250,150);
        setLayout(null);

        JLabel l = new JLabel("Amount:");
        JTextField amt = new JTextField();
        JButton b = new JButton("Withdraw");

        l.setBounds(20,20,80,25);
        amt.setBounds(100,20,100,25);
        b.setBounds(70,70,100,30);

        add(l); add(amt); add(b);

        b.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amt.getText());
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                        "UPDATE users SET balance = balance - ? WHERE user_id=? AND balance>=?"
                );
                ps.setDouble(1, amount);
                ps.setInt(2, userId);
                ps.setDouble(3, amount);

                int updated = ps.executeUpdate();

                if(updated==0) {
                    JOptionPane.showMessageDialog(this,"Insufficient Balance!");
                    return;
                }

                PreparedStatement ts = con.prepareStatement(
                        "INSERT INTO transactions(user_id,type,amount) VALUES (?,?,?)"
                );
                ts.setInt(1, userId);
                ts.setString(2, "Withdraw");
                ts.setDouble(3, amount);
                ts.executeUpdate();

                JOptionPane.showMessageDialog(this,"Withdraw Successful");
                dispose();

            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
