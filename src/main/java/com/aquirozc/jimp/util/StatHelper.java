package com.aquirozc.jimp.util;

import java.util.List;

public class StatHelper {

    public static double getAverage(List<Integer> sample){
        int n = sample.size();
        return sample.stream().mapToDouble(i -> i).reduce(0, (a,b) -> a + b/n);
    }

    public static double getStandardDeviation(List<Integer> sample){

        double n = sample.size();

        int sumX = sample.stream().reduce(0, (a,b) -> a+b);
        int sumX2 = sample.stream().map(i -> i*i).reduce(0, (a,b) -> a+b);

        return Math.sqrt(sumX2/n - Math.pow(sumX/n, 2));

    }

    public static List<Integer> trimSampleRight(List<Integer> sample){
        int n = sample.size();
        return sample.stream().sorted().limit((long)(n - n *  0.7d)).toList();
    }

    public static List<Integer> trimSampleLeft(List<Integer> sample){
        int n = sample.size();
        return sample.stream().sorted().skip((long)(n * 0.3)).toList();
    }
    
}
