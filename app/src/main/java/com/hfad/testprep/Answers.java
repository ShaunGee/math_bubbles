package com.hfad.testprep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Answers {
    List<String> answerList;
    private String question;
    private String answer;
    private String type;
    private String[] q;

    public Answers(String question) {
        answerList = new ArrayList<>();
        this.question = question.replaceAll("\\[|\\]","");
        breakQuestionUp();
    }

    private void breakQuestionUp(){
        answerList = Arrays.asList(question.split(","));
        answer = answerList.get(1);
        type = answerList.get(2).replace(" ","");
    }

    public String getAnswer(){return answer;}
    //public String getQuestion(){return question;}

    public boolean checkAnswerr(String ans){
        int a = Integer.parseInt(ans.replace(" ",""));
        int b = Integer.parseInt(answer.replace(" ",""));
        boolean check = false;
        if (a==b) {
            check = true;
        }
        return check;
    }
}
