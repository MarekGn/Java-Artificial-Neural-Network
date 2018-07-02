package com.company;

import java.io.*;
import java.util.*;

public class DataReader {

    private ArrayList<DataSet> DataList;
    private double inputs_lower_bound = 0;
    private double tortuosity_upper_bound = 15;
    private double vf_upper_bound = 1;
    private double l_upper_bound = 0.0001200;
    private double tem_upper_bound = 1600;
    private double i_tpb_upper_bound = 9000000000000L;
    private double p_h2_upper_bound = 100000;
    private double eta_b_upper_bound = 0.5;
    private double current_density_upper_bound = 2000;


    DataReader() throws IOException,NullPointerException {
        File file = new File("res/inputData.txt");
        Scanner text = new Scanner(file);
        text.nextLine();
        text.useDelimiter("\\s+");
        this.DataList = new ArrayList<DataSet>();

        while (text.hasNext()) {
            DataSet buffor = new DataSet();
            buffor.setAllInputs(
                    normalizeData(Double.valueOf(text.next()), inputs_lower_bound, tortuosity_upper_bound),
                    normalizeData(Double.valueOf(text.next()), inputs_lower_bound, vf_upper_bound),
                    normalizeData(Double.valueOf(text.next()), inputs_lower_bound, l_upper_bound),
                    normalizeData(Double.valueOf(text.next()), inputs_lower_bound, tem_upper_bound),
                    normalizeData(Double.valueOf(text.next()), inputs_lower_bound, i_tpb_upper_bound),
                    normalizeData(Double.valueOf(text.next()), inputs_lower_bound, p_h2_upper_bound),
                    normalizeData(Double.valueOf(text.next()), inputs_lower_bound, eta_b_upper_bound),
                    normalizeData(Double.valueOf(text.next()), inputs_lower_bound, current_density_upper_bound)
                    );
            DataList.add(buffor);
        }
        text.close();
    }

    private double normalizeData(double number, double lower_bound, double upper_bound){
        return (number-lower_bound) / (upper_bound-lower_bound);

    }

    public static double unnormalizeData(double number, double lower_bound, double upper_bound){
        return number * (upper_bound-lower_bound) + lower_bound;

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

    public double getInputTortuosity(int i){
        return DataList.get(i).getInputTortuosity();
    }

    public double getInputVF(int i){
        return DataList.get(i).getInputVF();
    }

    public double getInputL(int i){
        return DataList.get(i).getInputL();
    }

    public double getInputTem(int i){
        return DataList.get(i).getInputTem();
    }

    public double getInputI_tpb(int i){
        return DataList.get(i).getInputI_tpb();
    }

    public double getInputP_h2(int i){
        return DataList.get(i).getInputP_h2();
    }

    public double getInputEtab(int i){
        return DataList.get(i).getInputEtab();
    }

    public double getCurrentDensity(int i){
        return DataList.get(i).getCurrentDensity();
    }

    public double getInputs_lower_bound() {
        return inputs_lower_bound;
    }

    public double getTortuosity_upper_bound() {
        return tortuosity_upper_bound;
    }

    public double getVf_upper_bound() {
        return vf_upper_bound;
    }

    public double getL_upper_bound() {
        return l_upper_bound;
    }

    public double getTem_upper_bound() {
        return tem_upper_bound;
    }

    public double getI_tpb_upper_bound() {
        return i_tpb_upper_bound;
    }

    public double getP_h2_upper_bound() {
        return p_h2_upper_bound;
    }

    public double getEta_b_upper_bound() {
        return eta_b_upper_bound;
    }

    public double getCurrent_density_upper_bound() {
        return current_density_upper_bound;
    }

    private class DataSet{
        private double[] inputArray = new double[7];
        private double[] outputArray= new double[1];

        public double[] getInputArray() {
            return inputArray;
        }

        public double[] getOutputArray() {
            return outputArray;
        }

        public double getInputTortuosity() {
            return inputArray[0];
        }

        public double getInputVF() {
            return inputArray[1];
        }

        public double getInputL() {
            return inputArray[2];
        }

        public double getInputTem() {
            return inputArray[3];
        }

        public double getInputI_tpb() {
            return inputArray[4];
        }

        public double getInputP_h2() {
            return inputArray[5];
        }

        public double getInputEtab() {
            return inputArray[6];
        }

        public double getCurrentDensity() {
            return outputArray[0];
        }

        public void setAllInputs(double in1, double in2, double in3, double in4, double in5, double in6, double in7,
                                 double out1){
            inputArray[0] = in1;
            inputArray[1] = in2;
            inputArray[2] = in3;
            inputArray[3] = in4;
            inputArray[4] = in5;
            inputArray[5] = in6;
            inputArray[6] = in7;

            outputArray[0] = out1;
        }
    }


}

