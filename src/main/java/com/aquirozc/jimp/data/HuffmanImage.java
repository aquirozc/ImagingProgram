package com.aquirozc.jimp.data;

import java.util.Map;
import java.io.Serializable;

public class HuffmanImage implements Serializable {

    private final Map<Integer,Integer> freq; 
    private final String payload;
    private final int w;
    private final int h;

    public HuffmanImage(Map<Integer,Integer> freq, String payload, int w, int h){

        this.freq = freq;
        this.payload = payload;
        this.w = w;
        this.h = h;

    }

    public Map<Integer, Integer> freq() {
        return freq;
    }

    public String payload() {
        return payload;
    }

    public int w() {
        return w;
    }

    public int h() {
        return h;
    }
    
}
