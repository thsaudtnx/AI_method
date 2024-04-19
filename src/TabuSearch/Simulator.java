package TabuSearch;

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
                int numIterations = 10;
                int tabuTenure = 10;

                // Tabu search
                TabuSearch tabuSearch = new TabuSearch(itemWeights, itemCounts, binCapacity, numIterations, tabuTenure);
                tabuSearch.tabuSearch();
            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }
}
