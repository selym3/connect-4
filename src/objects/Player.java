package objects;

import java.awt.Color;
import java.awt.Graphics;

import display.MouseTracker;

public class Player extends Piece {

    public static final Color P1COLOR = Color.YELLOW;
    public static final Color P2COLOR = Color.RED;

    public static final int NOPLAYER = 0;
    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = 2;

    private final int screenW;
    private final MouseTracker mouseTracker;
    private int currentPlayer;
    private int cols;

    public Player(int screenW, int screenH, int rows, int cols, MouseTracker mouseTracker) {
        super(0, 0, screenW / (cols), screenH / (rows + 1));
        this.screenW = screenW;
        this.cols = cols;
        this.mouseTracker = mouseTracker;
        currentPlayer = 1;

    }

    public int getCol() {
        // Use this clamping method because if the mouse was really close to the edge it would round to COLS
        int col = (int) (mouseTracker.getX() / w);
        return col >= cols ? (cols - 1) : (col < 0) ? 0 : col;
    }

    public void updatePlayer() {
        currentPlayer = currentPlayer == 1 ? 2 : 1;
    }

    public int getPlayer() {
        return currentPlayer;
    }

    @Override
    public void tick() {
        x = mouseTracker.getX() - w / 2;
        x = x > screenW - w ? screenW - w : x;
        x = x < 0 ? 0 : x;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(currentPlayer == 1 ? Color.YELLOW : Color.RED);
        g.fillRect((int) x, (int) y, w, h);
    }

}