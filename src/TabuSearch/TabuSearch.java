package TabuSearch;

import BPP.BPPDatasetParser;
import BPP.BPPInstance;

import java.util.ArrayList;
import java.util.List;

public class TabuSearch {
    public static void main(String[] args) {
        BPPDatasetParser parser = new BPPDatasetParser();
        List<BPPInstance> instances = parser.parseBPPInstances();

        if (instances != null) {
            for (BPPInstance instance : instances) {
                System.out.println("Problem Name: " + instance.getProblemName());

                List<Integer> itemWeights = instance.getItemWeights();
                List<Integer> itemCounts = instance.getItemCounts();
                int binCapacity = instance.getBinCapacity();

                int numIterations = 10;
                int tabuTenure = 10;

                // Decreasing First Fit
                List<List<Integer>> tabuSearchBins = tabuSearch(itemWeights, itemCounts, binCapacity, numIterations, tabuTenure);
                System.out.println("Tabu search bins: " + tabuSearchBins.size());

                System.out.println();
            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }
    public static List<List<Integer>> tabuSearch(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity, int numIterations, int tabuTenure) {
        // Set items list with itemWeights of itemCounts
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < itemWeights.size(); i++) {
            for (int j = 0; j < itemCounts.get(i); j++){
                items.add(itemWeights.get(i));
            }
        }

        List<List<Integer>> bestSolution = new ArrayList<>();
        List<List<Integer>> currentSolution = new ArrayList<>();
        List<List<List<Integer>>> tabuList = new ArrayList<>();
        int bestFitness = items.size();

        // Initialize the currentSolution
        for (int item : items){
            currentSolution.add(new ArrayList<>() {{
                add(item);
            }});
        }

        // Set currentSolution to the best solution
        bestSolution = currentSolution;

        // Add currentSolution into the tabuList
        tabuList.add(currentSolution);

        for (int iter = 0; iter < numIterations; iter++) {
            // Generate neighboring solutions
            List<List<List<Integer>>> neighbors = generateNeighbors(currentSolution, tabuList, binCapacity);

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

    private static List<List<List<Integer>>> generateNeighbors(List<List<Integer>> solution, List<List<List<Integer>>> tabuList, int binCapacity) {
        List<List<List<Integer>>> neighbors = new ArrayList<>();
        // Implement neighbor generation logic here
        for (int i = 0;i<solution.size();i++){
            for (int j = 0;j<solution.get(i).size(); j++){
                for (int k = 0;k<solution.size();k++){
                    if (i == k) continue;

                    // Neighboring function
                    List<List<Integer>> neighbor = new ArrayList<>(solution);
                    neighbor.get(i).remove(j);
                    int item = solution.get(i).get(j);
                    List<Integer> currentBin = solution.get(k);

                    // Get the sum of the items weight in the current bin
                    int itemsWeight = 0;
                    for (int binItem : currentBin){
                        itemsWeight += binItem;
                    }

                    // Add new neighbor considering weight and tabu list
                    if (itemsWeight + item <= binCapacity){
                        neighbor.get(k).add(item);
                        if (!tabuList.contains(neighbor)){
                            neighbors.add(neighbor);
                        }
                    }

                }
            }
        }

        return neighbors;
    }

    private static int evaluateFitness(List<List<Integer>> solution) {
        // Count the number of bins
        return solution.size();
    }
}

