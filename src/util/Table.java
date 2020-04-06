package util;

import log.Logger;
import objects.Player;

/**
 * Represents the transposition table for Connect 4.
 */
public class Table {

    /**
     * Major oversight: col = x row = y
     * 
     * i reversed this and it needs to be fixed. RIght now, row is x and col is y
     * 
     * TODO: come up with minimax algorithm design TODO: if design doesn't call for
     * it, delete unnecessary polymorphic methods
     * 
     * TODO: List out all methods, and come up with design for minimax
     * https://en.wikipedia.org/wiki/Minimax
     */


    /**
     * Table that represents the game board being played.
     */
    private int[][] m;

    /**
     * Rows of the game board (represents y)
     */
    private final int rows;

    /**
     * Columns of the game board (represents x)
     */
    private final int cols;

    /**
     * @return number of rows
     */
    public int rows() {
        return rows;
    }

    /**
     * @return number of columns
     */
    public int cols() {
        return cols;
    }

    /**
     * Constructs a table with rows and columns.
     * 
     * @param rows number of rows
     * @param cols number of columns
     */
    public Table(int rows, int cols) {
        if (rows < 4 || cols < 4) {
            throw new IllegalArgumentException("Rows and cols must both be greater than 4");
        }
        this.rows = rows;
        this.cols = cols;
        m = new int[rows][cols];
    }

    /**
     * Gets a value the value at a given game board spot.
     * 
     * @param row row
     * @param col column
     * @return spot value to return
     */
    public int get(int row, int col) {
        return m[row][col];
    }

    /**
     * Sets the value at a given game board spot, along with game board for testing.
     * 
     * @param row row
     * @param col column
     * @param val new value
     * @return table to return
     */
    public Table set(int row, int col, int val) {
        m[row][col] = val;
        return this;
    }

    /**
     * Drops a player into a column by checking if there's a spot to place. If
     * there's no empty spot, leave the board unchanged.
     * 
     * @param col    column to 'drop' piece
     * @param player player to use
     * @return table to return
     */
    public Table drop(int col, int player) {
        if (getRow(col) != -1) {
            set(getRow(col), col, player);
        }
        return this;
    }

    /**
     * Checks if there are any possible moves left.
     * 
     * @return if there are any moves left.
     */
    public boolean movesLeft() {
        for (int col = 0; col < cols(); col++) {
            if (get(0,col) == Player.NOPLAYER) {
                return true;
            }
        }
        return false;
    }
    public int getRow(int col) {
        int rowIndex = -1;
        for (int row = 0; row < rows(); row++) {
            if (get(row,col) == 0) {
                rowIndex = row;
            }
        }
        return rowIndex;
    }

    /**
     * Checks all possible win conditions for a win.
     * 
     * @return
     *         <ul>
     *         <li><b>0</b> no win</li>
     *         <li><b>1</b> player 1 wins</li>
     *         <li><b>2</b> player 2 wins</li>
     *         <li><b>3</b> tie / no left</li>
     *         </ul>
     * 
     */
    public int winning() {
        // optomize for overlaps
        // score every possibility going right
        for (int col = 0; col < cols() - 3; col++) {
            for (int row = 0; row < rows(); row++) {
                int score1 = score(Player.PLAYER1, row, col, 0);
                if (score1 == 8) {
                    return 1;
                } else if (score1 == -8) {
                    return 2;
                }
            }
        }

        // check all possibilites that extend to the left
        for (int col = cols() - 1; col >= 3; col--) {
            for (int row = 0; row < rows(); row++) {
                int score1 = score(Player.PLAYER1, row, col, 1);
                // Logger.log(this, "[" + row + "][" + col + "]: " + score1);
                if (score1 == 8) {
                    return 1;
                } else if (score1 == -8) {
                    return 2;
                }
            }
        }

        // Logger.log(this, "EXTEND DOWN");
        for (int row = 0; row < rows() - 3; row++) {
            for (int col = 0; col < cols(); col++) {
                int score1 = score(Player.PLAYER1, row, col, 3);
                // Logger.log(this, "[" + row + "][" + col + "]: " + score1);
                if (score1 == 8) {
                    return 1;
                } else if (score1 == -8) {
                    return 2;
                }
            }
        }

        // Logger.log(this, "EXTEND UP");
        // check all possibilites that extend up
        for (int row = rows() - 1; row >= 3; row--) {
            for (int col = 0; col <rows(); col++) {
                int score1 = score(Player.PLAYER1, row, col, 2);
                // Logger.log(this, "[" + row + "][" + col + "]: " + score1);
                if (score1 == 8) {
                    return 1;
                } else if (score1 == -8) {
                    return 2;
                }
            }
        }

        // Logger.log(this, "EXTEND DOWN,RIGHT");
        // check all that go diagonal down,right
        for (int row = 0; row < rows() - 3; row++) {
            for (int col = 0; col < cols() - 4; col++) {
                int score1 = score(Player.PLAYER1, row, col, 5);
                // Logger.log(this, "[" + row + "][" + col + "]: " + score1);
                if (score1 == 8) {
                    return 1;
                } else if (score1 == -8) {
                    return 2;
                }
            }
        }

        // Logger.log(this, "EXTEND UP,RIGHT");
        for (int row = rows() - 1; row >= 3; row--) {
            for (int col = 0; col < cols() - 3; col++) {
                int score1 = score(Player.PLAYER1, row, col, 4);
                // Logger.log(this, "[" + row + "][" + col + "]: " + score1);
                if (score1 == 8) {
                    return 1;
                } else if (score1 == -8) {
                    return 2;
                }
            }
        }

        // Logger.log(this, "EXTEND DOWN,LEFT");
        // check all that go diagonal down,left
        for (int row = 0; row < rows() - 3; row++) {
            for (int col = cols() - 1; col >= 3; col--) {
                int score1 = score(Player.PLAYER1, row, col, 7);
                // Logger.log(this, "[" + row + "][" + col + "]: " + score1);
                if (score1 == 8) {
                    return 1;
                } else if (score1 == -8) {
                    return 2;
                }
            }
        }

        // Logger.log(this, "EXTEND UP,LEFT");
        // check all that go diagonal up, left
        for (int row = rows() - 1; row >= 3; row--) {
            for (int col = cols() - 1; col >= 3; col--) {
                int score1 = score(Player.PLAYER1, row, col, 6);
                // Logger.log(this, "[" + row + "][" + col + "]: " + score1);
                if (score1 == 8) {
                    return 1;
                } else if (score1 == -8) {
                    return 2;
                }
            }
        }

        if (!movesLeft()) {
            return 3;
        } else {
            return 0;
        }
    }

