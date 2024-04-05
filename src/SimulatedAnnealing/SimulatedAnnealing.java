package SimulatedAnnealing;

import java.util.ArrayList;
import java.util.List;

public class SimulatedAnnealing {
    private final List<Integer> itemWeights;
    private final List<Integer> itemCounts;
    private final int binCapacity;

    public SimulatedAnnealing(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity){
        this.itemWeights = itemWeights;
        this.itemCounts = itemCounts;
        this.binCapacity = binCapacity;
    }

    public List<List<Integer>> simulatedAnnealing(){
        // Initialize
        List<List<Integer>> currentSolution = generateInitialSolution();
        List<List<Integer>> bestSolution = currentSolution;
        int currentFitness = evaluateFitness(currentSolution);
        int bestFitness = currentFitness;

        // Implement Simulated Annealing here...
        List<List<List<Integer>>> neighbors = generateNeighbors(currentSolution);

        return null;
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

    private List<List<List<Integer>>> generateNeighbors(final List<List<Integer>> solution) {
        // Implement the logic to generate a neighboring solution
        // For bin packing, this could involve swapping items between bins
        List<List<List<Integer>>> neighbors = new ArrayList<>();
        for (int i=0;i<solution.size();i++){
            List<Integer> currentBin = solution.get(i);

            for (int j=0;j<solution.get(i).size();j++){
                int currentItem = currentBin.get(j);

                for (int k=0;k<solution.size();k++){
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

                        // Add neighbor to the neighbors list
                        neighbors.add(neighbor);
                    }
                }
            }
        }

        // Print the neighbors
        for (int i=0;i<neighbors.size();i++){
            System.out.println("=== Neighbor " + i + " ===");
            for (int j=0;j<neighbors.get(i).size();j++){
                System.out.println(j + "  " + neighbors.get(i).get(j).toString());
            }
        }

        return neighbors;
    }

    private int evaluateFitness(final List<List<Integer>> solution) {
        // Implement the logic to evaluate the fitness of a solution
        // For bin packing, this could be the number of bins used
        return solution.size();
    }

    private int getBinWeight(final List<Integer> bin) {
        int totalWeight = 0;
        for (int item : bin) {
            totalWeight += item;
        }
        return totalWeight;
    }

}
