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

    public List<Integer> firstFitDecreasing() {
        // Set items list with itemWeights of itemCounts
        List<Integer> items = generateSortedItems();

        // First Fit Algorithm
        List<Integer> bins = new ArrayList<>();
        for (int item : items) {
            boolean packed = false;
            for (int j = 0; j < bins.size(); j++) {
                if (bins.get(j) + item <= binCapacity) {
                    bins.set(j, bins.get(j) + item);
                    packed = true;
                    break;
                }
            }
            if (!packed) {
                bins.add(item);
            }
        }
        return bins;
    }

    public List<Integer> bestFitDecreasing() {
        // Set items list with itemWeights of itemCounts
        List<Integer> items = generateSortedItems();

        // Best Fit Decreasing
        List<Integer> bins = new ArrayList<>();
        for (int item : items) {
            int minSpaceIndex = -1;
            int minSpace = Integer.MAX_VALUE;
            for (int j = 0; j < bins.size(); j++) {
                int spaceLeft = binCapacity - bins.get(j);
                if (item <= spaceLeft && spaceLeft < minSpace) {
                    minSpace = spaceLeft;
                    minSpaceIndex = j;
                }
            }
            if (minSpaceIndex != -1) {
                bins.set(minSpaceIndex, bins.get(minSpaceIndex) + item);
            } else {
                bins.add(item);
            }
        }
        return bins;
    }

    public List<Integer> nextFitDecreasing() {
        // Set items list with itemWeights of itemCounts
        List<Integer> items = generateSortedItems();

        // Next FitDecreasing
        List<Integer> bins = new ArrayList<>();
        int currentBinSpace = binCapacity;
        for (int item : items) {
            // Check if the item can fit into the current bin
            if (item <= currentBinSpace) {
                currentBinSpace -= item;
            } else {
                // If not, start a new bin
                bins.add(binCapacity - currentBinSpace); // Add the remaining space of the current bin
                currentBinSpace = binCapacity - item; // Start a new bin with the current item's weight
            }
        }
        bins.add(binCapacity - currentBinSpace); // Add the remaining space of the last bin
        return bins;
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
}

