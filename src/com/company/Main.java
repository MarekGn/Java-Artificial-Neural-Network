package com.company;


import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception{
        Network network = new Network(2, 6, 3, 1);
        DataReader buffor = new DataReader();

        for(int i =0; i < 1000; i++) {
            for(int j = 0; j < buffor.getDataSize(); j++)
                network.train(buffor.getInput(j),buffor.getOutput(j), 0.7);
        }
        PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
        List<Double> scores = new ArrayList<Double>();
        for(int i = 0; i < buffor.getDataSize(); i++) {
            double a[] = network.calculate(1.0, buffor.getInputCurrent(i));
            scores.add(a[0]);
        }
        for (Double score : scores) {
            out.println(score * 3);
        }
        System.setOut(out);
    }
}


