package com.company;

public class Main {

    public static void main(String[] args) throws Exception{

        // creating object operating networks
        NetworkTools trainer = new NetworkTools();

        // training specified networks number from min_layer_num to max_layer_number having neurons in specified range and step
        trainer.mainTrainNetworks(5,1,2,1,7,
                3,1,0,0,0.3,true,true,100,20);

        // training specified network with also specified sizes;
        trainer.mainTrainNetwork(10000,1,1,0.3,true,true,
                100,100,7,7,5,1);
    }
}


