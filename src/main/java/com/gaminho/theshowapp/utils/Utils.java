package com.gaminho.theshowapp.utils;

import java.text.SimpleDateFormat;

public class Utils {

    /**
     * Upper the first char of the given word
     * @param word the given word as String
     * @return the string with first case up
     */
    public static String upCaseFirstChar(String word){
        return !Character.isUpperCase(word.charAt(0)) ?
                word.substring(0,1).toUpperCase() + word.substring(1)
                : word;
    }

    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

}
