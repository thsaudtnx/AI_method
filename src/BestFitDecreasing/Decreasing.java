package BestFitDecreasing;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class Decreasing {
    private final List<Integer> itemWeights;
    private final List<Integer> itemCounts;
    private final int binCapacity;

    public Decreasing(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity){
        this.itemWeights = itemWeights;
        this.itemCounts = itemCounts;
        this.binCapacity = binCapacity;
    }

    public void bestFitDecreasing() {

        // Set the start time
        long startTime = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        // Run garbage collector to free up memory
        runtime.gc();

        // Set items list with itemWeights of itemCounts
        List<Integer> items = generateSortedItems();

        // Best Fit Decreasing
        List<List<Integer>> bins = new ArrayList<>();
        for (int item : items) {
            int minSpaceIndex = -1;
            int minSpace = Integer.MAX_VALUE;
            for (int j = 0; j < bins.size(); j++) {
                int spaceLeft = binCapacity - getBinWeight(bins.get(j));
                if (item <= spaceLeft && spaceLeft < minSpace) {
                    minSpace = spaceLeft;
                    minSpaceIndex = j;
                }
            }
            if (minSpaceIndex != -1) {
                bins.get(minSpaceIndex).add(item);
            } else {
                List<Integer> bin = new ArrayList<>();
                bin.add(item);
                bins.add(bin);
            }
        }

        // Calculate the runtime
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total execution time: " + totalTime + " milliseconds");

        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory: " + memory + " bytes");

        // Display the result
        System.out.println("Bins : " + bins.toString());
        System.out.println("Number of bin used : " + bins.size());

        // Display Wastage
        double meanWastage = calculateMeanWastage(bins);
        System.out.println("Mean wastage per bin: " + String.format("%.2f", meanWastage) + " units");
        System.out.println();
    }
    private double calculateMeanWastage(final List<List<Integer>> solution) {
        int totalWastage = 0;
        for (List<Integer> bin : solution) {
            int binWeight = getBinWeight(bin);
            int binWastage = binCapacity - binWeight;
            totalWastage += binWastage;
        }
        // Calculate the mean wastage by dividing by the number of bins
        double meanWastage = (double) totalWastage / solution.size();
        return meanWastage;
    }
    private List<Integer> generateSortedItems(){
        // Set items list with itemWeights of itemCounts
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < itemWeights.size(); i++) {
            for (int j = 0; j < itemCounts.get(i); j++){
                items.add(itemWeights.get(i));
            }
        }
        // Sort items list in decreasing order
        Collections.sort(items, Collections.reverseOrder());

        return items;
    }
    private int getBinWeight(List<Integer> bin) {
        int totalWeight = 0;
        for (int item : bin) {
            totalWeight += item;
        }
        return totalWeight;
    }
}

