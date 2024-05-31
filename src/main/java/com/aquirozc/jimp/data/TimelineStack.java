package com.aquirozc.jimp.data;

import java.util.EmptyStackException;
import java.util.stream.IntStream;

public class TimelineStack {

    private int size = 0;
    private final int maxCapacity;
    private final TimelineRecord[] stack;

    public TimelineStack(int maxCapacity){

        if(maxCapacity < 0){
            throw new IllegalArgumentException("Maximum capacity should be greater or equal than 0");
        }

        this.maxCapacity = maxCapacity;
        stack = new TimelineRecord[maxCapacity+1];

    }

    public void add(TimelineRecord record){

        if(size == maxCapacity){
            IntStream.range(0,maxCapacity-1).forEach(i -> stack[i] = stack[i + 1]);
            size--;
        }

        stack[size++] = record;

    }

    public void clear(){
        IntStream.range(0,maxCapacity).forEach(i -> stack[i] = null);
        size = 0;
    }

    public boolean isEmpty(){
        return !(size > 0);
    }

    public TimelineRecord pop(){

        if (this.isEmpty()){
            throw new EmptyStackException();
        }

        TimelineRecord r = stack[--size];
        stack[size] = null;
        return r;

    }
    
    public int size(){
        return size;
    }
    
}
