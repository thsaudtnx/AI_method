package SimulatedAnnealing;

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

                SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(itemWeights, itemCounts, binCapacity);
                simulatedAnnealing.simulatedAnnealing();

                System.out.println();
            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }
}
