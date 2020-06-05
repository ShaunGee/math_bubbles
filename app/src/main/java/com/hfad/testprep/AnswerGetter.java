package com.hfad.testprep;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//extracts game questions from csv

public class AnswerGetter {
    private Context context;
    //private String fileName;
    private List<String[]> answers;

    public AnswerGetter(Context context) {
        answers=new ArrayList<String[]>();
        this.context = context;
        //this.fileName = fileName;
    }

    public List<String[]> readCsv() throws IOException{
        InputStream input = context.getResources().openRawResource(R.raw.questions_n_answers);
        InputStreamReader inputStreamReader = new InputStreamReader(input);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        String csvSpliter = ",";

        bufferedReader.readLine();

        while ((line = bufferedReader.readLine()) != null){
            String[] row = line.split(csvSpliter);
            answers.add(row);
        }
        fixList(answers);
        return answers;
    }

    private  List<String> fixList(List<String[]> answers){
        List<String> x = new ArrayList<>();;

        for (int i = 0; i<answers.size();i++){
            x.add(Arrays.toString(answers.get(i)).replaceAll("[\\[\\](){}]",""));
        }
        return x;
    }

}