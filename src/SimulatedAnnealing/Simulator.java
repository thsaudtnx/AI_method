package SimulatedAnnealing;

import BPP.BPPDatasetParser;
import BPP.BPPInstance;

import java.util.ArrayList;
import java.util.List;


public class Simulator {
    public static void main(String[] args) {
        BPPDatasetParser parser = new BPPDatasetParser();
        List<BPPInstance> instances = parser.parseBPPInstances();

        if (instances != null) {
            for (BPPInstance instance : instances) {
                System.out.println("Problem Name: " + instance.getProblemName());

                List<Integer> itemWeights = instance.getItemWeights();
                List<Integer> itemCounts = instance.getItemCounts();
                int binCapacity = instance.getBinCapacity();

                List<Double> temperatures = new ArrayList<>();
                int n = 10;
                double initialTemp = 10000;
                double coolingRate = 0.8;
                for (int i = 0; i < n; i++) {
                    temperatures.add(initialTemp * Math.pow(coolingRate, i));
                }

                SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(itemWeights, itemCounts, binCapacity, temperatures);
                simulatedAnnealing.simulatedAnnealing();
            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }
}