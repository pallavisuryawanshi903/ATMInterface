package atm;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class TransactionHistoryFrame extends JFrame {

    public TransactionHistoryFrame(int userId) {
        setTitle("Transaction History");
        setSize(500,300);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("Type");
        model.addColumn("Amount");
        model.addColumn("Date");

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM transactions WHERE user_id=?"
            );
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("date")
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        add(new JScrollPane(table));
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
