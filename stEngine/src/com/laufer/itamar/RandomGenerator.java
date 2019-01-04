package com.laufer.itamar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class RandomGenerator {
    private List<Integer>numbers;

    public RandomGenerator(int maxLength) {
       numbers = new ArrayList<>(maxLength);
       for(int i=0; i < maxLength; i++){
           numbers.add(i + 1);
       }
        Collections.shuffle(numbers);
    }
    public int getRandom(){
        return numbers.remove(0);
    }
}
