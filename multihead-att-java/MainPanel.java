import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JScrollPane {


    private JPanel mainPanel = null;
    private HeatmapPanel heatmapPanel = null;
    private JComboBox sampleComboBox = null;
    private JComboBox attentionComboBox = null;
    private DataObject dataObject = null;

    private JButton prevButton = null;
    private JButton nextButton = null;

    public MainPanel(JPanel panel, DataObject dataObj) {
        super(panel);
        this.mainPanel = panel;
        this.mainPanel.setPreferredSize(new Dimension(768, 3500));
        this.mainPanel.setLayout(null);
        this.setBounds(0, 0, 200, 200);
        this.setBackground(Color.WHITE);
        this.setOpaque(true);

        this.dataObject = dataObj;
        this.createPopupMenu();
    }

    public void addHeatmapPanel(HeatmapPanel panel) {
        this.mainPanel.add(panel);
        this.heatmapPanel = panel;
        heatmapPanel.currentAttentionName = (String) this.attentionComboBox.getSelectedItem();
        heatmapPanel.display(Integer.parseInt((String) this.sampleComboBox.getSelectedItem()));
    }

    public void flushPanel() {
        this.mainPanel.validate();
        this.mainPanel.invalidate();
        this.mainPanel.repaint();
    }

    public void createPopupMenu() {
        JLabel sampleLabel = new JLabel("Sample: ", JLabel.RIGHT);
        this.mainPanel.add(sampleLabel);
        sampleLabel.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        sampleLabel.setBounds(50, 5, 60, 30);

        this.prevButton = new JButton("prev");
        this.mainPanel.add(this.prevButton);
        this.prevButton.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        this.prevButton.setBounds(185, 5, 60, 30);

        this.nextButton = new JButton("next");
        this.mainPanel.add(this.nextButton);
        this.nextButton.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        this.nextButton.setBounds(250, 5, 60, 30);

        JLabel attLabel = new JLabel("Displaying: ", JLabel.RIGHT);
        this.mainPanel.add(attLabel);
        attLabel.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        attLabel.setBounds(290, 5, 120, 30);
//        }
        this.sampleComboBox = new JComboBox();
        for (int i = 0; i < this.dataObject.numSamples; ++i) {
            this.sampleComboBox.addItem(String.format("%d", i));
        }
        this.mainPanel.add(sampleComboBox);
        this.sampleComboBox.setBounds(110, 5, 70, 30);
        this.sampleComboBox.setSelectedIndex(0);

        this.attentionComboBox = new JComboBox();
        this.mainPanel.add(attentionComboBox);
        for (int i = 0; i < this.dataObject.attentionFieldList.size(); ++i) {
            this.attentionComboBox.addItem(this.dataObject.attentionFieldList.get(i));
        }
        this.attentionComboBox.setBounds(410, 5, 250, 30);
        this.attentionComboBox.setSelectedIndex(0);

        this.sampleComboBox.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        heatmapPanel.display(Integer.parseInt((String) sampleComboBox.getSelectedItem()));
                        flushPanel();
                    }
                });
        this.attentionComboBox.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        heatmapPanel.display((String) attentionComboBox.getSelectedItem());
                        flushPanel();
                    }
                });

        this.prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (heatmapPanel.currentSampleId > 0){
                    int prev = heatmapPanel.currentSampleId - 1;
                    sampleComboBox.setSelectedIndex(prev);
                    heatmapPanel.display(prev);
                    flushPanel();
                }
            }
        });
        this.nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(heatmapPanel.currentSampleId < dataObject.numSamples - 1){
                    int next = heatmapPanel.currentSampleId + 1;
                    sampleComboBox.setSelectedIndex(next);
                    heatmapPanel.display(next);
                    flushPanel();
                }
            }
        });

    }


}
