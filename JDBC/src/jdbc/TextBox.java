/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author HeLi
 */
public class TextBox extends JPanel {
    private JLabel boxName;
    private JTextField text;
    
    public TextBox(String name, Dimension d) {
        setLayout(new GridLayout(2, 1));
        
        boxName = new JLabel(name);
        text = new JTextField();
        boxName.setHorizontalTextPosition(JTextField.CENTER);
        
        add(boxName);
        add(text);
        setPreferredSize(d);
    }
    
    public String getText() {
        return text.getText();
    }
    
    public void resetText() {
        text.setText("");
    }
    public void setText(String data) {
        text.setText(data);
    }
}
