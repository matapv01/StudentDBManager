/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package jdbc;

import java.awt.Rectangle;
import java.sql.SQLException;

/**
 *
 * @author HeLi
 */
public class JDBC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MyForm form = new MyForm(new Rectangle(0, 0, 1280, 720));
        form.setVisible(true);
    }
    
}
