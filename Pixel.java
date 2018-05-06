import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Vector;
import java.lang.Math;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Pixel extends JComponent{
    
    public static int di = 900;  //dimentions
    public static final int BYTE_ARRAY_SIZE  = (di*di)/8;
    public static Pixel comp;
    public static final byte[] bytes = new byte[BYTE_ARRAY_SIZE];
    public static double permutations = 4;
    
    @Override
    //Need to add a buffer as this is not actioning correctly because of speed of for loop incrementing byteArray
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        displayByteArray(g);
    }
    
    public static void main(String[] args) {
        comp = new Pixel();
        comp.setPreferredSize(new Dimension(di, di));
        JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.getContentPane().add(comp, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel();
        JButton newLineButton = new JButton("Permutate");
        JButton clearButton = new JButton("Clear");
        JButton randomiseButton = new JButton("Randomise Sequence");
        buttonsPanel.add(newLineButton);
        buttonsPanel.add(clearButton);
        buttonsPanel.add(randomiseButton);
        testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
        
        for(int i = 0; i < BYTE_ARRAY_SIZE ; i++){
            bytes[i] = -128;
        }
        newLineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("PERMA STARTED ");
                new Thread(new Runnable(){
                    public void run(){
                        while(permutations < Math.pow(2, (di*di))){
                            comp.incrementAtIndex(bytes, (bytes.length-1));  
                        }
                        comp.inferPermutationCount(bytes);
                        System.out.println("Finished! permutations: " + permutations);
                    }
                }).start();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clear Button Pressed");
                newLineButton.removeAll();
                for(int i = 0; i < BYTE_ARRAY_SIZE ; i++){
                    bytes[i] = -128;
                }
                comp.repaint();
            }
        });
        
        randomiseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable(){
                    public void run(){
                        for(int i = 0; i < 1000; i++){
                            comp.randomise();
                            comp.repaint();
                            //comp.inferPermutationCount(bytes);  
                            try{
                                Thread.sleep(60);
                            }catch(InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }   
                        }
                    }
                }).start();
            }
        });
        testFrame.pack();
        testFrame.setVisible(true);
        
    }
    
    private void randomise(){
        for(int i = 0; i < bytes.length; i++){
            bytes[i] = (byte)Math.floor(Math.random() * (127 - (-128)) -128);
        }
    } 
    
    private void inferPermutationCount(byte[] array){
        double percentageTotal = 1;
        double p;
        for(int i = 0; i < array.length; i++){
            p = (array[i] + 128);
            p = p/256;
            percentageTotal += p;
        }   
        percentageTotal = percentageTotal/array.length;
        percentageTotal *= 100;  
        System.out.println("Percentage through: " + String.format( "%.2f", percentageTotal) + "%");
    }
    
    private void incrementAtIndex(byte[] array, int index) {
        if (array[index] == Byte.MAX_VALUE) {
            array[index] = 0;
            if(index > 0)
            incrementAtIndex(array, index - 1);
        }
        else {
            repaint();
            array[index]++;
            permutations++;
        }
    }
    
    private void printBinary(){
        System.out.println();
        for(int k = 0; k < BYTE_ARRAY_SIZE; k++){
            System.out.print(Integer.toBinaryString(bytes[k] & 255 | 256).substring(1));
        }
        System.out.println();
        try{
            Thread.sleep(1000);
        }catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void printSwitchCases(){
        byte d = 0;
        ArrayList<String> blacks = new ArrayList();
        ArrayList<String> whites = new ArrayList();
        
        for(int k = 0, j=128; k < 128; k++, j--){
            blacks.clear();
            whites.clear();
            System.out.println("case -" + String.format("0x%08X", j) + ":");
            String bytey = Integer.toBinaryString(d++ & 255 | 256).substring(1);
            if (String.valueOf(bytey.charAt(0)).equals("0")){
                whites.add("g.drawLine(j, column, j, column);");
            }else{
                blacks.add("g.drawLine(j, column, j, column);");
            }
            if (String.valueOf(bytey.charAt(1)).equals("0")){
                whites.add("g.drawLine(j+1, column, j+1, column);");
            }else{
                blacks.add("g.drawLine(j+1, column, j+1, column);");
            }
            if (String.valueOf(bytey.charAt(2)).equals("0")){
                whites.add("g.drawLine(j+2, column, j+2, column);");
            }else{
                blacks.add("g.drawLine(j+2, column, j+2, column);");
            }
            if (String.valueOf(bytey.charAt(3)).equals("0")){
                whites.add("g.drawLine(j+3, column, j+3, column);");
            }else{
                blacks.add("g.drawLine(j+3, column, j+3, column);");
            }
            if (String.valueOf(bytey.charAt(4)).equals("0")){
                whites.add("g.drawLine(j+4, column, j+4, column);");
            }else{
                blacks.add("g.drawLine(j+4, column, j+4, column);");
            }
            if (String.valueOf(bytey.charAt(5)).equals("0")){
                whites.add("g.drawLine(j+5, column, j+5, column);");
            }else{
                blacks.add("g.drawLine(j+5, column, j+5, column);");
            }
            if (String.valueOf(bytey.charAt(6)).equals("0")){
                whites.add("g.drawLine(j+6, column, j+6, column);");
            }else{
                blacks.add("g.drawLine(j+6, column, j+6, column);");
            }
            if (String.valueOf(bytey.charAt(7)).equals("0")){
                whites.add("g.drawLine(j+7, column, j+7, column);");
            }else{
                blacks.add("g.drawLine(j+7, column, j+7, column);");
            }
            
            if(blacks.size() > 0){
                System.out.println("g.setColor(Color.black);");
                for(int blackIndex = 0; blackIndex < blacks.size(); blackIndex++){
                    System.out.println(blacks.get(blackIndex));
                }
            }
            if(whites.size() > 0){
                System.out.println("g.setColor(Color.white);");
                for(int whiteIndex = 0; whiteIndex < whites.size(); whiteIndex++){
                    System.out.println(whites.get(whiteIndex));
                }
            }
            System.out.println("break;");
        }
    }
    
    public void displayByteArray(Graphics g){
        int column = 0;
        for(int i = 0, j = 0; i < BYTE_ARRAY_SIZE; i++, j+=8){
            if(j > (di-1)){
                column++;
                j=0;
            }
            switch(bytes[i]){
                case -0x00000080:
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000007F:
                g.setColor(Color.black);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000007E:
                g.setColor(Color.black);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000007D:
                g.setColor(Color.black);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x0000007C:
                g.setColor(Color.black);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000007B:
                g.setColor(Color.black);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000007A:
                g.setColor(Color.black);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000079:
                g.setColor(Color.black);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case -0x00000078:
                g.setColor(Color.black);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000077:
                g.setColor(Color.black);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000076:
                g.setColor(Color.black);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000075:
                g.setColor(Color.black);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x00000074:
                g.setColor(Color.black);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000073:
                g.setColor(Color.black);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000072:
                g.setColor(Color.black);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000071:
                g.setColor(Color.black);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                break;
                case -0x00000070:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000006F:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000006E:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000006D:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x0000006C:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000006B:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000006A:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000069:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case -0x00000068:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000067:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000066:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000065:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x00000064:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000063:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000062:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000061:
                g.setColor(Color.black);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                break;
                case -0x00000060:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000005F:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000005E:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000005D:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x0000005C:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000005B:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000005A:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000059:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case -0x00000058:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000057:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000056:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000055:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x00000054:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000053:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000052:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000051:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                break;
                case -0x00000050:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000004F:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000004E:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000004D:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x0000004C:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000004B:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000004A:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000049:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case -0x00000048:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000047:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000046:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000045:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x00000044:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000043:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000042:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000041:
                g.setColor(Color.black);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                break;
                case -0x00000040:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000003F:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000003E:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000003D:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x0000003C:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000003B:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000003A:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000039:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case -0x00000038:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000037:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000036:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000035:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x00000034:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000033:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000032:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000031:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                break;
                case -0x00000030:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000002F:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000002E:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000002D:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x0000002C:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000002B:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000002A:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000029:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case -0x00000028:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000027:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000026:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000025:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x00000024:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000023:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000022:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000021:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                break;
                case -0x00000020:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000001F:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000001E:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000001D:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x0000001C:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000001B:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000001A:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000019:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case -0x00000018:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000017:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000016:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000015:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x00000014:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000013:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000012:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000011:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                break;
                case -0x00000010:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000000F:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000000E:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000000D:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x0000000C:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x0000000B:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x0000000A:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000009:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case -0x00000008:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000007:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000006:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000005:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case -0x00000004:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000003:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case -0x00000002:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case -0x00000001:
                g.setColor(Color.black);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j, column, j, column);
                break;
                case 0x00000000:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000001:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000002:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000003:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x00000004:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000005:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000006:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000007:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case 0x00000008:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000009:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000000A:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000000B:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x0000000C:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000000D:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000000E:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000000F:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                break;
                case 0x00000010:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000011:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000012:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000013:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x00000014:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000015:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000016:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000017:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case 0x00000018:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000019:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000001A:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000001B:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x0000001C:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000001D:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000001E:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000001F:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                break;
                case 0x00000020:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000021:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000022:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000023:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x00000024:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000025:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000026:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000027:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case 0x00000028:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000029:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000002A:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000002B:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x0000002C:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000002D:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000002E:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000002F:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                break;
                case 0x00000030:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000031:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000032:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000033:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x00000034:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000035:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000036:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000037:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case 0x00000038:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000039:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000003A:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000003B:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x0000003C:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000003D:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000003E:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000003F:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+1, column, j+1, column);
                break;
                case 0x00000040:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000041:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000042:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000043:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x00000044:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000045:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000046:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000047:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case 0x00000048:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000049:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000004A:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000004B:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x0000004C:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000004D:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000004E:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000004F:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                break;
                case 0x00000050:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000051:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000052:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000053:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x00000054:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000055:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000056:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000057:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case 0x00000058:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000059:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000005A:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000005B:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x0000005C:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000005D:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000005E:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000005F:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+2, column, j+2, column);
                break;
                case 0x00000060:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000061:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000062:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000063:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x00000064:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000065:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000066:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000067:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                break;
                case 0x00000068:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000069:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000006A:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000006B:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x0000006C:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000006D:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000006E:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000006F:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+3, column, j+3, column);
                break;
                case 0x00000070:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.setColor(Color.white);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000071:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000072:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000073:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x00000074:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000075:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x00000076:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000077:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+4, column, j+4, column);
                break;
                case 0x00000078:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.setColor(Color.white);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x00000079:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000007A:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000007B:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+5, column, j+5, column);
                break;
                case 0x0000007C:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.setColor(Color.white);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000007D:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+7, column, j+7, column);
                g.setColor(Color.white);
                g.drawLine(j+6, column, j+6, column);
                break;
                case 0x0000007E:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.setColor(Color.white);
                g.drawLine(j+7, column, j+7, column);
                break;
                case 0x0000007F:
                g.setColor(Color.black);
                g.drawLine(j, column, j, column);
                g.drawLine(j+1, column, j+1, column);
                g.drawLine(j+2, column, j+2, column);
                g.drawLine(j+3, column, j+3, column);
                g.drawLine(j+4, column, j+4, column);
                g.drawLine(j+5, column, j+5, column);
                g.drawLine(j+6, column, j+6, column);
                g.drawLine(j+7, column, j+7, column);
                break;
            }
        }
    }
    
    
}

