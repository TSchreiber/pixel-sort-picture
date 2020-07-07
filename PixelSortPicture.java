package pixelsortpicture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * @author Daniel Schreiber
 * @date Jan 25, 2017
 */
public class PixelSortPicture {
    
    public static final String[] images = {
/* 0  */    "Background-for-Photography-1024x576.jpg",          
/* 1  */    "galaxy.jpg",                                       
/* 2  */    "Prague-street-night-Czech-Republic.jpg",           
/* 3  */    "sunflower.jpg",                                    
/* 4  */    "blue-mountains.jpg",                               
/* 5  */    "red-mountains.jpg",                                
/* 6  */    "black-and-white-forest.jpg",
    };
    public static final String PATH = images[0];
    
    public static final Dimension IMAGE_DIMENSIONS = new Dimension(400, 600);
    public static final int LABEL_HEIGHT = 30;
    public static final int FONT_SIZE = 20;
    
    public static void main(String[] args) throws IOException, InterruptedException {
        // Create the frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(IMAGE_DIMENSIONS.width * 2, IMAGE_DIMENSIONS.height + LABEL_HEIGHT);
        frame.setResizable(false);
        // Create the panel for the original image
        ImagePanel oldImagePanel = new ImagePanel(PATH);
        // Get the pixel array for the image
        HSV_Color[] pixels = oldImagePanel.getPixelColors();
        
        JPanel panels = new JPanel();
        
        new Thread(() -> {
            synchronized (pixels) {
                try {
                    Thread.sleep(1000);
                    while (true) {
                        pixels.wait();
                        BufferedImage newImage = imageFromArray(pixels);
                        ImagePanel imagePanel = new ImagePanel(newImage);
                        panels.remove(1);
                        panels.add(imagePanel);
                        frame.revalidate();
                        frame.repaint();
                        pixels.notify();
                    }
                } catch (InterruptedException ex) {}
            }
        }).start();
        
        
        
        // Sort by each type
        ImagePanel value = sortImage(pixels, HSV_Color.comparisonProperty.VALUE_REVERSE);
        // Create a container for the panels
        panels.setLayout(new BoxLayout(panels, BoxLayout.X_AXIS));
        panels.add(oldImagePanel);
        panels.add(value);
        // Create a container for the labels
        JPanel labels = createLabels();
        // Add the containers to the frame
        frame.add(panels, BorderLayout.CENTER);
        frame.add(labels, BorderLayout.NORTH);
        new Thread(() -> frame.setVisible(true)).start();
    }
    
    private static ImagePanel sortImage(HSV_Color[] pixels, HSV_Color.ComparisonProperty sortProperty) {
        // Sort the pixels
        HSV_Color.comparisonProperty = sortProperty;
        new Thread(() -> {
            Scanner scan = new Scanner(System.in);
            scan.nextLine();
            synchronized (pixels) {
                VisualizeSort.sort(pixels);
            }
        }).start();
        // Convert the pixel array into an image
        BufferedImage newImage = imageFromArray(pixels);
        // Create the panel for the new image
        return new ImagePanel(newImage);
    }
    
    private static BufferedImage imageFromArray(HSV_Color[] pixels) {        
        BufferedImage newImage = new BufferedImage(IMAGE_DIMENSIONS.width, IMAGE_DIMENSIONS.height, BufferedImage.TYPE_INT_RGB);
        for (int y=0; y<IMAGE_DIMENSIONS.height; y++) {
            for (int x=0; x<IMAGE_DIMENSIONS.width; x++) {
                int w = IMAGE_DIMENSIONS.width;
                int color = pixels[y*w + x].rgb;
                newImage.setRGB(x, y, color);
            }
        }
        return newImage;
    }
    
    private static JPanel createLabels() {
        JPanel labels = new JPanel();
        labels.setPreferredSize(new Dimension(IMAGE_DIMENSIONS.width * 2, LABEL_HEIGHT));
        labels.setLayout(new GridLayout(1, 2));
        
        Font font = new Font("Helvetica Neue", Font.PLAIN, FONT_SIZE);
        
        JLabel oLabel = new JLabel("Original");
        oLabel.setHorizontalAlignment(JLabel.CENTER);
        oLabel.setFont(font);
        
        JLabel vLabel = new JLabel("Value");
        vLabel.setHorizontalAlignment(JLabel.CENTER);
        vLabel.setFont(font);
        
        labels.add(oLabel);
        labels.add(vLabel);
        
        return labels;
    }
    
}