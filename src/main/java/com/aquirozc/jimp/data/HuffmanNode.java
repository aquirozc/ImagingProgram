package com.aquirozc.jimp.data;


public record HuffmanNode(int value, int freq, HuffmanNode left, HuffmanNode right ) {

    public static int compare(HuffmanNode a, HuffmanNode b){
        return Integer.compare(a.freq, b.freq);
    }

}
    

