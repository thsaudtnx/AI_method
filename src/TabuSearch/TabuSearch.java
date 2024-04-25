package TabuSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public void tabuSearch() {

        // Set the start time
        long startTime = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        // Run garbage collector to free up memory
        runtime.gc();

        // Initialize the bestSolution and tabuList
        Solution initialSolution = generateInitialSolution();
        Solution bestSolution = initialSolution;
        List<Solution> tabuList = new ArrayList<>();
        tabuList.add(initialSolution);

        // Display the initial solution
        // System.out.println("Initial Solution");
        // System.out.println("Bins : " + initialSolution.bins.toString());
        // System.out.println("Fitness : " + initialSolution.fitness);
        // System.out.println();

        // Tabu Search
        for (int iteration = 0; iteration < numIterations; iteration++) {
            // Generate neighboring solutions
            List<Solution> neighbors = generateNeighbors(bestSolution);

            // Display the neighbors
            // System.out.println("Iteration " + iteration);
            // for (int i=0;i<neighbors.size();i++){
            //     System.out.println("Neighbor " + i + " " + neighbors.get(i).bins.toString());
            // }
            // System.out.println();

            // Find the best neighbor that is not in the tabu list
            Solution bestNeighbor = null;
            int bestNeighborFitness = Integer.MAX_VALUE;
            for (Solution neighbor : neighbors) {
                if (!tabuList.contains(neighbor) && neighbor.fitness < bestNeighborFitness) {
                    bestNeighbor = neighbor;
                    bestNeighborFitness = neighbor.fitness;
                }
            }

            // Update the best solution
            if (bestNeighborFitness < bestSolution.fitness) {
                bestSolution = bestNeighbor;
            }

            // Update the tabu list
            tabuList.add(bestNeighbor);
            if (tabuList.size() > tabuTenure) {
                tabuList.remove(0);
            }

            // Display the best neighbor and tabu list
            // System.out.println("Tabu list : ");
            // for (int i=0;i<tabuList.size();i++){
            //     System.out.print(tabuList.get(i).bins.toString());
            // }
            // System.out.println();
            // System.out.println("Best Neighbor");
            // System.out.println("Bins : " + bestNeighbor.bins.toString());
            // System.out.println("Fitness : " + bestNeighbor.fitness);
            // System.out.println();
        }

        // Calculate the runtime
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total execution time: " + totalTime + " milliseconds");

        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory: " + memory + " bytes");

        // Display the result
        System.out.println("Bin : " + bestSolution.bins.toString());
        System.out.println("Number of bins used : " + bestSolution.fitness);
        System.out.println();
    }
    private List<Solution> generateNeighbors(Solution solution) {
        List<Solution> neighbors = new ArrayList<>();

        // Iterate over each bin in the solution
        for (int i = 0; i < solution.bins.size(); i++) {
            List<Integer> bin = solution.bins.get(i);

            // Iterate over each item in the current bin
            for (int j = 0; j < bin.size(); j++) {
                int currentItem = bin.get(j);

                // Try moving the current item to other bins
                for (int k = 0; k < solution.bins.size(); k++) {
                    if (i == k) continue; // Skip the same bin

                    // Create a deep copy of the current solution
                    List<List<Integer>> bins = new ArrayList<>();
                    for (List<Integer> innerList : solution.bins) {
                        List<Integer> binCopy = new ArrayList<>(innerList);
                        bins.add(binCopy);
                    }

                    // Add the current item to the target bin if its weight doesn't exceed the bin capacity
                    if (getBinWeight(bins.get(k)) + currentItem <= binCapacity) {
                        bins.get(k).add(currentItem); // Add the item to the target bin
                        bins.get(i).remove(j); // Remove the item

                        // Remove the empty bin
                        if (bins.get(i).isEmpty()){
                            bins.remove(i);
                        }

                        // Add the neighbor into the neighbors
                        Solution neighbor = new Solution(bins);
                        neighbors.add(neighbor);
                    }
                }
            }
        }

        return neighbors;
    }
    private Solution generateInitialSolution() {
        // Implement the logic to generate an initial solution
        // Set items list with itemWeights of itemCounts
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < itemWeights.size(); i++) {
            for (int j = 0; j < itemCounts.get(i); j++){
                items.add(itemWeights.get(i));
            }
        }

        // Initialize the visited array
        List<Boolean> visited = new ArrayList();
        for (int i=0;i<items.size();i++){
            visited.add(false);
        }

        // Create bins with random
        Random random = new Random();
        List<List<Integer>> bins = new ArrayList<>();
        List<Integer> firstBin = new ArrayList<>();
        bins.add(firstBin);
        int binIndex = 0;
        for (int i=0;i<items.size();i++) {
            while (true) {
                int itemIndex = random.nextInt(items.size());
                if (!visited.get(itemIndex)){
                    visited.set(itemIndex, true);
                    if (getBinWeight(bins.get(binIndex)) + items.get(itemIndex) <= binCapacity){
                        bins.get(binIndex).add(items.get(itemIndex));
                    } else {
                        binIndex += 1;
                        List<Integer> bin = new ArrayList<>();
                        bin.add(items.get(itemIndex));
                        bins.add(bin);
                    }
                    break;
                }
            }
        }

        Solution solution = new Solution(bins);

        return solution;
    }
    private int getBinWeight(List<Integer> bin) {
        int totalWeight = 0;
        for (int item : bin) {
            totalWeight += item;
        }
        return totalWeight;
    }
    private class Solution {
        private List<List<Integer>> bins;
        private int fitness;
        public Solution(List<List<Integer>> bins){
            this.bins = bins;
            this.fitness = bins.size();
        }
    }
}

