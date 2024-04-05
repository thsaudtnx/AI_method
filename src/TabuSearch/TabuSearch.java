package TabuSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabuSearch {
    private final List<Integer> itemWeights;
    private final List<Integer> itemCounts;
    private final int binCapacity;
    private final int numIterations;
    private final int tabuTenure;

    public TabuSearch(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity, int numIterations, int tabuTenure){
        this.itemWeights = itemWeights;
        this.itemCounts = itemCounts;
        this.binCapacity = binCapacity;
        this.numIterations = numIterations;
        this.tabuTenure = tabuTenure;
    }
    public List<List<Integer>> tabuSearch() {
        // Initialize the currentSolution, bestSolution and tabuList
        List<List<Integer>> items = generateInitialSolution();
        List<List<Integer>> bestSolution = items;
        List<List<Integer>> currentSolution = items;
        List<List<List<Integer>>> tabuList = new ArrayList<>(){{add(items);}};
        int bestFitness = items.size();

        // Tabu Search
        for (int iter = 0; iter < numIterations; iter++) {

            // Generate neighboring solutions
            List<List<List<Integer>>> neighbors = generateNeighbors(currentSolution, tabuList);

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
        return bestSolution;
    }
    private List<List<List<Integer>>> generateNeighbors(List<List<Integer>> solution, List<List<List<Integer>>> tabuList) {
        List<List<List<Integer>>> neighbors = new ArrayList<>();

        // Iterate over each bin in the solution
        for (int i = 0; i < solution.size(); i++) {
            List<Integer> bin = solution.get(i);

            // Iterate over each item in the current bin
            for (int j = 0; j < bin.size(); j++) {
                int currentItem = bin.get(j);

                // Try moving the current item to other bins
                for (int k = 0; k < solution.size(); k++) {
                    if (i == k) continue; // Skip the same bin

                    // Create a deep copy of the current solution
                    List<List<Integer>> neighbor = new ArrayList<>();
                    for (List<Integer> innerList : solution) {
                        List<Integer> innerCopy = new ArrayList<>(innerList);
                        neighbor.add(innerCopy);
                    }

                    // Add the current item to the target bin if its weight doesn't exceed the bin capacity
                    if (getBinWeight(neighbor.get(k)) + currentItem <= binCapacity) {
                        neighbor.get(k).add(currentItem); // Add the item to the target bin
                        neighbor.get(i).remove(j); // Remove the item
                        if (neighbor.get(i).isEmpty()){
                            neighbor.remove(i); // Remove the empty bin
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
    private List<List<Integer>> generateInitialSolution() {
        // Implement the logic to generate an initial solution
        // Set items list with itemWeights of itemCounts
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < itemWeights.size(); i++) {
            for (int j = 0; j < itemCounts.get(i); j++){
                items.add(itemWeights.get(i));
            }
        }

        // Each bin contain each item
        List<List<Integer>> bins = new ArrayList<>();
        for (int item : items){
            bins.add(new ArrayList<>(){{
                add(item);
            }});
        }

        return bins;
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

