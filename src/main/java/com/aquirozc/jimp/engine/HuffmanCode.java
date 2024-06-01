package com.aquirozc.jimp.engine;

import com.aquirozc.jimp.data.HuffmanImage;
import com.aquirozc.jimp.data.HuffmanNode;

import java.util.BitSet;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.stream.IntStream;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class HuffmanCode {

    public static HuffmanImage encodeImage(Image img){

        int w = (int) img.getWidth(); int h = (int)img.getHeight();
        int[][] img_gray = new int[w][h];
        Map<Integer,Integer> freq = new HashMap<>();

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {

                int brightness = img.getPixelReader().getArgb(i, j) & 0xFF;

                img_gray[i][j] = brightness;

                freq.put(brightness, freq.getOrDefault(brightness, 0) + 1);

            });
        });

        Map<Integer,String> code = new HashMap<>();
        HuffmanNode root = getTreeRoot(freq);
        
        genHuffmanCode(root, "", code);
        

        StringBuilder payload = new StringBuilder();

        long l = System.currentTimeMillis();
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int brightness = img_gray[i][j];
                payload.append(code.get(brightness));
            }  
        }

        System.out.println(System.currentTimeMillis() - l);


        String binaryString = payload.toString();
        BitSet binaryPayload = new BitSet(binaryString.length());

        for (int i = 0; i < binaryString.length(); i++) {
            binaryPayload.set(i, binaryString.charAt(i) == '1');
        }

        return new HuffmanImage(freq,binaryPayload , w, h);

    }

    public static Image decodeImage(HuffmanImage src){

        int aa = 0;
        HuffmanNode root = getTreeRoot(src.freq());
        HuffmanNode current = root;

        int i = 0; int j = 0;
        int w = src.w(); int h = src.h();
        WritableImage img = new WritableImage(w,h);

        while (true) {

            if(!src.payload().get(aa++)){
                current = current.left();
            }else{
                current = current.right();
            }

            if (isLeaf(current)){

                int brightness = current.value();
                img.getPixelWriter().setArgb(i, j, (255 << 24) | (brightness << 16) | (brightness << 8) | brightness);
                j++;

                if (j == h){
                    j = 0;
                    i++;
                }

                current = root;
            }

            if(aa >= src.payload().length()){
                break;
            }
            
        }

        return img;

    }

    private static boolean isLeaf(HuffmanNode n){
        return n.left() == null && n.right() == null;
    }

    private static void genHuffmanCode(HuffmanNode root, String payload, Map<Integer,String> code){

        if (root == null){
            return;
        }

        if (isLeaf(root)){
            code.put(root.value(), payload);
            return;
        }

        genHuffmanCode(root.left(), payload +  "0", code);
        genHuffmanCode(root.right(), payload +  "1",code);

    }

    private static HuffmanNode getTreeRoot(Map<Integer,Integer> freq){

        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(HuffmanNode::compare);

        freq.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(e -> new HuffmanNode(e.getKey(), e.getValue(), null, null) )
                .forEach(n -> queue.add(n));

        while(queue.size() > 1){
            HuffmanNode l = queue.poll();
            HuffmanNode r = queue.poll();
            queue.add(new HuffmanNode(-1, l.freq() + r.freq(), l, r));
        }

        return queue.poll();

    }
    
}
