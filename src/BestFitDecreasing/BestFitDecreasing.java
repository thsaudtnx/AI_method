package BestFitDecreasing;

import BPP.BPPDatasetParser;
import BPP.BPPInstance;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class BestFitDecreasing {

    public static void main(String[] args) {
        BPPDatasetParser parser = new BPPDatasetParser();
        List<BPPInstance> instances = parser.parseBPPInstances();

        if (instances != null) {
            for (BPPInstance instance : instances) {
                System.out.println("Problem Name: " + instance.getProblemName());

                List<Integer> itemWeights = instance.getItemWeights();
                List<Integer> itemCounts = instance.getItemCounts();
                int binCapacity = instance.getBinCapacity();

                // Decreasing First Fit
                List<Integer> binsDecreasingFirstFit = firstFitDecreasing(itemWeights, itemCounts, binCapacity);
                System.out.println("Decreasing First Fit bins: " + binsDecreasingFirstFit.size());

                // Decreasing Best Fit
                List<Integer> binsDecreasingBestFit = bestFitDecreasing(itemWeights, itemCounts, binCapacity);
                System.out.println("Decreasing Best Fit bins: " + binsDecreasingBestFit.size());

                // Decreasing Next Fit
                List<Integer> binsDecreasingNextFit = nextFitDecreasing(itemWeights, itemCounts, binCapacity);
                System.out.println("Decreasing Next Fit bins: " + binsDecreasingNextFit.size());

                System.out.println();
            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }

    public static List<Integer> firstFitDecreasing(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity) {
        // Set items list with itemWeights of itemCounts
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < itemWeights.size(); i++) {
            for (int j = 0; j < itemCounts.get(i); j++){
                items.add(itemWeights.get(i));
            }
        }
        // Sort items list in decreasing order
        Collections.sort(items, Collections.reverseOrder());

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

    public static List<Integer> bestFitDecreasing(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity) {
        // Set items list with itemWeights of itemCounts
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < itemWeights.size(); i++) {
            for (int j = 0; j < itemCounts.get(i); j++){
                items.add(itemWeights.get(i));
            }
        }
        // Sort items list in decreasing order
        Collections.sort(items, Collections.reverseOrder());

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

    public static List<Integer> nextFitDecreasing(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity) {
        // Set items list with itemWeights of itemCounts
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < itemWeights.size(); i++) {
            for (int j = 0; j < itemCounts.get(i); j++){
                items.add(itemWeights.get(i));
            }
        }
        // Sort items list in decreasing order
        Collections.sort(items, Collections.reverseOrder());

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
}

