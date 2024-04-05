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

                // Decreasing First Fit
                List<Integer> binsDecreasingFirstFit = decreasing.firstFitDecreasing();
                System.out.println("Decreasing First Fit bins: " + binsDecreasingFirstFit.size());

                // Decreasing Best Fit
                List<Integer> binsDecreasingBestFit = decreasing.bestFitDecreasing();
                System.out.println("Decreasing Best Fit bins: " + binsDecreasingBestFit.size());

                // Decreasing Next Fit
                List<Integer> binsDecreasingNextFit = decreasing.nextFitDecreasing();
                System.out.println("Decreasing Next Fit bins: " + binsDecreasingNextFit.size());

                System.out.println();
            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }
}
