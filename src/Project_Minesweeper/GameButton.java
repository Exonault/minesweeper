package Project_Minesweeper;

import javax.swing.*;
import java.awt.*;

public class GameButton extends JButton {
    private boolean isBomb = false;
    private Point position = null;
    private int bombCount = 0;
    private Minesweeper.State state = Minesweeper.State.Initial;

    public GameButton(Point position) {
        setPosition(position);
        setText(position.toString());
    }

    //Getters and setters for: bombCount,bomb,state of the game
    public Minesweeper.State getState() {
        return state;
    }

    public void setState(Minesweeper.State state) {
        this.state = state;
        if (getBombCount() == 0 && !isBomb) {
            setEnabled(false);
        }
    }

    public int getBombCount() {
        return bombCount;
    }

    public void setBombCount(int bombs) {
        this.bombCount = bombs;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean isBomb) {
        this.isBomb = isBomb;
    }

    @Override
    public String getText() {//What to be the text in each button
        if (state == Minesweeper.State.Initial) {
            return "";
        }

        if (state == Minesweeper.State.Marked) {
            return "\u00B6";
        }

        if (state == Minesweeper.State.Clicked) {
            if (isBomb) {
                return "<html><font size = '16'><b>*</b></font></html>";
            } else {
                if (getBombCount() > 0) {
                    return getBombCount() + "";
                } else return "";
            }
        }

        if (state == Minesweeper.State.WrongMarked) {
            return "X";
        }
        return super.getText();
    }

    @Override
    public Color getBackground() {//Color of each element(button,bomb,has bombs near it)
        if (Minesweeper.isColorCheatOn && isBomb)
            return Color.YELLOW;
        if (state == Minesweeper.State.Clicked) {
            if (isBomb) {
                return Color.YELLOW;
            }
            if (getBombCount() == 1 || getBombCount() == 5) {
                Color color = new Color(102, 0, 102);
                return color;
            }
            if (getBombCount() == 2 || getBombCount() == 6) {
                Color color = new Color(0,51,0);
                return color;
            }
            if (getBombCount() == 3 || getBombCount() == 7) {
                Color color = new Color(0, 51, 102);
                return color;
            }
            if (getBombCount() == 4 || getBombCount() == 8) {
                Color color = new Color(128, 0, 0);
                return color;
            }
        }

        if (isEnabled()) {
            return Color.LIGHT_GRAY;
        } else {
            return super.getBackground();
        }
    }
}
