/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 *
 * @author HeLi
 */
public class MyForm extends JFrame {

    private JPanel menu;
    private JButton addButton, showButton, updateButton, deleteButton, resetButton;
    private JTable table;

    private TextBox maSVField, hoTenField, lopField, gpaField;

    private DefaultTableModel tableModel;

    private TreeSet<SinhVien> svList = new TreeSet<>();

    private ConnectMysql cn;

    public MyForm(Rectangle rec) throws SQLException, ClassNotFoundException {
        setBounds(rec);
        setTitle("SQL");
        cn = new ConnectMysql();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        menu = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel();

        tableModel.addColumn("MaSV");
        tableModel.addColumn("HoTen");
        tableModel.addColumn("lop");
        tableModel.addColumn("GPA");

        table = new JTable(tableModel);

        table.setDropMode(DropMode.ON);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    showSelectedRow();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        add(BorderLayout.CENTER, scrollPane);
        add(BorderLayout.NORTH, menu);

        addButton = new JButton("add");
        showButton = new JButton("show");
        updateButton = new JButton("update");
        deleteButton = new JButton("delete");
        resetButton = new JButton("reset");

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetButtonAction();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addButtonAction();
                } catch (SQLException ex) {
                    Logger.getLogger(MyForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteButtonAction();
                } catch (SQLException ex) {
                    Logger.getLogger(MyForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showButtonAction();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateButtonAction();
                } catch (SQLException ex) {
                    Logger.getLogger(MyForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JPanel buttonField = new JPanel(new FlowLayout());

        buttonField.add(showButton);
        buttonField.add(updateButton);
        buttonField.add(deleteButton);
        buttonField.add(resetButton);
        buttonField.add(addButton);

        menu.add(BorderLayout.SOUTH, buttonField);

        JPanel formField = new JPanel(new FlowLayout());

        maSVField = new TextBox("MSV", new Dimension(100, 50));
        hoTenField = new TextBox("hoTen", new Dimension(100, 50));
        lopField = new TextBox("lop", new Dimension(100, 50));
        gpaField = new TextBox("gpa", new Dimension(100, 50));

        formField.add(maSVField);
        formField.add(hoTenField);
        formField.add(lopField);
        formField.add(gpaField);

        menu.add(BorderLayout.CENTER, formField);
    }

    private void resetButtonAction() {
        maSVField.resetText();
        hoTenField.resetText();
        lopField.resetText();
        gpaField.resetText();
    }

    private void showSelectedRow() {
        int rowIndex = table.getSelectedRow();
        if (rowIndex != -1) {
            maSVField.setText((String) table.getValueAt(rowIndex, 0));
            hoTenField.setText((String) table.getValueAt(rowIndex, 1));
            lopField.setText((String) table.getValueAt(rowIndex, 2));
            gpaField.setText(String.valueOf(table.getValueAt(rowIndex, 3)));
        } else {
            resetButtonAction();
        }
    }

    private void addButtonAction() throws SQLException {
        if (!maSVField.getText().isEmpty() && !hoTenField.getText().isEmpty()
                && !lopField.getText().isEmpty() && !gpaField.getText().isEmpty()) {

            try {
                // Parse GPA as a float
                float gpa = Float.parseFloat(gpaField.getText());
                if (!(gpa >= 0f && gpa <= 4f)) {
                    throw new NumberFormatException();
                }

                // Add the student with the GPA as a float
                cn.addStudent(maSVField.getText(), hoTenField.getText(), lopField.getText(), gpa);

                // Update table model with the parsed GPA as float
                tableModel.addRow(new Object[]{maSVField.getText(), hoTenField.getText(), lopField.getText(), gpa});

                resetButtonAction();

                table.revalidate();
                table.repaint();
                JOptionPane.showMessageDialog(this, "Thêm thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Nhập GPA đúng định dạng.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập hết ô thông tin.", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteButtonAction() throws SQLException {
        cn.deleteData(maSVField.getText());
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Xoá thành công!", "Done", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi chưa chọn hàng để xoá.", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateButtonAction() throws SQLException {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {

            String maSV = maSVField.getText();
            String hoTen = hoTenField.getText();
            String lop = lopField.getText();
            String gpa = gpaField.getText();

            table.setValueAt(maSV, selectedRow, 0);
            table.setValueAt(hoTen, selectedRow, 1);
            table.setValueAt(lop, selectedRow, 2);
            table.setValueAt(gpa, selectedRow, 3);
            resetButtonAction();
            cn.updateData(maSV, hoTen, lop, gpa);

        } else {
            JOptionPane.showMessageDialog(this, "Chưa chọn hàng để cập nhật.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showButtonAction() {

        ArrayList<SinhVien> newSVList = cn.getStudentList();
        newSVList.removeAll(svList);
        svList.addAll(newSVList);

        if (svList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Danh sách sinh viên rỗng.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (SinhVien sv : newSVList) {
            tableModel.addRow(new Object[]{
                sv.getMaSV(),
                sv.getHoTen(),
                sv.getLop(),
                sv.getGpa()
            });
        }

        table.revalidate();
        table.repaint();
        JOptionPane.showMessageDialog(this, "Danh sách sinh viên đã được cập nhật", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

    }

}
