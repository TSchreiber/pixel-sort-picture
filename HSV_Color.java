package pixelsortpicture;

import java.awt.Color;

/**
 * @author Daniel Schreiber
 * @date Feb 15, 2017
 */
public class HSV_Color implements Comparable<HSV_Color>{

    public enum ComparisonProperty {HUE, SATURATION, VALUE, VALUE_REVERSE};
    
    public static ComparisonProperty comparisonProperty = ComparisonProperty.VALUE;
    
    double hue;
    double saturation;
    double value;
    
    int rgb;
    
    public HSV_Color(int value) {
        rgb = value;
        Color c = new Color(value);
        double r = c.getRed()/255.0;
        double g = c.getGreen()/255.0;
        double b = c.getBlue()/255.0;
        double cmax = Math.max(r, Math.max(b, g));
        double cmin = Math.min(r, Math.min(b, g));
        double delta = cmax-cmin;
        if (cmax==r) 
            hue = 60*(((g-b)/delta)%6);
        else if (cmax==g)
            hue = 60*(((b-r)/delta)+2);
        else 
            hue = 60*(((r-g)/delta)+4);
        if (cmax==0)
            saturation = 0;
        else
            saturation = delta/cmax;
        this.value = cmax;
    }

    @Override
    public int compareTo(HSV_Color o) {
        if (this.equals(o))
            return 0;
        
        switch (comparisonProperty) {
            case HUE:
                return Double.compare(hue, o.hue);
            case SATURATION:
                return Double.compare(saturation, o.saturation);
            case VALUE:
                return Double.compare(value, o.value);
            case VALUE_REVERSE:
                return Double.compare(o.value, value);
            default:
                return Double.compare(value, o.value);
                
        }
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof HSV_Color)
        {
            HSV_Color hsv = (HSV_Color)other;
            if (hsv.hue!=this.hue)
                return false;
            if (hsv.value!=this.value)
                return false;
            if (hsv.saturation!=this.saturation)
                return false;
            return true;
        }
        else
            return false;
    }
}