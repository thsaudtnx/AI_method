package SimulatedAnnealing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedAnnealing {
    private final List<Integer> itemWeights;
    private final List<Integer> itemCounts;
    private final int binCapacity;
    private final List<Double> temperatures;

    public SimulatedAnnealing(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity, List<Double> temperatures){
        this.itemWeights = itemWeights;
        this.itemCounts = itemCounts;
        this.binCapacity = binCapacity;
        this.temperatures = temperatures;
    }
    public int simulatedAnnealing(){
        // Initialize
        List<List<Integer>> currentSolution = generateInitialSolution();
        List<List<Integer>> bestSolution = currentSolution;
        int currentFitness = evaluateFitness(currentSolution);
        int bestFitness = currentFitness;

        // Display the initial values
        System.out.println("=== Iteration 0 ===");
        System.out.println("Temperature : Null");
        System.out.print("Current Solution :");
        for (int i=0;i<currentSolution.size();i++){
            System.out.print(" " + currentSolution.get(i).toString());
        }
        System.out.println("\nCurrent fitness : " + currentFitness);
        System.out.println();

        // Iterations
        for (int iteration = 0; iteration < temperatures.size(); iteration++){
            List<List<Integer>> nextSolution = generateNextSolution(currentSolution);
            int nextFitness = evaluateFitness(currentSolution);

            // Update the next fitness and solution
            if (nextFitness <= currentFitness){
                currentSolution = nextSolution;
                currentFitness = nextFitness;
            } else {
                double acceptanceProbability = generateAcceptanceProbability(currentFitness, nextFitness, temperatures.get(iteration));
                // Generate a random number between 0 and 1
                Random random = new Random();
                double randomValue = random.nextDouble();

                // Check if the random value is less than or equal to the acceptance probability
                if (randomValue <= acceptanceProbability) {
                    // Accept the worse solution
                    currentSolution = nextSolution;
                    currentFitness = nextFitness;
                }
            }

            // Update the best Fitness and solution
            if (nextFitness <= bestFitness){
                bestSolution = nextSolution;
                bestFitness = nextFitness;
            }

            // Display the result of iteration
            System.out.println("=== Iteration " + (iteration+1) + " ===");
            System.out.println("Temperature : " + temperatures.get(iteration));
            System.out.print("Current Solution :");
            for (int i=0;i<currentSolution.size();i++){
                System.out.print(" " + currentSolution.get(i).toString());
            }
            System.out.println("\nCurrent fitness : " + currentFitness);
            System.out.println();
        }


        return bestFitness;
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
    private List<List<Integer>> generateNextSolution(final List<List<Integer>> currentSolution) {
        // Make a hard copy of current solution
        List<List<Integer>> nextSolution = new ArrayList<>();
        for (List<Integer> innerList : currentSolution) {
            List<Integer> innerCopy = new ArrayList<>(innerList);
            nextSolution.add(innerCopy);
        }

        // Random function for choosing the two bins and the item
        Random random = new Random();
        int from = random.nextInt(nextSolution.size());
        int itemIndex = random.nextInt(nextSolution.get(from).size());
        int item = nextSolution.get(from).get(itemIndex);
        int to;
        int totalItemWeight;
        do {
            // from and to bin cannot be same
            to = random.nextInt(nextSolution.size());
            // Check the total Item weight doesn't exceed the bin capacity
            totalItemWeight = getBinWeight(nextSolution.get(to)) + item;
        } while (from == to || totalItemWeight > binCapacity);

        // Move the item from the bin to the other bin
        nextSolution.get(to).add(item); // Add the item to the new bin
        nextSolution.get(from).remove(itemIndex); // Remove the item
        if (nextSolution.get(from).isEmpty()){
            nextSolution.remove(from); // Remove the empty bin
        }

        // Display the generated next solution
        System.out.println("from : " + from + ", to : " + to + ", itemIndex : " + itemIndex + ", item : " + item);
        System.out.print("Next Solution :");
        for (int i=0;i<nextSolution.size();i++){
            System.out.print(" " + nextSolution.get(i).toString());
        }
        System.out.println("\nNext Fitness : " + evaluateFitness(nextSolution) + "\n");

        return nextSolution;
    }
    private double generateAcceptanceProbability(int currentSolution, int nextSolution, double temperature) {
        return Math.exp((currentSolution - nextSolution) / temperature);
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
