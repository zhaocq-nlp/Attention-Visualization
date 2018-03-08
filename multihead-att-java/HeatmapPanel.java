
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;


public class HeatmapPanel extends JPanel {

    private MainFrame parent = null;
    private DataObject dataObject = null;
    private int preDefineMaxLength = 50;

    public int currentSampleId = 0;
    public String currentAttentionName = "";
    public int wordIndex = -1;


    // left
    private ArrayList<JLabel> leftLabelList = new ArrayList<JLabel>();
    private ArrayList<JLabel> rightLabelList = new ArrayList<JLabel>();
    private ArrayList<JLabel> leftNumLabelList = new ArrayList<JLabel>();
    private ArrayList<JLabel> rightNumLabelList = new ArrayList<JLabel>();

    public HeatmapPanel(MainFrame parent, DataObject dataObject, int preDefineMaxLength) {
        super();
        this.parent = parent;
        this.dataObject = dataObject;
        this.preDefineMaxLength = preDefineMaxLength;
        this.setBounds(0, 0, 768, 3400);
        this.setLayout(null);
        this.addEmptyLabels();
    }

    public void flushPanel() {
        this.parent.flushFrame();
    }

    private void addEmptyLabels() {
        for (int i = 0; i < this.preDefineMaxLength; ++i) {
            JLabel label = new ActionLabel(this, i, "", JLabel.RIGHT);
            label.setFont(new Font("TimesRoman", Font.PLAIN, 18));
            label.setBounds(100, 50 + i * 20 + 7 * i, 140, 20);
            this.add(label);
            this.leftLabelList.add(label);
        }
        for (int i = 0; i < this.preDefineMaxLength; ++i) {
            JLabel label = new JLabel("", JLabel.LEFT);
            label.setFont(new Font("TimesRoman", Font.PLAIN, 18));
            label.setBounds(500, 50 + i * 20 + 7 * i, 140, 20);
            this.add(label);
            this.rightLabelList.add(label);
        }
        for (int i = 0; i < this.preDefineMaxLength; ++i) {
            JLabel label = new JLabel("", JLabel.RIGHT);
            label.setFont(new Font("TimesRoman", Font.PLAIN, 18));
            label.setBounds(60, 50 + i * 20 + 7 * i, 30, 20);
            this.add(label);
            this.leftNumLabelList.add(label);
        }
        for (int i = 0; i < this.preDefineMaxLength; ++i) {
            JLabel label = new JLabel("", JLabel.LEFT);
            label.setFont(new Font("TimesRoman", Font.PLAIN, 18));
            label.setBounds(640, 50 + i * 20 + 7 * i, 30, 20);
            this.add(label);
            this.rightNumLabelList.add(label);
        }
    }

    public void setWordIndex(int wordIndex) {
        this.wordIndex = wordIndex;
        this.parent.flushFrame();
    }

    public void display(String currentAttentionName) {
        int curPrefixIndex = this.currentAttentionName.indexOf("_attention");
        int nextPrefixIndex = currentAttentionName.indexOf("_attention");
        if (this.currentAttentionName.substring(0, curPrefixIndex).equals(
                currentAttentionName.substring(0, nextPrefixIndex))) {
            this.currentAttentionName = currentAttentionName;
        } else {
            this.currentAttentionName = currentAttentionName;
            this.display(this.currentSampleId);
        }
    }