    /**
     * Returns the summation score of placing a piece in a certain spot.
     * 
     * @param table   Table to search
     * @param primary use primary board or not
     * @param player  player to favor
     * @param row     row
     * @param col     column
     * @return score
     */
    public int score(int player, int row, int col) {
        int score = 0;


        for (int dir = 0; dir < 7; dir++) {
            if (!((((dir == 0 || dir == 4 || dir == 5) && col + 3 > cols())
                    || ((dir == 1 || dir == 6 || dir == 7) && col - 3 < 0))
                    || (((dir == 3 || dir == 5 || dir == 7) && row + 3 > rows())
                            || ((dir == 2 || dir == 4 || dir == 6) && row - 3 < 0)))) {
                score += score(player, row, col, dir);
            }
        }
        return score;

    }

    /**
     * Scores a row from a table based on a row, column, and direction.</br>
     * 
     * @param t       Table to query
     * @param primary check table's primary matrix or secondary matrix
     * @param player  player to preference
     * @param row     row
     * @param col     col
     * @param dir     direction.
     *                <ul>
     *                <li>0 - right</li>
     *                <li>1 - left</li>
     *                <li>2 - up</li>
     *                <li>3 - down</li>
     *                <li>4 - right, up</li>
     *                <li>5 - right, down</li>
     *                <li>6 - left, up</li>
     *                <li>7 - left, down</li>
     *                </ul>
     * @return final score
     */
    public int score(int player, int row, int col, int dir) {
        if (row >= rows() || row < 0 || col >= cols() || col < 0) {
            throw new IllegalArgumentException("Rows and cols must be within the bounds of given table.");
        }
        if (dir > 7 || dir < 0) {
            throw new IllegalArgumentException("Direction must be between 0 - 7");
        }

        if (dir == 0 || dir == 4 || dir == 5) {
            if (col + 3 > cols()) {
                throw new IllegalArgumentException("BAD DIRECTION");
            }
        }

        if (dir == 1 || dir == 6 || dir == 7) {
            if (col - 3 < 0) {
                throw new IllegalArgumentException("BAD DIRECTION");
            }
        }

        if (dir == 2 || dir == 4 || dir == 6) {
            if (row - 3 < 0) {
                throw new IllegalArgumentException("BAD DIRECTION");
            }
        }

        if (dir == 3 || dir == 5 || dir == 7) {
            if (row + 3 > rows()) {
                throw new IllegalArgumentException("BAD DIRECTION");
            }
        }

        int score = 0;
        int opponent = player == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1;

        int row1 = row, row2 = row, row3 = row, col1 = col, col2 = col, col3 = col;

        // go right
        if (dir == 0 || dir == 4 || dir == 5) {
            col1 += 1;
            col2 += 2;
            col3 += 3;
        }

        // go left
        if (dir == 1 || dir == 6 || dir == 7) {
            col1 -= 1;
            col2 -= 2;
            col3 -= 3;
        }

        // go up
        if (dir == 2 || dir == 4 || dir == 6) {
            row1 -= 1;
            row2 -= 2;
            row3 -= 3;
        }

        if (dir == 3 || dir == 5 || dir == 7) {
            row1 += 1;
            row2 += 2;
            row3 += 3;
        }

        if (get(row,col) == opponent) {
            score -= 2;
        } else if (get(row,col) == player) {
            score += 2;
        }

        if (get(row1,col1) == opponent) {
            score -= 2;
        } else if (get(row1,col1) == player) {
            score += 2;
        }

        if (get(row2,col2) == opponent) {
            score -= 2;
        } else if (get(row2,col2) == player) {
            score += 2;
        }

        //Logger.log(t, "dir: " + dir + ", row: " + row + " col: " + col + " => " + row3 + ", " + col3);
        if (get(row3,col3) == opponent) {
            score -= 2;
        } else if (get(row3,col3) == player) {
            score += 2;
        }

        return score;
    }

    @Override
    public String toString() {
        String s = "\n   ";
        for (int col = 0; col < cols; col++) {
            s += col + " ";
        }
        s += "\n";

        for (int row = 0; row < rows; row++) {
            s += "" + row + ": ";
            for (int col = 0; col < cols; col++) {
                s += m[row][col] + " ";
            }
            s += "\n";
        }

        return s;
    }

}