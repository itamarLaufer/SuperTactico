package com.laufer.itamar.engine;

public class EngineUtils {
    /**
     * Flips to given array in the y axis
     * [[1, 2, 3]] -> [[1,2,3]]
     * [[1, 2], -> [[3, 4],
     * [3, 4]]      [1, 2]]
     * @param arr the array to flip
     */
    public static <T> void flipY(T[][]arr){
        for(int i=0;i<arr.length / 2;i++){
            for(int j=0;j<arr[i].length;j++){
                T tmp = arr[i][j];
                arr[i][j] = arr[arr.length - i - 1][j];
                arr[arr.length - i - 1][j] = tmp;
            }
        }
    }
}
