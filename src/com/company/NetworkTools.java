package com.company;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NetworkTools {

    private List<Integer> train_order = IntStream.range(0, 1200).boxed().collect(Collectors.toList());
    private List<Double> networks_mse = new ArrayList<>();
    private List<List<Double>> networks_mse_array = new ArrayList<>();
    private List<Network> networkList = new ArrayList<>();
    private DataReader networkData = new DataReader();

    NetworkTools() throws IOException {
    }

    public static double[] createRandomArray(int size, double lower_bound, double upper_bound){
        if(size < 1){
            return null;
        }
        double[] array = new double[size];
        for(int i = 0; i < size; i++){
            array[i] = randomValue(lower_bound,upper_bound);
        }
        return array;
    }

    public static double[][] createRandomArray(int size_1, int size_2, double bottom_limit, double top_limit){
        if(size_1 < 1 || size_2 < 1){
            return null;
        }
        double[][] array = new double[size_1][size_2];
        for(int i = 0; i < size_1; i++){
            array[i] = createRandomArray(size_2, bottom_limit, top_limit);
        }
        return array;
    }

    private static double randomValue(double lower_bound, double upper_bound){
        return Math.random()*(upper_bound-lower_bound) + lower_bound;
    }

    private void trainNetworks (double eta){

        for(Iterator<Integer> it_num = train_order.iterator(); it_num.hasNext();) {
            int number = it_num.next();
            for (Iterator<Network> it_net = networkList.iterator(); it_net.hasNext();) {
                Network network = it_net.next();
                network.train(networkData.getInput(number),networkData.getOutput(number), eta);
            }
        }
    }

    private void emphasizing_scheme (int additional_emph_num, int emph_repeat, double eta ) {
        for (Iterator<Network> it_net = networkList.iterator(); it_net.hasNext(); ) {

            Network network = it_net.next();

            for (Iterator<List<Double>> it_mse_array = networks_mse_array.iterator(); it_mse_array.hasNext(); ) {
                List<Double> mse_array = it_mse_array.next();
                for (int i = 0; i < emph_repeat; i++) {
                    List<Double> training_array = new ArrayList<>(mse_array);
                    for (int j = training_array.size() - 1; j >= training_array.size() - additional_emph_num; j--) {
                        int max_mse_index = training_array.lastIndexOf(Collections.max(training_array));
                        network.train(networkData.getInput(max_mse_index), networkData.getOutput(max_mse_index), eta);
                        training_array.set(max_mse_index, 0.0);
                    }
                }

            }
        }
    }
    private void check_networks_mse (){
        for (Iterator<Network> it_net = networkList.iterator(); it_net.hasNext(); ) {

            double mse = 0.0;
            Network network = it_net.next();
            List<Double> mse_array = new ArrayList<>();

            for (Iterator<Integer> it_num = train_order.iterator(); it_num.hasNext();) {

                int number =  it_num.next();
                double sum = 0;
                for (int i = 0; i < network.getOUTPUT_SIZE(); i++) {
                    double proper_output = DataReader.unnormalizeData(
                            networkData.getOutput(number)[i],
                            networkData.getInputs_lower_bound(),
                            networkData.getCurrent_density_upper_bound());
                    double network_output = DataReader.unnormalizeData(
                            network.calculate(networkData.getInput(number))[i],
                            networkData.getInputs_lower_bound(),
                            networkData.getCurrent_density_upper_bound());
                    sum += Math.pow(proper_output - network_output, 2);
                }
                mse_array.add(sum / network.getOUTPUT_SIZE());
                mse += mse_array.get(mse_array.size()-1);
            }
            networks_mse_array.add(mse_array);
            networks_mse.add(mse/networkData.getDataSize());
        }
    }

    private boolean are_all_equal_to(int[] value_array, int equal_to){
        for(int i = 1; i<value_array.length-1; i++){
            if(value_array[i] != equal_to){
                return false;
            }
        }
        return true;
    }

    private boolean are_all_lower_than(int[] value_array, int bound){
        for(int i = 1; i<value_array.length-1; i++){
            if(value_array[i] > bound){
                return false;
            }
        }
        return true;
    }

    private List<Network> make_network_array(int networks_num, int min_layer_number, int max_layer_number, int min_neurons_number,
                                           int max_neurons_number, int step) {
        List<Network> networkList = new ArrayList<>();
        max_neurons_number = (max_neurons_number-min_neurons_number)/step*step+min_neurons_number;
        int created_net_num = 0;
        for (int layer_num = min_layer_number; layer_num <= max_layer_number; layer_num++) {
            int[] layer_sizes = new int[layer_num + 2];
            layer_sizes[0] = 7;
            layer_sizes[layer_sizes.length - 1] = 1;
            for (int layer = 1; layer < layer_sizes.length - 1; layer++) {
                layer_sizes[layer] = min_neurons_number;
            }
            for (int i = min_neurons_number; i < Math.pow(max_neurons_number,layer_num); i++) {
                for (int hidden_layer = 1; hidden_layer <= layer_num; hidden_layer++) {
                    if (layer_sizes[hidden_layer] >= max_neurons_number+1) {
                        layer_sizes[hidden_layer] = min_neurons_number;
                        if (hidden_layer + 1 != layer_sizes.length - 1) {
                            layer_sizes[hidden_layer + 1] += step;
                        }
                    }
                }
                for(int b : layer_sizes){
                    System.out.print(" "+b);
                }
                System.out.println();
                if(are_all_equal_to(layer_sizes, max_neurons_number)){
                    if(created_net_num < networks_num){
                        Network network = new Network(layer_sizes);
                        networkList.add(network);
                        created_net_num += 1;
                        break;

                    }else{
                        System.out.println("The " + created_net_num + " have been created");
                        return networkList;
                    }
                }else if(are_all_lower_than(layer_sizes, max_neurons_number)){
                    if(created_net_num < networks_num){
                        Network network = new Network(layer_sizes);
                        networkList.add(network);
                        created_net_num += 1;
                        layer_sizes[1] += step;
                    }else{
                        System.out.println("The " + created_net_num + " have been created");
                        return networkList;
                    }
                }else
                    break;
                }
            }
        System.out.println("The " + created_net_num + " have been created");
        for(Network net : networkList){
            for(int i : (net.NETWORK_LAYER_SIZES)){
                System.out.print(i);
            }
            System.out.println();
        }
    return networkList;
    }

    public void mainTrainNetwork(int max_iterations, double max_mse_to_save, double min_mse, double eta,
                                        boolean shuffle_input, boolean save_mse_data, int additional_emph_num, int emph_repeat,
                                        int... network_layer_sizes) throws Exception{
        networkList.add(new Network(network_layer_sizes));
        for(int current_iteration = 1; current_iteration <= max_iterations; current_iteration++){
            if(shuffle_input) {
                Collections.shuffle(train_order);
            }
            trainNetworks(eta);
            check_networks_mse();
            emphasizing_scheme(additional_emph_num, emph_repeat, eta);
            System.out.println("current iteration is " + current_iteration + " with " + Collections.min(networks_mse) + " mse");
            networks_mse_array.removeAll(networks_mse_array);
            networks_mse.removeAll(networks_mse);
        }
    }

    public void mainTrainNetworks(int networks_num, int min_layer_number, int max_layer_number, int min_neurons_number,
                                         int max_neurons_number, int step, int max_iterations, double max_mse_to_save,
                                         double min_mse, double eta, boolean shuffle_input, boolean save_mse_data,
                                         int additional_emph_num, int emph_repeat){
        for(int current_iteration = 1; current_iteration <= max_iterations; current_iteration++){
            if(shuffle_input) {
                Collections.shuffle(train_order);
            }

            make_network_array(networks_num,min_layer_number,max_layer_number,min_neurons_number,max_neurons_number,step);
            trainNetworks(eta);
            check_networks_mse();
            emphasizing_scheme(additional_emph_num, emph_repeat, eta);
            System.out.println("current iteration is" + current_iteration);
            System.out.println("worst mse is" + Collections.max(networks_mse));
            System.out.println("best mse is" + Collections.min(networks_mse));
            networks_mse_array.removeAll(networks_mse_array);
            networks_mse.removeAll(networks_mse);
        }
    }
}
