package com.example;

import java.util.ArrayList;

public class numQuestion extends Question{
    ArrayList<Integer> lower = new ArrayList<>();
    ArrayList<Integer> upper = new ArrayList<>();
    ArrayList<Integer> coderW = new ArrayList<>();
    ArrayList<Integer> gamerW = new ArrayList<>();

    public void addRange(int l, int u, int c, int g) {
        lower.add(l);
        upper.add(u);
        coderW.add(c);
        gamerW.add(g);

        if (c > 0){
            maxCoderScore += c;
        }
        if (g > 0){
            maxGamerScore += g;
        }
    }

    public void showQuestion(){
        System.out.println(questionText);
    }

    public int[] getScoreDelta(String userAnswer){
        try {
            int v = Integer.parseInt(userAnswer);
            for (int i = 0; i < lower.size(); i++){
                if (v >= lower.get(i) && v <= upper.get(i)) {
                    return new int[]{coderW.get(i), gamerW.get(i)};
                }
            }
        } catch (Exception e) {}
        return new int[]{0, 0};
    }
}
