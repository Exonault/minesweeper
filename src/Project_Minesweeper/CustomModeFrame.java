package Project_Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomModeFrame {
    CustomModeFrame() {
        JFrame frame = new JFrame("Custom mode menu");
        JTextField columns, rows, bombs;

        columns = new JTextField("Columns", 10);
        rows = new JTextField("Rows", 10);
        bombs = new JTextField("Bombs", 10);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nrows = Integer.parseInt(rows.getText());
                int ncols = Integer.parseInt(columns.getText());
                int nbombs = Integer.parseInt(bombs.getText());
                StartUp.startGame(nrows, ncols, nbombs);
            }
        });

        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(rows);
        frame.getContentPane().add(columns);
        frame.getContentPane().add(bombs);
        frame.getContentPane().add(submitButton);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocation(500,200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
