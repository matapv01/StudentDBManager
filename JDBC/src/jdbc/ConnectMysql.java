package jdbc;

import java.sql.*;
import java.util.*;
import javax.swing.*;

public class ConnectMysql {

    private String DB_URL = "jdbc:mysql://sql.freedb.tech:3306/freedb_Student";
    private String USER_NAME = "freedb_userx";
    private String PASSWORD = "AC#WW*aUKJm2x4a";
    private Connection conn;

    public ConnectMysql() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

        conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);  
    }

    public ArrayList<SinhVien> getStudentList() {
        ArrayList<SinhVien> studentList = new ArrayList<>();
        String query = "SELECT MaSV, HoTen, Lop, GPA FROM SinhVien";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maSV = rs.getString("MaSV");
                String hoTen = rs.getString("HoTen");
                String lop = rs.getString("Lop");
                float gpa = rs.getFloat("GPA");
                studentList.add(new SinhVien(maSV, hoTen, lop, gpa));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("nhận danh sách sv bị lỗi.");
        }

        return studentList;
    }
    public void updateData(String MaSV, String hoTen, String lop, String GPA) throws SQLException {
        // Kiểm tra sự tồn tại của sinh viên theo MaSV
        String checkQuery = "SELECT COUNT(*) FROM SinhVien WHERE MaSV = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setString(1, MaSV);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                JOptionPane.showMessageDialog(null, "Mã sinh viên " + MaSV + " không tồn tại.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Thực hiện truy vấn cập nhật
        String query = "UPDATE SinhVien SET HoTen=?, Lop=?, GPA=? WHERE MaSV=?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, hoTen);
            pst.setString(2, lop);

            // Chuyển đổi GPA thành số thực
            try {
                pst.setFloat(3, Float.parseFloat(GPA));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "GPA không hợp lệ.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            pst.setString(4, MaSV);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Cập nhật thành công cho sinh viên " + MaSV + ".", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Cập nhật thất bại.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void deleteData(String maSV) throws SQLException {
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa sinh viên " + maSV + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM SinhVien WHERE MaSV=?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, maSV);
                pst.executeUpdate();
                System.out.println("Sinh viên " + maSV + " đã được xóa.");
            }
        }
    }

    public void addStudent(String MaSV, String hoTen, String lop, float gpa) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM SinhVien WHERE MaSV = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setString(1, MaSV);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "Mã sinh viên " + MaSV + " đã tồn tại.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        // Truy vấn thêm
        String query = "INSERT INTO SinhVien (MaSV, HoTen, Lop, GPA) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, MaSV); 
        pst.setString(2, hoTen); 
        pst.setString(3, lop); 
        pst.setFloat(4, gpa);  
        pst.executeUpdate();   
        conn.close();
    }
}
