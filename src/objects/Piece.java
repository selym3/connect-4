package objects;

import java.awt.Graphics;

public abstract class Piece {

    protected float x, y;
    protected int w, h;

    public Piece(float x, float y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    @Override
    public String toString() {
        return x + ", " + y + ", " + w + ", " + h;
    }

}