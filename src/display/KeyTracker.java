package display;

import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public final class KeyTracker extends KeyAdapter {

    private Set<Integer> keys;

    public KeyTracker() {
        keys = new HashSet<Integer>();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(1);
        keys.add(e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
        keys.remove(e.getKeyCode());
    }

    public boolean get(int keyCode) {
        return keys.contains(keyCode);
    }

    public boolean down() {
        return keys.size() > 0;
    }

}