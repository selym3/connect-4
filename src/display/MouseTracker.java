package display;

import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.MouseInputAdapter;

public class MouseTracker extends MouseInputAdapter {

    /* MouseEvent.BUTTON1
    MouseEvent.BUTTON2
    MouseEvent.BUTTON3*/

    private final Set<Integer> buttons;
    private float x, y;

    public MouseTracker() {
        buttons = new HashSet<Integer>();
    }

    public boolean get(int e) {
        return buttons.contains(e);
    }

    public boolean down() {
        return buttons.size() > 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons.add(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons.remove(e.getButton());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

}
