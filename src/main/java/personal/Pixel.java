package personal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.lang.Math;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.image.DataBufferByte;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

public class Pixel extends JComponent {

    public static int di = 144; // dimentions
    public static final int BYTE_ARRAY_SIZE = (di * di) / 8;
    public static Pixel comp;
    public byte[] bytes;
    public static BigInteger permutations = BigInteger.ONE;
    public static BigInteger maxPermutations = BigInteger.valueOf(2).pow(di * di);

    private JLabel percentageCompleteLabel;
    private JLabel permutationLabel;
    private JLabel maxPermutationLabel;
    private static boolean useWebcam = false;
    private static boolean usePermutations = false;
    private int index = -2;
    private static Thread webcamThread = null;
    private static Thread permutationThread = null;
    private JButton permutateButton = new JButton("Permutate");
    private JButton clearButton = new JButton("Clear");
    private JButton randomiseButton = new JButton("Randomise Sequence");
    private JButton webcamButton = new JButton("Use Webcam");

    private NumberFormat formatter = new DecimalFormat("0.######E0", DecimalFormatSymbols.getInstance(Locale.ROOT));

    public Pixel() {
        this.bytes = new byte[BYTE_ARRAY_SIZE];
    }

    @Override
    // Need to add a buffer as this is not actioning correctly because of speed of
    // for loop incrementing byteArray
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Display.displayByteArray(g, bytes);
    }

    public static void main(String[] args) {
        comp = new Pixel();
        comp.setPreferredSize(new Dimension(di, di));
        JFrame testFrame = new JFrame();

        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel topPanel = new JPanel();
        JLabel permutationLabel = new JLabel("Permutation Area");
        permutationLabel.setForeground(Color.GREEN);
        topPanel.add(permutationLabel, BorderLayout.CENTER);
        testFrame.getContentPane().add(topPanel, BorderLayout.NORTH);
        comp.setMinimumSize(new Dimension(di, di));
        //Add Permuatation display
        testFrame.getContentPane().add(comp, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(comp.permutateButton);
        buttonsPanel.add(comp.clearButton);
        buttonsPanel.add(comp.randomiseButton);
        buttonsPanel.add(comp.webcamButton);
        testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        JPanel metricPanel = new JPanel();
        comp.percentageCompleteLabel = new JLabel("0.00%");
        comp.permutationLabel = new JLabel("Permuation number: 0");
        comp.maxPermutationLabel = new JLabel("Max Permutations: " + comp.formatter.format(maxPermutations));
        metricPanel.setLayout(new BoxLayout(metricPanel, BoxLayout.Y_AXIS));
        JLabel metricsLabel = new JLabel("Metrics");
        metricsLabel.setForeground(Color.BLUE);
        metricPanel.add(metricsLabel);
        metricPanel.add(comp.percentageCompleteLabel);
        metricPanel.add(comp.permutationLabel);
        metricPanel.add(comp.maxPermutationLabel);
        testFrame.getContentPane().add(metricPanel, BorderLayout.EAST);

        // webcam
        Webcam webcam = Webcam.getDefault();
        Dimension[] dimensions = webcam.getViewSizes();
        webcam.setViewSize(dimensions[0]);
        WebcamPanel webcamPanel = new WebcamPanel(webcam);
        JLabel webcamLabel = new JLabel("Webcam Feed");
        webcamLabel.setForeground(Color.RED);
        webcamPanel.add(webcamLabel, BorderLayout.NORTH);
        webcamPanel.setFPSDisplayed(false);
        webcamPanel.setDisplayDebugInfo(false);
        webcamPanel.setImageSizeDisplayed(false);
        webcamPanel.setMirrored(false);
        webcamPanel.setMaximumSize(new Dimension(di,di));
        webcam.setImageTransformer(new Transformer());
        testFrame.getContentPane().add(webcamPanel, BorderLayout.WEST);

        // Reset image to white.
        for (int i = 0; i < BYTE_ARRAY_SIZE; i++) {
            comp.bytes[i] = -128;
        }

        // Button actions
        comp.permutateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usePermutations = !usePermutations;
                if (!usePermutations && permutationThread != null) {
                    permutationThread.interrupt();
                    permutationThread = null;
                } else {
                    permutationThread = new Thread(new Runnable() {
                        public void run() {

                            if (useWebcam && webcamThread != null) {
                                webcamThread.interrupt();
                                webcamThread = null;

                                if (comp.index == -2) {
                                    permutations = calculatePermutationNumber(comp.bytes);
                                    comp.index = comp.nextArrayToIncrement(permutations);
                                    if (comp.index == -1) {
                                        System.out.println("Index is -1");
                                    }
                                }
                                while (usePermutations) {
                                    comp.incrementAtIndex(comp.bytes, comp.index);
                                    comp.caculatePermutationPercentageComplete(permutations);
                                    comp.permutationLabel
                                            .setText("Permuation number: " + comp.formatter.format(permutations));
                                }

                            } else {
                                while (permutations.compareTo(maxPermutations) < 0 && usePermutations) {
                                    comp.incrementAtIndex(comp.bytes, 0);
                                    comp.caculatePermutationPercentageComplete(permutations);
                                    comp.permutationLabel
                                            .setText("Permuation number: " + comp.formatter.format(permutations));
                                }
                            }
                        }
                    });
                    permutationThread.start();
                }
            }
        });

        comp.clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comp.permutateButton.setText("Permutate");
                try {
                    if (webcamThread != null) {
                        useWebcam = false;
                        webcamThread.interrupt();
                        webcamThread = null;
                    }
                    if (permutationThread != null) {
                        usePermutations = false;
                        permutationThread.interrupt();
                        permutationThread = null;
                    }
                } catch (Exception exception) {
                    // Do nothing
                }
                for (int i = 0; i < BYTE_ARRAY_SIZE; i++) {
                    comp.bytes[i] = -128;
                }
                comp.repaint();
            }
        });

        comp.randomiseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comp.randomise();
                permutations = calculatePermutationNumber(comp.bytes);
                comp.caculatePermutationPercentageComplete(permutations);
                comp.permutationLabel.setText("Permuation number: " + comp.formatter.format(permutations));
                comp.repaint();
            }
        });

        comp.webcamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                useWebcam = !useWebcam;
                if (!useWebcam && webcamThread != null) {
                    webcamThread.interrupt();
                    webcamThread = null;
                    comp.permutateButton.setText("Permutate");
                } else {
                    comp.permutateButton.setText("Permutate Webcam");
                    webcamThread = new Thread(new Runnable() {
                        public void run() {
                            while (useWebcam) {
                                comp.bytes = comp.getWebcamData(webcam.getImage());
                                permutations = calculatePermutationNumber(comp.bytes);
                                comp.caculatePermutationPercentageComplete(permutations);
                                comp.permutationLabel
                                        .setText("Permuation number: " + comp.formatter.format(permutations));
                                comp.repaint();
                            }

                        }
                    });
                    webcamThread.start();
                }

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

    public byte[] getWebcamData(BufferedImage image) {
        byte[] buffer = ByteBuffer.wrap(this.imageToBytes(image)).array();

        byte[] new_buffer = new byte[BYTE_ARRAY_SIZE];
        int i = 0;
        int byte_counter = 0;
        for (byte b : buffer) {
            // No idea why raster to btye arry causes this case, line artifacts else
            if (i == 7) {
                switch (b) {
                    case -1:
                        new_buffer[byte_counter] |= 1 << i;
                        break;
                    case 0:
                        new_buffer[byte_counter] &= ~(1 << i);
                        break;
                    default:
                        System.out.println("error!");
                }
            }
            if (i != 7) {
                switch (b) {
                    case -1:
                        new_buffer[byte_counter] &= ~(1 << i);
                        break;
                    case 0:
                        new_buffer[byte_counter] |= 1 << i;
                        break;
                    default:
                        System.out.println("error!");
                }
            }
            i++;
            if (i > 7) {
                i = 0;
                byte_counter++;
            }
        }
        return new_buffer;
    }

    public byte[] imageToBytes(BufferedImage bi) {
        return ((DataBufferByte) bi.getData().getDataBuffer()).getData();
    }

    public int nextArrayToIncrement(BigInteger currentPermutation) {
        return compareByteArrays(currentPermutation.toByteArray(),
                currentPermutation.add(BigInteger.ONE).toByteArray());
    }

    public int compareByteArrays(byte[] first, byte[] second) {
        if (first.length != second.length) {
            return -1;
        }
        for (int i = 0; i < first.length; i++) {
            if (first[i] != second[i]) {
                return i;
            }
        }
        return -1;
    }

    // TODO fix deciminal
    public void caculatePermutationPercentageComplete(BigInteger permutations) {
        BigInteger percentageTotal = permutations.multiply(new BigInteger("100")).divide(maxPermutations);
        this.percentageCompleteLabel.setText(percentageTotal + "% complete");
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
            if (index < (array.length - 1))
                incrementAtIndex(array, index + 1);
        } else {
            repaint();
            array[index]++;
            permutations = permutations.add(BigInteger.ONE);
        }
    }
}