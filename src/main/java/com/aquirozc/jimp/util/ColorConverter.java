package com.aquirozc.jimp.util;

public class ColorConverter {

    public static int[] hslToRGB(double h, double s, double l){

        if (s == 0) return new int[]{255,255,255};

        double  q = l < 0.5d ? l * (1 + s) : l + s - l * s;
        double  p = 2 * l - q;
        double r = hueToRGB(p, q, h + 1/3d);
        double g = hueToRGB(p, q, h);
        double b = hueToRGB(p, q, h - 1/3d);

        return new int[]{(int)(r*255),(int)(g*255),(int)(b*255)};

    }


    private static double hueToRGB(double p, double q, double t){
        if (t < 0d) t += 1;
        if (t > 1d) t -= 1;
        if (t < 1/6d) return p + (q - p) * 6 * t;
        if (t < 1/2d) return q;
        if (t < 2/3d) return p + (q - p) * (2/3d - t) * 6d;
        return p;
    }

    
}
