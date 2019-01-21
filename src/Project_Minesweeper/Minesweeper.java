package Project_Minesweeper;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.Flow;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Minesweeper extends JPanel implements AWTEventListener, ActionListener {

    public static boolean isColorCheatOn = false;
    private int max_bombs;
    private int rows;
    private int cols;

    private JLabel timerLabel = new JLabel("0");
    private JButton resetButton = new JButton("Reset");
    private GameState state = GameState.NotStarted;
    private int total;
    private JPanel mainPanel;
    private JLabel bombCountLabel;

    public Minesweeper(int rows, int cols, int maxBombs) {
        this.rows = rows;
        this.cols = cols;
        this.max_bombs = maxBombs;

        total = rows * cols;
        mainPanel = new JPanel(new GridLayout(rows, cols));
        bombCountLabel = new JLabel(max_bombs + "");

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        createButtons();
        addControlPanel();
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
    }


    private void startThread() { // Starts the game
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (state == GameState.Playing) {
                    timerLabel.setText((Long.parseLong(timerLabel.getText()) + 1) + "");
                    timerLabel.updateUI();
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void showAbout() {
        JOptionPane.showMessageDialog
                (this,
                        "<html>Brigitte: Rally to me! </html>",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE);
    }

    private void restartGame() {//Restarts the game
        state = GameState.NotStarted;
        timerLabel.setText("0");
        mainPanel.removeAll();
        createButtons();
        mainPanel.updateUI();
        bombCountLabel.setText("" + max_bombs);
        bombCountLabel.updateUI();
    }

    private void addControlPanel() {//Generates the menu in the game(bombCounter, timer and restart button)
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        timerPanel.add(timerLabel);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resetButton.setToolTipText("<html>Press <B>F2</B> to reset the current game </html>");
        panel.add(bombCountLabel);
        panel.add(resetButton);

        JPanel panel1 = new JPanel(new GridLayout(1, 3));
        panel1.add(bombCountLabel);
        panel1.add(panel);
        panel1.add(timerPanel);
        add(panel1, BorderLayout.NORTH);
        resetButton.addActionListener(this);

    }

    public void createButtons() {//Generates the bombs
        List<Point> lstBombLocation = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton btn = getButton(lstBombLocation, total, new Point(row, col) {
                    @Override
                    public String toString() {
                        return (int) getX() + ", " + (int) getY();
                    }

                    @Override
                    public boolean equals(Object obj) {
                        return ((Point) obj).getX() == getX() && ((Point) obj).getY() == getY();
                    }
                });
                mainPanel.add(btn);
            }
        }

        while (lstBombLocation.size() < max_bombs) {
            updateBombs(lstBombLocation, mainPanel.getComponents());
        }
        for (Component c : mainPanel.getComponents()) {
            updateBombCount((GameButton) c, mainPanel.getComponents());
        }
    }

    private void updateBombs(List<Point> lstBombLocation, Component[] components) {//Changes the bombs
        Random r = new Random();
        for (Component c : components) {
            Point location = ((GameButton) c).getPosition();
            int currentPosition = new Double(((location.x) * cols) + location.getY())
                    .intValue();
            int bombLocation = r.nextInt(total);

            if (bombLocation == currentPosition) {
                ((GameButton) c).setBomb(true);
                lstBombLocation.add(((GameButton) c).getPosition());
                return;
            }
        }
    }

    private GameButton getButton(List<Point> lstBombsLocation, int totalLocations, Point location) {//generates the buttons
        GameButton btn = new GameButton(location);
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setFocusable(false);
        if (lstBombsLocation.size() < max_bombs) {
            if (isBomb()) {
                btn.setBomb(true);
                lstBombsLocation.add(location);
            }
        }
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (state != GameState.Playing) {
                    state = GameState.Playing;
                    startThread();
                }
                if (((GameButton) mouseEvent.getSource()).isEnabled() == false) {
                    return;
                }
                if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                    if (((GameButton) mouseEvent.getSource()).getState() == State.Marked) {
                        ((GameButton) mouseEvent.getSource()).setState(State.Initial);
                        bombCountLabel.setText((Long.parseLong(bombCountLabel.getText()) + 1) + "");
                        ((GameButton) mouseEvent.getSource()).updateUI();
                        return;
                    }
                    ((GameButton) mouseEvent.getSource()).setState(State.Clicked);
                    if (((GameButton) mouseEvent.getSource()).isBomb()) {
                        blastBombs();
                        return;
                    } else {
                        if (((GameButton) mouseEvent.getSource()).getBombCount() == 0) {
                            updateSurroundingZeros(((GameButton) mouseEvent.getSource()).getPosition());
                        }
                    }
                    if (!checkGameState()) {
                        ((GameButton) mouseEvent.getSource()).setEnabled(false);
                    }
                } else if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
                    if (((GameButton) mouseEvent.getSource()).getState() == State.Marked) {
                        ((GameButton) mouseEvent.getSource()).setState(State.Initial);
                        bombCountLabel.setText((Long.parseLong(bombCountLabel.getText()) + 1) + "");
                    } else {
                        ((GameButton) mouseEvent.getSource()).setState(State.Marked);
                        bombCountLabel.setText((Long.parseLong(bombCountLabel.getText()) - 1) + "");
                    }
                }
                ((GameButton) mouseEvent.getSource()).updateUI();
            }
        });
        return btn;
    }

    private boolean checkGameState() {//Checks the game stan
        boolean isWin = false;
        for (Component c : mainPanel.getComponents()) {
            GameButton b = (GameButton) c;
            if (b.getState() != State.Clicked) {
                if (b.isBomb()) {
                    isWin = true;
                } else return false;
            }
        }

        if (isWin) {
            state = GameState.Finished;
            for (Component c : mainPanel.getComponents()) {
                GameButton b = (GameButton) c;
                if (b.isBomb()) {
                    b.setState(State.Marked);
                }
                b.setEnabled(false);
            }
            JOptionPane.showMessageDialog(this, "You won the game :D",
                    "Congrats", JOptionPane.INFORMATION_MESSAGE, null);
        }
        return isWin;
    }

    private void updateSurroundingZeros(Point currentPoint) {
        Point[] points = getSurroundings(currentPoint);

        for (Point p : points) {
            GameButton b = getButtonAt(mainPanel.getComponents(), p);
            if (b != null
                    && b.getBombCount() == 0
                    && b.getState() != State.Clicked
                    && b.getState() != State.Marked
                    && b.isBomb() == false) {
                b.setState(State.Clicked);
                updateSurroundingZeros(b.getPosition());
                b.updateUI();
            }

            if (b != null
                    && b.getBombCount() > 0
                    && b.getState() != State.Clicked
                    && b.getState() != State.Marked
                    && b.isBomb() == false) {
                b.setEnabled(false);
                b.setState(State.Clicked);
                b.updateUI();
            }
        }
    }

    private void blastBombs() {//Blasts a bomb
        int blastCount = 0;
        for (Component c : mainPanel.getComponents()) {
            ((GameButton) c).setEnabled(false);
            ((GameButton) c).transferFocus();

            if (((GameButton) c).isBomb()
                    && ((GameButton) c).getState() != State.Marked) {
                ((GameButton) c).setState(State.Clicked);
                ((GameButton) c).updateUI();
                blastCount++;
            }

            if (((GameButton) c).isBomb() == false
                    && ((GameButton) c).getState() == State.Marked) {
                ((GameButton) c).setState(State.WrongMarked);
            }
        }

        bombCountLabel.setText("" + blastCount);
        bombCountLabel.updateUI();

        state = GameState.Finished;
        JOptionPane.showMessageDialog(this,
                "You lost :(",
                "Game over",
                JOptionPane.INFORMATION_MESSAGE,
                null);

        for (Component c : mainPanel.getComponents()) {
            GameButton b = (GameButton) c;
            b.setEnabled(false);
        }
    }

    private boolean isBomb() {//Checks is a button has a bomb
        Random r = new Random();
        return r.nextInt(rows) == 1;
    }

    private Point[] getSurroundings(Point cPoint) {// Gets the surroundigs of a button
        int cX = (int) cPoint.getX();
        int cY = (int) cPoint.getY();
        Point[] points = {new Point(cX - 1, cY - 1), new Point(cX - 1, cY), new Point(cX - 1, cY + 1), new Point(cX, cY - 1), new Point(cX, cY + 1), new Point(cX + 1, cY - 1), new Point(cX + 1, cY), new Point(cX + 1, cY + 1)};
        return points;
    }

    private void updateBombCount(GameButton btn, Component[] components) {//Changes the bombs count
        Point[] points = getSurroundings(btn.getPosition());

        for (Point p : points) {
            GameButton b = getButtonAt(components, p);
            if (b != null && b.isBomb()) {
                btn.setBombCount(btn.getBombCount() + 1);
            }
        }

        btn.setText(btn.getBombCount() + "");
    }

    private GameButton getButtonAt(Component[] components, Point position) {//Get buttons at a given place
        for (Component btn : components) {
            if ((((GameButton) btn).getPosition().equals(position))) {
                return (GameButton) btn;
            }
        }
        return null;
    }

    public void eventDispatched(AWTEvent event) {
        if (KeyEvent.class.isInstance(event) && ((KeyEvent) (event)).getID() == KeyEvent.KEY_RELEASED) {//The separete keys
            if (((KeyEvent) (event)).getKeyCode() == KeyEvent.VK_F1) {
                showAbout();
            }
            if (((KeyEvent) (event)).getKeyCode() == KeyEvent.VK_F2) {
                restartGame();
            }
            if (((KeyEvent) (event)).getKeyCode() == KeyEvent.VK_F3) {
                isColorCheatOn = !isColorCheatOn;
                if (state == GameState.Playing) {
                    mainPanel.updateUI();
                }
            }

            if (((KeyEvent) (event)).getKeyCode() == KeyEvent.VK_F12) {
                for (Component c : mainPanel.getComponents()) {
                    GameButton b = (GameButton) c;
                    if (b.isBomb() == false) {
                        b.setState(State.Clicked);
                    } else {
                        b.setState(State.Marked);
                    }
                    b.setEnabled(false);
                }
                checkGameState();
            }
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {//Какво да прави бутона за рестарт
        if (actionEvent.getSource() == resetButton) {
            restartGame();
        }
    }

    public static enum State {
        Clicked, Marked, Initial, WrongMarked
    }

    public static enum GameState {
        NotStarted, Playing, Finished
    }


}