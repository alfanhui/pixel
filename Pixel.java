import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Math;
import java.math.BigInteger;
import java.text.DecimalFormat;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Pixel extends JComponent {

    public static int di = 128; // dimentions
    public static final int BYTE_ARRAY_SIZE = (di * di) / 8;
    public static Pixel comp;
    public byte[] bytes;
    public static BigInteger permutations = BigInteger.ONE;
    public JLabel percentageCompleteLabel;
    public JLabel permutationLabel;
    public JLabel maxPermutationLabel;
    public static BigInteger maxPermutations = BigInteger.valueOf(2).pow(di * di);

    public Pixel() {
        this.bytes = new byte[BYTE_ARRAY_SIZE];
    }

    @Override
    // Need to add a buffer as this is not actioning correctly because of speed of
    // for loop incrementing byteArray
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        displayByteArray(g);
    }

    public static void main(String[] args) {
        comp = new Pixel();
        comp.setPreferredSize(new Dimension(di, di));
        JFrame testFrame = new JFrame();

        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.getContentPane().add(comp, BorderLayout.NORTH);
        JPanel buttonsPanel = new JPanel();
        JButton newLineButton = new JButton("Permutate");
        JButton clearButton = new JButton("Clear");
        JButton randomiseButton = new JButton("Randomise Sequence");
        buttonsPanel.add(newLineButton);
        buttonsPanel.add(clearButton);
        buttonsPanel.add(randomiseButton);
        testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        JPanel metricPanel = new JPanel();
        comp.percentageCompleteLabel = new JLabel("0.00%");
        comp.permutationLabel = new JLabel("0");
        comp.maxPermutationLabel = new JLabel(maxPermutations.toString(1));
        metricPanel.setLayout(new BoxLayout(metricPanel, BoxLayout.Y_AXIS));
        metricPanel.add(comp.percentageCompleteLabel);
        metricPanel.add(comp.permutationLabel);
        metricPanel.add(comp.maxPermutationLabel);
        testFrame.getContentPane().add(metricPanel, BorderLayout.EAST);

        for (int i = 0; i < BYTE_ARRAY_SIZE; i++) {
            comp.bytes[i] = -128;
        }
        newLineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("PERMA STARTED ");
                new Thread(new Runnable() {
                    public void run() {
                        while (permutations.compareTo(maxPermutations) < 0) {
                            comp.incrementAtIndex(comp.bytes, 0);
                            comp.inferPermutationCount(permutations);
                            comp.permutationLabel.setText(permutations.toString(10));
                        }
                        System.out.println("Finished! permutations: " + permutations.toString(10));
                    }
                }).start();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clear Button Pressed");
                newLineButton.removeAll();
                for (int i = 0; i < BYTE_ARRAY_SIZE; i++) {
                    comp.bytes[i] = -128;
                }
                comp.repaint();
            }
        });

        randomiseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comp.randomise();
                permutations = calculatePermutationNumber(comp.bytes);
                comp.inferPermutationCount(permutations);
                comp.permutationLabel.setText(permutations.toString(10));
                comp.repaint();
            }
        });
        testFrame.pack();
        testFrame.setVisible(true);

    }

    private void randomise() {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Math.floor(Math.random() * (127 - (-128)) - 128);
        }
    }

    public void inferPermutationCount(BigInteger permutations) {
        BigInteger percentageTotal = permutations.multiply(new BigInteger("100")).divide(maxPermutations);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedPercentage = decimalFormat.format(percentageTotal);
        this.percentageCompleteLabel.setText(formattedPercentage + "%");
    }

    public static BigInteger calculatePermutationNumber(byte[] byteArray) {
        BigInteger radix = new BigInteger("256");
        BigInteger permutationNumber = BigInteger.ZERO;
        BigInteger positionMultiplier = BigInteger.ONE;

        for (int i = 0; i < byteArray.length; i++) {
            BigInteger value = BigInteger.valueOf(byteArray[i] + 128);
            permutationNumber = permutationNumber.add(value.multiply(positionMultiplier));
            positionMultiplier = positionMultiplier.multiply(radix);
        }

        return permutationNumber;
    }

    private void incrementAtIndex(byte[] array, int index) {
        if (array[index] == Byte.MAX_VALUE) {
            array[index] = -128;
            if (index < array.length)
                incrementAtIndex(array, index + 1);
        } else {
            repaint();
            array[index]++;
            permutations = permutations.add(BigInteger.ONE);
        }
    }

    public void displayByteArray(Graphics g) {
        int column = 0;
        for (int i = 0, j = 0; i < BYTE_ARRAY_SIZE; i++, j += 8) {
            // If end row, next row
            if (j > (di - 1)) {
                column++;
                j = 0;
            }
            switch (this.bytes[i]) {
                case -0x00000080:
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000007F:
                    g.setColor(Color.black);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000007E:
                    g.setColor(Color.black);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000007D:
                    g.setColor(Color.black);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x0000007C:
                    g.setColor(Color.black);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000007B:
                    g.setColor(Color.black);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000007A:
                    g.setColor(Color.black);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000079:
                    g.setColor(Color.black);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case -0x00000078:
                    g.setColor(Color.black);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000077:
                    g.setColor(Color.black);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000076:
                    g.setColor(Color.black);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000075:
                    g.setColor(Color.black);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x00000074:
                    g.setColor(Color.black);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000073:
                    g.setColor(Color.black);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000072:
                    g.setColor(Color.black);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000071:
                    g.setColor(Color.black);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    break;
                case -0x00000070:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000006F:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000006E:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000006D:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x0000006C:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000006B:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000006A:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000069:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case -0x00000068:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000067:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000066:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000065:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x00000064:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000063:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000062:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000061:
                    g.setColor(Color.black);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    break;
                case -0x00000060:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000005F:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000005E:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000005D:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x0000005C:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000005B:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000005A:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000059:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case -0x00000058:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000057:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000056:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000055:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x00000054:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000053:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000052:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000051:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    break;
                case -0x00000050:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000004F:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000004E:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000004D:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x0000004C:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000004B:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000004A:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000049:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case -0x00000048:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000047:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000046:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000045:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x00000044:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000043:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000042:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000041:
                    g.setColor(Color.black);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    break;
                case -0x00000040:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000003F:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000003E:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000003D:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x0000003C:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000003B:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000003A:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000039:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case -0x00000038:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000037:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000036:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000035:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x00000034:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000033:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000032:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000031:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    break;
                case -0x00000030:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000002F:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000002E:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000002D:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x0000002C:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000002B:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000002A:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000029:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case -0x00000028:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000027:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000026:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000025:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x00000024:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000023:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000022:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000021:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    break;
                case -0x00000020:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000001F:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000001E:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000001D:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x0000001C:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000001B:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000001A:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000019:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case -0x00000018:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000017:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000016:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000015:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x00000014:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000013:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000012:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000011:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    break;
                case -0x00000010:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000000F:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000000E:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000000D:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x0000000C:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x0000000B:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x0000000A:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000009:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case -0x00000008:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000007:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000006:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000005:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case -0x00000004:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000003:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case -0x00000002:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j, column, j, column);
                    break;
                case -0x00000001:
                    g.setColor(Color.black);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 7, column, j + 7, column);
                    break;
                case 0x00000000:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000001:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000002:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000003:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x00000004:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000005:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000006:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000007:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case 0x00000008:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000009:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000000A:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000000B:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x0000000C:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000000D:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000000E:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000000F:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    break;
                case 0x00000010:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000011:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000012:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000013:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x00000014:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000015:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000016:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000017:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case 0x00000018:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000019:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000001A:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000001B:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x0000001C:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000001D:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000001E:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000001F:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    break;
                case 0x00000020:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000021:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000022:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000023:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x00000024:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000025:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000026:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000027:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case 0x00000028:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000029:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000002A:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000002B:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x0000002C:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000002D:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000002E:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000002F:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    break;
                case 0x00000030:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000031:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000032:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000033:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x00000034:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000035:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000036:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000037:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case 0x00000038:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000039:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000003A:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000003B:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x0000003C:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000003D:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000003E:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000003F:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 6, column, j + 6, column);
                    break;
                case 0x00000040:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000041:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000042:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000043:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x00000044:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000045:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000046:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000047:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case 0x00000048:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000049:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000004A:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000004B:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x0000004C:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000004D:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000004E:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000004F:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    break;
                case 0x00000050:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000051:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000052:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000053:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x00000054:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000055:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000056:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000057:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case 0x00000058:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000059:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000005A:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000005B:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x0000005C:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000005D:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000005E:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000005F:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 5, column, j + 5, column);
                    break;
                case 0x00000060:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000061:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000062:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000063:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x00000064:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000065:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000066:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000067:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case 0x00000068:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000069:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000006A:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000006B:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x0000006C:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000006D:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000006E:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000006F:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 4, column, j + 4, column);
                    break;
                case 0x00000070:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000071:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000072:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000073:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x00000074:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000075:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x00000076:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000077:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 3, column, j + 3, column);
                    break;
                case 0x00000078:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x00000079:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000007A:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000007B:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 2, column, j + 2, column);
                    break;
                case 0x0000007C:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000007D:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j, column, j, column);
                    g.setColor(Color.white);
                    g.drawLine(j + 1, column, j + 1, column);
                    break;
                case 0x0000007E:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.setColor(Color.white);
                    g.drawLine(j, column, j, column);
                    break;
                case 0x0000007F:
                    g.setColor(Color.black);
                    g.drawLine(j + 7, column, j + 7, column);
                    g.drawLine(j + 6, column, j + 6, column);
                    g.drawLine(j + 5, column, j + 5, column);
                    g.drawLine(j + 4, column, j + 4, column);
                    g.drawLine(j + 3, column, j + 3, column);
                    g.drawLine(j + 2, column, j + 2, column);
                    g.drawLine(j + 1, column, j + 1, column);
                    g.drawLine(j, column, j, column);
                    break;
                default:
                    System.out.println("Unable to draw");
            }
        }
    }

}