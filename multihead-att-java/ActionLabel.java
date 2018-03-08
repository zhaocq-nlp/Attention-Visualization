import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class ActionLabel extends JLabel {

    private boolean isActive = false;
    private HeatmapPanel parent = null;
    private String currentText = "";
    private int id = -1;

//    private HintPanel hintPanel = null;

    public ActionLabel(HeatmapPanel parent, Integer id, String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        this.parent = parent;
        this.currentText = text;
        this.id = id;
        LabelMouseListener listener = new LabelMouseListener();
        this.addMouseListener(listener);

    }

    @Override
    public void setText(String text) {
        super.setText(text);
        this.currentText = text;
        if (text.length() > 0) {
            isActive = true;
        } else {
            isActive = false;
        }
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
//        if(hintPanel == null){
//            hintPanel = new HintPanel(x - 50, (int) y - 25, 200, 30);
//        }
    }

    private class HintPanel extends JPanel{

        JLabel label = new JLabel("", JLabel.CENTER);
        public HintPanel(int x, int y, int width, int height){
            super();
            super.setBounds(x, y, width, height);
            super.add(label);
            label.setBounds(0, 0, width, height);
            label.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        }

        public void setText(String text){
            label.setText(text);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2d.setColor(Color.YELLOW);
            g2d.fill(getBounds());
            g2d.dispose();
        }
    }

    private class LabelMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (isActive) {
                parent.setWordIndex(id);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (isActive) {
//                if (hintPanel != null) {
//                    parent.add(hintPanel);
//                    parent.flushPanel();
//                }
                ((JLabel) e.getComponent()).setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
                ((JLabel) e.getComponent()).setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (isActive) {
//                parent.remove(hintPanel);
//                parent.flushPanel();
                ((JLabel) e.getComponent()).setBorder(null);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (isActive) {
                ((JLabel) e.getComponent()).setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (isActive) {
                ((JLabel) e.getComponent()).setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
            }
        }
    }

}
