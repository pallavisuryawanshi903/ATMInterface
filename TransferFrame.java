package atm;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class TransferFrame extends JFrame {

    public TransferFrame(int fromUserId) {

        setTitle("Transfer Money");
        setSize(350,220);
        setLayout(new GridLayout(4,2,10,10));

        JTextField toUserField = new JTextField();
        JTextField amountField = new JTextField();
        JButton transferBtn = new JButton("Transfer");

        // Row 1
        add(new JLabel("To User ID:"));
        add(toUserField);

        // Row 2
        add(new JLabel("Amount:"));
        add(amountField);

        // Row 3 (Empty left cell + Centered button on right)
//        add(new JLabel(""));   // empty cell

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(transferBtn);
        add(btnPanel);

        // Button Action
        transferBtn.addActionListener(e -> {
            try {
                int toUserId = Integer.parseInt(toUserField.getText());
                double amt = Double.parseDouble(amountField.getText());

                Connection con = DBConnection.getConnection();
                con.setAutoCommit(false);

                // Check balance
                PreparedStatement bal = con.prepareStatement(
                        "SELECT balance FROM users WHERE user_id=?"
                );
                bal.setInt(1, fromUserId);
                ResultSet rs = bal.executeQuery();

                if (!rs.next() || rs.getDouble("balance") < amt) {
                    JOptionPane.showMessageDialog(this, "Insufficient Balance!");
                    return;
                }

                // Debit sender
                PreparedStatement debit = con.prepareStatement(
                        "UPDATE users SET balance = balance - ? WHERE user_id=?"
                );
                debit.setDouble(1, amt);
                debit.setInt(2, fromUserId);
                debit.executeUpdate();

                // Credit receiver
                PreparedStatement credit = con.prepareStatement(
                        "UPDATE users SET balance = balance + ? WHERE user_id=?"
                );
                credit.setDouble(1, amt);
                credit.setInt(2, toUserId);

                if (credit.executeUpdate() == 0) {
                    JOptionPane.showMessageDialog(this, "User Not Found!");
                    return;
                }

                // Transaction logs
                PreparedStatement t1 = con.prepareStatement(
                        "INSERT INTO transactions(user_id,type,amount) VALUES (?,?,?)"
                );
                t1.setInt(1, fromUserId);
                t1.setString(2, "Transfer Sent");
                t1.setDouble(3, amt);
                t1.executeUpdate();

                PreparedStatement t2 = con.prepareStatement(
                        "INSERT INTO transactions(user_id,type,amount) VALUES (?,?,?)"
                );
                t2.setInt(1, toUserId);
                t2.setString(2, "Transfer Received");
                t2.setDouble(3, amt);
                t2.executeUpdate();

                // Transfer table entry
                PreparedStatement t3 = con.prepareStatement(
                        "INSERT INTO transfer(from_user,to_user,amount) VALUES (?,?,?)"
                );
                t3.setInt(1, fromUserId);
                t3.setInt(2, toUserId);
                t3.setDouble(3, amt);
                t3.executeUpdate();

                con.commit();
                JOptionPane.showMessageDialog(this, "Transfer Successful!");
                dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Transfer Failed!");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
