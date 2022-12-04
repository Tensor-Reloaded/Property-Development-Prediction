package com.example.propertypredictionbackend.utils;

public class SingletonProvider {

    private static SingletonProvider INSTANCE = null;
    private final ImageUtils imageUtils = new ImageUtils();

    private SingletonProvider(){

    }

    private static synchronized SingletonProvider getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SingletonProvider();
        }
        return INSTANCE;
    }

    public static ImageUtils getImageUtils() {
        return getInstance().imageUtils;
    }
}