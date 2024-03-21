package GeneticAlgorithm;

import BPP.BPPDatasetParser;
import BPP.BPPInstance;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GeneticAlgoSimulator {

    private static int capacity;
    private static int no_items;
    private static int items[];
    private static int population_size;
    private static int generation;
    private static int global_minimum_bins;

    public static void main(String[] args) {
        BPPDatasetParser parser = new BPPDatasetParser();
        List<BPPInstance> instances = parser.parseBPPInstances();

        if (instances != null) {
            for (BPPInstance instance : instances) {
                System.out.println("Problem Name: " + instance.getProblemName());

                List<Integer> itemWeights = instance.getItemWeights();
                List<Integer> itemCounts = instance.getItemCounts();
                int binCapacity = instance.getBinCapacity();

                //run my genetic algorithm here
                geneticAlgorithm(itemWeights, itemCounts, binCapacity);


                System.out.println();
            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }
}