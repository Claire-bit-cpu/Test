package com.example;

public class tfQuestion extends mcQuestion{

    public tfQuestion() {
        options.add("true");
        options.add("false");
    }

    public void setWeights(int ct, int gt, int cf, int gf){
        coderWeights.add(ct);
        gamerWeights.add(gt);
        coderWeights.add(cf);
        gamerWeights.add(gf);

        if (ct > 0) maxCoderScore += ct;
        if (gt > 0) maxGamerScore += gt;
        if (cf > 0) maxCoderScore += cf;
        if (gf > 0) maxGamerScore += gf;
    }

    public void showQuestion(){
        System.out.println(questionText + " (true/false)");
    }
}
