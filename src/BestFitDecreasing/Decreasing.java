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

    public void firstFitDecreasing() {
        // Set items list with itemWeights of itemCounts
        List<Integer> items = generateSortedItems();

        // First Fit Algorithm
        List<List<Integer>> bins = new ArrayList<>();
        for (int item : items) {
            boolean packed = false;
            for (List<Integer> bin : bins) {
                if (getBinWeight(bin) + item <= binCapacity) {
                    bin.add(item);
                    packed = true;
                    break;
                }
            }
            if (!packed) {
                List<Integer> bin = new ArrayList<>();
                bin.add(item);
                bins.add(bin);
            }
        }

        // Display the result
        System.out.println("First Fit Decreasing");
        System.out.print("Bins : " + bins.toString());
        System.out.println();
        System.out.println("Fitness : " + bins.size());
        System.out.println();

    }

    public void bestFitDecreasing() {
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

        // Display the result
        System.out.println("Best Fit Decreasing");
        System.out.print("Bins : " + bins.toString());
        System.out.println();
        System.out.println("Fitness : " + bins.size());
        System.out.println();
    }

    public void nextFitDecreasing() {
        // Set items list with itemWeights of itemCounts
        List<Integer> items = generateSortedItems();

        // Next Fit Decreasing
        List<List<Integer>> bins = new ArrayList<>();
        List<Integer> bin = new ArrayList<>();
        for (int item : items) {
            // Check if the item can fit into the current bin
            if (getBinWeight(bin) + item <= binCapacity) {
                bin.add(item);
            } else {
                // If not, start a new bin
                bins.add(bin);
                bin = new ArrayList<>();
                bin.add(item);
            }
        }
        bins.add(bin); // Add the remaining space of the last bin

        // Display the result
        System.out.println("Next Fit Decreasing");
        System.out.print("Bins : " + bins.toString());
        System.out.println();
        System.out.println("Fitness : " + bins.size());
        System.out.println();
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

