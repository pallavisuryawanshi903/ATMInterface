package atm;
import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    int userId;

    public DashboardFrame(int userId) {
        this.userId = userId;

        setTitle("ATM Dashboard");
        setSize(300,400);
        setLayout(new GridLayout(5,1));

        JButton withdraw = new JButton("Withdraw");
        JButton deposit = new JButton("Deposit");
        JButton transfer = new JButton("Transfer");
        JButton history = new JButton("Transaction History");
        JButton logout = new JButton("Logout");

        add(withdraw);
        add(deposit);
        add(transfer);
        add(history);
        add(logout);

        withdraw.addActionListener(e -> new WithdrawFrame(userId));
        deposit.addActionListener(e -> new DepositFrame(userId));
        transfer.addActionListener(e -> new TransferFrame(userId));
        history.addActionListener(e -> new TransactionHistoryFrame(userId));
        logout.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
