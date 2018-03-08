import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class MainFrame extends JDialog {

    // for main panel
    private JTabbedPane mainTabbedPane = new JTabbedPane(JTabbedPane.TOP);

    // for menu
    private JFileChooser fileChooser = new JFileChooser();

    private int panelCount = 0;

    public MainFrame(String name) throws Exception {
        this.setTitle(name);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(800, 600));//setSize();
        this.setVisible(true);
        this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2);
        this.setResizable(true);

        FileFilter attFileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName();
                return f.isDirectory() || name.endsWith(".attention");
            }

            @Override
            public String getDescription() {
                return "*.attention";
            }
        };
        fileChooser.setFileFilter(attFileFilter);
        fileChooser.addChoosableFileFilter(attFileFilter);

        this.createMenuBar();


        Container contentPane = this.getContentPane();
        contentPane.add(this.mainTabbedPane);
        this.flushFrame();
        System.out.println("Finish");
    }

    public void openFile(String filename) {
        try {
            DataObject dataObject = new DataObject(filename);
            HeatmapPanel heatmapPanel = new HeatmapPanel(this, dataObject, 30);
            MainPanel panel = new MainPanel(new JPanel(), dataObject);
            panel.addHeatmapPanel(heatmapPanel);
            this.mainTabbedPane.add(panel, Utils.extractFilePrefix(filename));
            this.mainTabbedPane.setSelectedComponent(panel);
            this.flushFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void flushFrame() {
        this.validate();
        this.invalidate();
        this.repaint();
    }


    private void createMenuBar() {
        JMenu menu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open...");
        menu.add(openItem);
        JMenuBar br = new JMenuBar();
        br.add(menu);

        JMenuItem closeItem = new JMenuItem("Close Tab");
        menu.add(closeItem);

        JMenuItem quitItem = new JMenuItem("Quit");
        menu.add(quitItem);

        closeItem.setEnabled(false);

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int state = fileChooser.showOpenDialog(null);
                if (state == 1) {
                    return;
                } else {
                    String filename = fileChooser.getSelectedFile().getAbsolutePath();
                    openFile(filename);
                    ++panelCount;
                    closeItem.setEnabled(true);
                }
            }
        });

        closeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(closeItem.isEnabled()){
                    mainTabbedPane.remove(mainTabbedPane.getSelectedComponent());
                    --panelCount;
                    if (panelCount == 0){
                        closeItem.setEnabled(false);
                    }
                }

            }
        });

        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        this.setJMenuBar(br);
    }


    public static void main(String[] args) throws Exception {
        new MainFrame("Heatmap");
    }
}
