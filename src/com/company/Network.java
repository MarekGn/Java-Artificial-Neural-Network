package com.company;

public class Network{

    private double[][] output;
    private double[][][] weights;
    private double[][] bias;
    private double[][] error_signal;
    private double[][] output_derivative;

    public final int[] NETWORK_LAYER_SIZES;
    private final int INPUT_SIZE;
    private final int NETWORK_SIZE;
    private final int OUTPUT_SIZE;

    Network(int... NETWORK_LAYER_SIZES) {
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
        this.INPUT_SIZE = NETWORK_LAYER_SIZES[0];
        this.NETWORK_SIZE = NETWORK_LAYER_SIZES.length;
        this.OUTPUT_SIZE = NETWORK_LAYER_SIZES[NETWORK_SIZE-1];

        this.output = new double[NETWORK_SIZE][];
        this.weights = new double[NETWORK_SIZE][][];
        this.bias = new double[NETWORK_SIZE][];
        this.error_signal = new double[NETWORK_SIZE][];
        this.output_derivative = new double[NETWORK_SIZE][];

        for(int i = 0; i<NETWORK_SIZE; i++){
            this.output[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.error_signal[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.output_derivative[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.bias[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i],-1,1);

            if(i>0){
                weights[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i],NETWORK_LAYER_SIZES[i-1],-1,1);
            }
        }
    }

    public double[] calculate(double... input){
        if(input.length != this.INPUT_SIZE) return null;
        this.output[0] = input;
        for(int layer = 1; layer<NETWORK_SIZE; layer++){
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++){
                double sum = bias[layer][neuron];
                for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer-1]; prevNeuron++){
                    sum += output[layer-1][prevNeuron] * weights[layer][neuron][prevNeuron];
                }
                output[layer][neuron] = sigmoid(sum);
                output_derivative[layer][neuron] = 1 / (1+Math.exp(-sum));
            }
        }
        return output[NETWORK_SIZE-1];
    }

    public void train(double[] input, double[] target, double eta){
        if(input.length !=INPUT_SIZE || target.length != OUTPUT_SIZE) return;
        calculate(input);
        backpropError(target);
        updateWeights(eta);
    }

    private void backpropError(double[] target){
        for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[NETWORK_SIZE-1]; neuron++){
            error_signal[NETWORK_SIZE-1][neuron] = (output[NETWORK_SIZE-1][neuron] - target[neuron]) * output_derivative[NETWORK_SIZE-1][neuron];
        }
        for(int layer = NETWORK_SIZE-2; layer > 0; layer--){
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++){
                double sum = 0;
                for(int nextNeuron = 0; nextNeuron < NETWORK_LAYER_SIZES[layer + 1]; nextNeuron++){
                    sum += weights[layer + 1][nextNeuron][neuron] * error_signal[layer + 1][nextNeuron];
                }
                this.error_signal[layer][neuron] = sum * output_derivative[layer][neuron];
            }
        }
    }

    private void updateWeights(double eta){
        for(int layer = 1; layer < NETWORK_SIZE; layer++){
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++){

                double delta = - eta * error_signal[layer][neuron];
                bias[layer][neuron] += delta;

                for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer-1]; prevNeuron++){
                    weights[layer][neuron][prevNeuron] += delta * output[layer-1][prevNeuron];
                }

            }
        }
    }

    private double sigmoid(double x){
        return Math.log10(1 + Math.exp(x));
    }

    public int getOUTPUT_SIZE() {
        return OUTPUT_SIZE;
    }
}
