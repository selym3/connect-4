package objects;

import java.awt.Color;
import java.awt.Graphics;

import display.MouseTracker;
import util.Table;

public class Board extends Piece {

    private final Player player;
    private final MouseTracker mouseTracker;

    private Table board;
    private final int rows, cols;

    private boolean prev;

    private int lastWinner;

    public Board(Player player, int screenWidth, int screenHeight, int rows, int cols, MouseTracker mouseTracker) {
        super(0, screenHeight / (rows + 1), screenWidth, (rows) * screenHeight / (rows + 1));
        this.rows = rows;
        this.cols = cols;
        board = new Table(rows, cols); // a rows by b columns
        this.player = player;
        this.mouseTracker = mouseTracker;
    }

    @Override
    public void tick() {

        if (mouseTracker.down() && mouseTracker.down() != prev) {

            if (board.getRow(player.getCol()) != -1 && lastWinner != 1 && lastWinner != 2) {
                board.drop(player.getCol(), player.getPlayer());

                int winner = board.winning();
                lastWinner = winner;

                if (winner == 1) {
                    System.out.println("YOU WON!");
                } else if (winner == 2) {
                    System.out.println("YOU LOST!");
                } else if (winner == 3) {
                    System.out.println("TIED!");
                }
                player.updatePlayer();

            }

        }

        prev = mouseTracker.down();
    }

    @Override
    public void render(Graphics g) {
        // xx represents col, yy represents rows
        // yy starts at 1 FOR RENDERING because it relates to screen positition at which
        // to render,
        // meaning u have to go one row donw

        for (int xx = 0; xx < cols; xx++) {
            g.setColor(Color.GRAY);
            for (int yy = 0; yy < rows; yy++) {

                // RENDER BACKGROUND BASED ON IF ITS THE SELECTED COLUMN, OR THE NEXT POSSIBLE
                // MOVE
                if (xx == player.getCol()) {
                    g.setColor(new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), 155));
                    if (board.getRow(xx) == yy) {
                        g.setColor(player.getPlayer() == Player.PLAYER1
                                ? new Color(Player.P1COLOR.getRed(), Player.P1COLOR.getGreen(),
                                        Player.P1COLOR.getBlue(), 155)
                                : new Color(Player.P2COLOR.getRed(), Player.P2COLOR.getGreen(),
                                        Player.P2COLOR.getBlue(), 155));
                    }
                } else {
                    g.setColor(Color.GRAY);
                }
                g.fillRect(xx * w / cols, (yy + 1) * h / rows, w / cols, h / rows);

                // RENDER BORDERS
                g.setColor(Color.WHITE);
                g.drawRect(xx * w / cols, (yy + 1) * h / rows, w / cols, h / rows);

                // RENDER ALL PLACED PIECES
                if (board.get(yy, xx) != 0) {
                    g.setColor(board.get(yy, xx) == Player.PLAYER1 ? Player.P1COLOR : Player.P2COLOR);
                    g.fillRect((xx * w / cols) + 3, ((yy + 1) * h / rows) + 3, w / cols - 6, h / rows - 6);
                }

            }
        }
    }

}