    public void display(int sampleId) {
        this.currentSampleId = sampleId;
        this.wordIndex = -1;
        String left = "";
        String right = "";
        try {
            JSONObject currentObj = this.dataObject.get(this.currentSampleId);
            String source = currentObj.getString("source");
            String target = currentObj.getString("translation");
            if (this.currentAttentionName.contains("encoder_decoder_attention")) {
                left = target;
                right = source;
            } else if (this.currentAttentionName.contains("encoder_self_attention")) {
                left = source;
                right = source;
            } else if (this.currentAttentionName.contains("decoder_self_attention")) {
                left = target;
                right = target;
            } else {
                System.err.println("Error name with attention");
                System.exit(0);
            }
            if (this.currentAttentionName.contains("decoder_self_attention")) {
                left = "<SOS> " + left;
                right = "<SOS> " + right;
            } else {
                left += " <EOS>";
                right += " <EOS>";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] leftTokens = left.trim().split(" ");
        String[] rightTokens = right.trim().split(" ");
        int auxIndex = this.leftLabelList.size();
        while (auxIndex < leftTokens.length) {
            JLabel label = new ActionLabel(this, auxIndex, "", JLabel.RIGHT);
            label.setFont(new Font("TimesRoman", Font.PLAIN, 18));
            label.setBounds(100, 50 + auxIndex * 20 + 7 * auxIndex, 140, 20);
            this.add(label);
            this.leftLabelList.add(label);

            JLabel numLabel = new JLabel("", JLabel.RIGHT);
            numLabel.setFont(new Font("TimesRoman", Font.PLAIN, 18));
            numLabel.setBounds(60, 50 + auxIndex * 20 + 7 * auxIndex, 30, 20);
            this.add(numLabel);
            this.leftNumLabelList.add(numLabel);
            ++auxIndex;
        }
        auxIndex = this.rightLabelList.size();
        while (auxIndex < rightTokens.length) {
            JLabel label = new JLabel("", JLabel.LEFT);
            label.setFont(new Font("TimesRoman", Font.PLAIN, 18));
            label.setBounds(500, 50 + auxIndex * 20 + 7 * auxIndex, 130, 20);
            this.add(label);
            this.rightLabelList.add(label);

            JLabel numlabel = new JLabel("", JLabel.LEFT);
            numlabel.setFont(new Font("TimesRoman", Font.PLAIN, 18));
            numlabel.setBounds(640, 50 + auxIndex * 20 + 7 * auxIndex, 30, 20);
            this.add(numlabel);
            this.rightNumLabelList.add(numlabel);
            ++auxIndex;
        }
        for (int i = 0; i < this.leftLabelList.size(); ++i) {
            if (i < leftTokens.length) {
                this.leftLabelList.get(i).setText(leftTokens[i]);
                this.leftNumLabelList.get(i).setText(String.format("%d", i));
            } else {
                this.leftLabelList.get(i).setText("");
                this.leftNumLabelList.get(i).setText("");
            }
        }
        for (int i = 0; i < this.rightLabelList.size(); ++i) {
            if (i < rightTokens.length) {
                this.rightLabelList.get(i).setText(rightTokens[i]);
                this.rightNumLabelList.get(i).setText(String.format("%d", i));
            } else {
                this.rightLabelList.get(i).setText("");
                this.rightNumLabelList.get(i).setText("");
            }
        }
//        this.validate();
//        this.invalidate();
//        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (this.wordIndex < 0) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLUE);
//        g2.setColor(Color.CYAN);
        JSONArray attArray = this.dataObject.getAttentionWeight(this.currentSampleId, this.currentAttentionName);
        String attType = this.dataObject.getAttentionType(this.currentSampleId, this.currentAttentionName);
        if (attType.equals("multihead")) {
            double[] accumulateScores = null;
            g2.setStroke(new BasicStroke(3.0f));
            for (int head = 0; head < attArray.length(); ++head) {
                JSONArray att = null;
                try {
                    att = (JSONArray) ((JSONArray) (attArray.get(head))).get(this.wordIndex);
                    if (head == 0) {
                        accumulateScores = new double[att.length()];
                        Arrays.fill(accumulateScores, 0.0);
                    }
                    for (int idx = 0; idx < att.length(); ++idx) {
                        accumulateScores[idx] += att.getDouble(idx);
                        g2.setComposite(AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER, (float) (att.getDouble(idx) * 0.7)));
                        g2.fillRect(500 + head * 20, 50 + 27 * idx, 20, 20);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            double sum = 0.0;
            for (int idx = 0; idx < accumulateScores.length; ++idx) {
                sum += accumulateScores[idx];
            }
            int[] topIndexes = Utils.topIndexes(accumulateScores, 5);
            double multiplier = 0.6 / (accumulateScores[topIndexes[0]] / sum);
            for (int idx = 0; idx < topIndexes.length; ++idx) {
                double prob = accumulateScores[topIndexes[idx]] / sum * multiplier;
                g2.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, (float) (prob)));
                g2.drawLine(240, 60 + this.wordIndex * 27, 500, 60 + topIndexes[idx] * 27);
            }
        } else if (attType.equals("simple")) {
            double[] accumulateScores = null;
            g2.setStroke(new BasicStroke(3.0f));
            JSONArray att = null;
            try {
                att = (JSONArray) (attArray.get(this.wordIndex));
                accumulateScores = new double[att.length()];
                Arrays.fill(accumulateScores, 0.0);
                for (int idx = 0; idx < att.length(); ++idx) {
                    accumulateScores[idx] = att.getDouble(idx);
                    g2.setComposite(AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER, (float) (att.getDouble(idx) * 0.7)));
                    g2.fillRect(500 + 20, 50 + 27 * idx, 20 * 8, 20);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            int[] topIndexes = Utils.topIndexes(accumulateScores, 5);
            double multiplier = 0.6 / accumulateScores[topIndexes[0]];
            for (int idx = 0; idx < topIndexes.length; ++idx) {
                double prob = accumulateScores[topIndexes[idx]] * multiplier;
                g2.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, (float) (prob)));
                g2.drawLine(240, 60 + this.wordIndex * 27, 500, 60 + topIndexes[idx] * 27);
            }
        }
        g.dispose();
    }
}
