package TabuSearch;

import BPP.BPPDatasetParser;
import BPP.BPPInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabuSearch {
    private List<List<Integer>> bestSolution;
    private List<List<Integer>> currentSolution;
    private List<List<List<Integer>>> tabuList;
    private int bestFitness;
    private List<Integer> itemWeights;
    private List<Integer> itemCounts;
    private int binCapacity;
    private int numIterations;
    private int tabuTenure;
    private List<Integer> items;

    public TabuSearch(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity, int numIterations, int tabuTenure){
        this.itemWeights = itemWeights;
        this.itemCounts = itemCounts;
        this.binCapacity = binCapacity;
        this.numIterations = numIterations;
        this.tabuTenure = tabuTenure;

        // Set items list with itemWeights of itemCounts
        items = new ArrayList<>();
        for (int i = 0; i < itemWeights.size(); i++) {
            for (int j = 0; j < itemCounts.get(i); j++){
                items.add(itemWeights.get(i));
            }
        }
        bestSolution = new ArrayList<>();
        currentSolution = new ArrayList<>();
        tabuList = new ArrayList<>();
        bestFitness = items.size();
    }

    public void tabuSearch() {
        // Initialize the currentSolution and bestSolution
        for (int item : items){
            List<Integer> temp = new ArrayList<>(){{
                add(item);
            }};
            currentSolution.add(temp);
            bestSolution.add(temp);
        }

        // Add currentSolution into the tabuList
        tabuList.add(currentSolution);

        // Tabu Search
        for (int iter = 0; iter < numIterations; iter++) {

            // Generate neighboring solutions
            List<List<List<Integer>>> neighbors = generateNeighbors();

            // Find the best neighbor solution
            List<List<Integer>> bestNeighbor = null;
            int bestNeighborFitness = Integer.MAX_VALUE;
            for (List<List<Integer>> neighbor : neighbors) {
                int fitness = evaluateFitness(neighbor);
                if (!tabuList.contains(neighbor) && fitness < bestNeighborFitness) {
                    bestNeighbor = neighbor;
                    bestNeighborFitness = fitness;
                }
            }

            // Update the current solution and tabu list
            currentSolution = bestNeighbor;
            tabuList.add(bestNeighbor);
            if (tabuList.size() > tabuTenure) {
                tabuList.remove(0);
            }

            // Update the best solution
            if (bestNeighborFitness < bestFitness) {
                bestSolution = bestNeighbor;
                bestFitness = bestNeighborFitness;
            }

        }
    }
    public int getBestFitness(){return bestFitness;}
    private List<List<List<Integer>>> generateNeighbors() {
        List<List<List<Integer>>> neighbors = new ArrayList<>();

        // Iterate over each bin in the solution
        for (int i = 0; i < currentSolution.size(); i++) {
            List<Integer> bin = currentSolution.get(i);

            // Iterate over each item in the current bin
            for (int j = 0; j < bin.size(); j++) {
                int currentItem = bin.get(j);

                // Try moving the current item to other bins
                for (int k = 0; k < currentSolution.size(); k++) {
                    if (i == k) continue; // Skip the same bin

                    List<List<Integer>> neighbor = new ArrayList<>(currentSolution); // Create a copy of the current solution

                    // Add the current item to the target bin if its weight doesn't exceed the bin capacity
                    if (getBinWeight(neighbor.get(k)) + currentItem <= binCapacity) {
                        neighbor.get(k).add(currentItem); // Add the item to the target bin

                        // Remove the item from the current bin if it's successfully added to the target bin
                        if (!bin.isEmpty()) {
                            neighbor.get(i).remove(j);
                        }

                        // Add the neighbor solution to the list if it's not in the tabu list
                        if (!tabuList.contains(neighbor)) {
                            neighbors.add(neighbor);
                        }
                    }
                }
            }
        }

        return neighbors;
    }
    private int getBinWeight(List<Integer> bin) {
        int totalWeight = 0;
        for (int item : bin) {
            totalWeight += item;
        }
        return totalWeight;
    }
    private int evaluateFitness(List<List<Integer>> solution) {
        // Count the number of bins
        return solution.size();
    }
}

