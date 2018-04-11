package com.company;

public class NetworkRandomFunctions {

    public static double[] createRandomArray(int size, double lower_bound, double upper_bound){
        if(size < 1){
            return null;
        }
        double[] array = new double[size];
        for(int i = 0; i < size; i++){
            array[i] = randomValue(lower_bound,upper_bound);
        }
        return array;
    }

    public static double[][] createRandomArray(int size_1, int size_2, double bottom_limit, double top_limit){
        if(size_1 < 1 || size_2 < 1){
            return null;
        }
        double[][] array = new double[size_1][size_2];
        for(int i = 0; i < size_1; i++){
            array[i] = createRandomArray(size_2, bottom_limit, top_limit);
        }
        return array;
    }

    private static double randomValue(double lower_bound, double upper_bound){
        return Math.random()*(upper_bound-lower_bound) + lower_bound;
    }
}
