package GeneticAlgorithm;

import BPP.BPPDatasetParser;
import BPP.BPPInstance;

import java.util.List;

import static GeneticAlgorithm.GeneticAlgorithm.geneticAlgorithm;


public class GeneticAlgoSimulator {
    public static void main(String[] args) {
        BPPDatasetParser parser = new BPPDatasetParser();
        List<BPPInstance> instances = parser.parseBPPInstances();

        if (instances != null) {
            for (BPPInstance instance : instances) {
                System.out.println("Problem Name: " + instance.getProblemName());

                List<Integer> itemWeights = instance.getItemWeights();
                List<Integer> itemCounts = instance.getItemCounts();
                int binCapacity = instance.getBinCapacity();

<<<<<<< HEAD
                //run my genetic algorithm here
                //geneticAlgorithm(itemWeights, itemCounts, binCapacity);
=======
                geneticAlgorithm(itemWeights, itemCounts, binCapacity);
>>>>>>> e84ca6fd9a6f981d1ab6842c7f68896c7d25ec75

                System.out.println();
            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }
}
