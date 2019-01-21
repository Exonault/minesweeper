package Project_Minesweeper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class StartFrame {
    StartFrame() {//Generating the main menu
        JFrame frame = new JFrame("Main menu");

        JButton button = new JButton("Submit");
        button.setBounds(100, 100, 140, 40);

        JLabel dificulty = new JLabel();
        dificulty.setText("Enter dificulty :");
        dificulty.setBounds(10, 10, 100, 100);

        JLabel easy = new JLabel();
        easy.setBounds(10, 110, 200, 100);
        easy.setText("Easy - 9x9 with 10 bombs \n");

        JLabel medium = new JLabel();
        medium.setBounds(10, 130, 200, 100);
        medium.setText("Medium - 16x16 with 40 bombs \n");

        JLabel hard = new JLabel();
        hard.setBounds(10, 150, 200, 100);
        hard.setText("Hard - 16x30 with 99 bombs \n");

        JLabel custom = new JLabel();
        custom.setBounds(10, 170, 200, 100);
        custom.setText("Custom - You choose the numbers");

        JTextField textfield = new JTextField();
        textfield.setBounds(110, 50, 130, 30);
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String level = textfield.getText();
                    if (level.equals("Easy")) {
                        StartUp.startGame(9, 9, 10);
                    } else if (level.equals("Medium")) {
                        StartUp.startGame(16, 16, 40);
                    } else if (level.equals("Hard")) {
                        StartUp.startGame(16, 30, 99);
                    } else if (level.equals("Custom")) {
                        new CustomModeFrame();
                    } else StartUp.startGame(9, 9, 10);
                }
            }
        });

        frame.add(textfield);

        frame.add(dificulty);
        frame.add(easy);
        frame.add(medium);
        frame.add(hard);
        frame.add(custom);

        frame.add(button);
        frame.setBounds(500, 200, 300, 300);
//        frame.setSize(300, 300);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String level = textfield.getText();
                if (level.equals("Easy")) {
                    StartUp.startGame(9, 9, 10);
                } else if (level.equals("Medium")) {
                    StartUp.startGame(16, 16, 40);
                } else if (level.equals("Hard")) {
                    StartUp.startGame(16, 30, 99);
                } else if (level.equals("Custom")) {
                    new CustomModeFrame();
                } else StartUp.startGame(9, 9, 10);
            }
        });
    }
}
