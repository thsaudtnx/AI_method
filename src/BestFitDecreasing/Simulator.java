package BestFitDecreasing;

import BPP.BPPDatasetParser;
import BPP.BPPInstance;

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

                // Initialize
                Decreasing decreasing = new Decreasing(itemWeights, itemCounts, binCapacity);

                // Decreasing Algorithms
                //decreasing.firstFitDecreasing();
                decreasing.bestFitDecreasing();
                //decreasing.nextFitDecreasing();
            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }
}
