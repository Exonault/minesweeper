package Project_Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class StartUp {
    public static void main(String[] args) {
        new StartFrame();
    }

    public static void startGame(int rows, int cols, int bombs) {//Generating the game
        JFrame frame = new JFrame("MineSweeper");
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.add(new Minesweeper(rows, cols, bombs));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(700,600);
        frame.setLocation(400,150);
    }
}
