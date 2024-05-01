package GeneticAlgorithm;

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
                int populationSize = 1000;
                double mutationRate = 0.05;
                double crossoverRate = 0.95;
                int maxGeneration = 100;

                GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
                        itemWeights,
                        itemCounts,
                        binCapacity,
                        populationSize,
                        mutationRate,
                        crossoverRate,
                        maxGeneration
                );

                geneticAlgorithm.geneticAlgorithm();

            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }
}
