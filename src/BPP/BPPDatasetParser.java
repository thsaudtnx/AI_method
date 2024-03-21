package BPP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BPPDatasetParser {
    private static final String FILENAME = "src/BPP/BPP.txt";

    public List<BPPInstance> parseBPPInstances() {
        List<BPPInstance> instances = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String problemName = line.trim();
                int numItemWeights = Integer.parseInt(br.readLine().trim());
                int binCapacity = Integer.parseInt(br.readLine().trim());
                List<Integer> itemWeights = new ArrayList<>();
                List<Integer> itemCounts = new ArrayList<>();
                for (int i = 0; i < numItemWeights; i++) {
                    String[] parts = br.readLine().trim().split("\\s+");
                    itemWeights.add(Integer.parseInt(parts[0]));
                    itemCounts.add(Integer.parseInt(parts[1]));
                }
                instances.add(new BPPInstance(problemName, numItemWeights, binCapacity, itemWeights, itemCounts));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return instances;
    }

    public void displayDataset(List<BPPInstance> instances) {
        if (instances != null) {
            for (BPPInstance instance : instances) {
                System.out.println("Problem Name: " + instance.getProblemName());
                System.out.println("Number of Item Weights: " + instance.getNumItemWeights());
                System.out.println("Bin Capacity: " + instance.getBinCapacity());
                System.out.println("Item Weights and Counts:");
                for (int i = 0; i < instance.getNumItemWeights(); i++) {
                    System.out.println(instance.getItemWeights().get(i) + " " + instance.getItemCounts().get(i));
                }
                System.out.println();
            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }

    public static void main(String[] args) {
        BPPDatasetParser parser = new BPPDatasetParser();
        List<BPPInstance> instances = parser.parseBPPInstances();
        parser.displayDataset(instances);
    }
}
