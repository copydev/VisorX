package com.example.visorx;

public interface Classifier {
    //String name();

    Classification recognize(final float[][][][] pixels);
}

