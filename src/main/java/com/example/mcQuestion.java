package com.example;

import java.util.ArrayList;

public class mcQuestion extends Question{
    ArrayList<String> options = new ArrayList<>();
    ArrayList<Integer> coderWeights = new ArrayList<>();
    ArrayList<Integer> gamerWeights = new ArrayList<>();

    public void addOption(String opt, int coder, int gamer){
        options.add(opt);
        coderWeights.add(coder);
        gamerWeights.add(gamer);

        if (coder > 0){
            maxCoderScore += coder;
        }
        if (gamer > 0){
            maxGamerScore += gamer;
        }
    }

    public void showQuestion(){
        System.out.println(questionText);

        String line = "(";
        for (int i = 0; i < options.size(); i++) {
            if (i > 0) line += " / ";
            line += options.get(i);
        }
        line += ")";
        System.out.println(line);
    }

    public int[] getScoreDelta(String userAnswer){
        for (int i = 0; i < options.size(); i++){
            if (options.get(i).equalsIgnoreCase(userAnswer)) {
                return new int[]{coderWeights.get(i), gamerWeights.get(i)};
            }
        }
        return new int[]{0, 0};
    }
}
