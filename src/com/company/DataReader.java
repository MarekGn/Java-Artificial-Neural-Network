package com.company;

import java.io.*;
import java.util.*;

public class DataReader {

    private ArrayList<DataSet> DataList;


    DataReader() throws IOException,NullPointerException {
        File file = new File("res/inputData.txt");
        Scanner text = new Scanner(file);
        text.useDelimiter("\\s");
        this.DataList = new ArrayList<DataSet>();

        while (text.hasNext()) {
            DataSet buf = new DataSet();
            buf.setAllInputs(Double.valueOf(text.next()) / 1000, Double.valueOf(text.next()), Double.valueOf(text.next()) / 3);
            DataList.add(buf);
        }
    }

    public double[] getInput(int i){
        return DataList.get(i).getInputArray();
    }

    public double[] getOutput(int i){
        return DataList.get(i).getOutputArray();
    }

    public int getDataSize(){
        return DataList.size();
    }

    public double getInputCurrent(int i){
        return DataList.get(i).getInputCurrent();
    }

    public double getInputTemperatur(int i){
        return DataList.get(i).getInputTemperatur();
    }

    public double getOutputVoltage(int i){
        return DataList.get(i).getOutputVoltage();
    }

    private class DataSet{
        private double[] inputArray = new double[2];
        private double[] outputArray= new double[1];

        public double[] getInputArray() {
            return inputArray;
        }

        public double[] getOutputArray() {
            return outputArray;
        }

        public double getInputTemperatur() {
            return inputArray[0];
        }

        public double getInputCurrent() {
            return inputArray[1];
        }

        public double getOutputVoltage() {
            return outputArray[0];
        }

        public void setAllInputs(double in1, double in2, double out1){
            inputArray[0] = in1;
            inputArray[1] = in2;
            outputArray[0] = out1;
        }
    }


}

