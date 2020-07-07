package pixelsortpicture;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Daniel Schreiber
 * @date Jul 5, 2020
 * purpose: 
 *  
 */
public class ImagePanel extends JPanel {
    
    private BufferedImage image;
    
    public Dimension size = PixelSortPicture.IMAGE_DIMENSIONS;

    public ImagePanel(String imagePath) throws IOException {
        image = ImageIO.read(new File(imagePath));
        Image scaledImage = image.getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);
        image = toBufferedImage(scaledImage);
    }
    
    public ImagePanel(BufferedImage image) {
        this.image = image;
    }
    
    public HSV_Color[] getPixelColors() {
        int[] pixelValues = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        HSV_Color[] pixels = new HSV_Color[pixelValues.length];
        for (int i=0; i<pixels.length; i++)
            pixels[i] = new HSV_Color(pixelValues[i]);
        return pixels;
    }
    
    @Override
    public void paintComponent(Graphics g){
        g.drawImage(image, 0, 0, size.width, size.height, this);
    }
    
    
    
    // From https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    private static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}