package SimulatedAnnealing;

import java.util.ArrayList;
import java.util.Collections;
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
    public void simulatedAnnealing(){
        // Set the start time
        long startTime = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        // Run garbage collector to free up memory
        runtime.gc();

        // Initialize
        List<List<Integer>> currentSolution = generateInitialSolution();
        List<List<Integer>> bestSolution = currentSolution;
        int currentFitness = evaluateFitness(currentSolution);
        int bestFitness = currentFitness;

        // Display the initial values
        // System.out.println("=== Iteration 0 ===");
        // System.out.println("Temperature : Null");
        // System.out.print("Current Solution : " + currentSolution.toString());
        // System.out.println("Current fitness : " + currentFitness);
        // System.out.println();
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
            // System.out.println("=== Iteration " + (iteration+1) + " ===");
            // System.out.println("Temperature : " + temperatures.get(iteration));
            // System.out.print("Current Solution : " + currentSolution.toString());
            // System.out.println("Current fitness : " + currentFitness);
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
        System.out.println("Bins: " + bestSolution.toString());
        System.out.println("Number of bin used: " + bestFitness);

        // Display Wastage
        double meanWastage = calculateMeanWastage(bestSolution);
        System.out.println("Mean wastage per bin: " + String.format("%.2f", meanWastage) + " units");
        System.out.println();

    }
    private List<List<Integer>> generateInitialSolution() {
        // Generate list of items based on their weights and counts
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < itemWeights.size(); i++) {
            for (int j = 0; j < itemCounts.get(i); j++) {
                items.add(itemWeights.get(i));
            }
        }

        // Optional: sort items by weight in descending order for First Fit Decreasing
        Collections.sort(items, Collections.reverseOrder());

        // Initialize bins
        List<List<Integer>> bins = new ArrayList<>();

        // Apply the First Fit algorithm
        for (int item : items) {
            boolean placed = false;
            for (List<Integer> bin : bins) {
                if (getBinWeight(bin) + item <= binCapacity) {
                    bin.add(item);
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                List<Integer> newBin = new ArrayList<>();
                newBin.add(item);
                bins.add(newBin);
            }
        }
        return bins;
    }

    private List<List<Integer>> generateNextSolution(final List<List<Integer>> currentSolution) {
        // Deep copy of the current solution
        List<List<Integer>> nextSolution = new ArrayList<>(currentSolution.size());
        for (List<Integer> bin : currentSolution) {
            nextSolution.add(new ArrayList<>(bin));
        }

        // Shared random instance
        Random random = new Random();

        // Select a random bin and item
        int fromBinIndex = random.nextInt(nextSolution.size());
        List<Integer> fromBin = nextSolution.get(fromBinIndex);
        int itemIndex = random.nextInt(fromBin.size());
        int item = fromBin.get(itemIndex);

        // List of potential bins that can accommodate the item
        List<Integer> possibleBins = new ArrayList<>();
        for (int i = 0; i < nextSolution.size(); i++) {
            if (i != fromBinIndex && nextSolution.get(i).isEmpty()) continue;
            int spaceLeft = binCapacity - getBinWeight(nextSolution.get(i));
            if (spaceLeft >= item) {
                possibleBins.add(i);
            }
        }

        // Randomly select a bin to place the item if possible
        if (!possibleBins.isEmpty()) {
            int targetBinIndex = possibleBins.get(random.nextInt(possibleBins.size()));
            nextSolution.get(targetBinIndex).add(item);
            fromBin.remove(itemIndex);
        } else {
            // If no suitable bin found, create a new bin
            nextSolution.add(new ArrayList<>(List.of(item)));
        }

        // Remove the original bin if it's now empty
        if (fromBin.isEmpty()) {
            nextSolution.remove(fromBinIndex);
        }

        return nextSolution;
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


    private double generateAcceptanceProbability(final int currentFitness, final int nextFitness, final double temperature) {
        if (nextFitness < currentFitness) {
            // Automatically accept if the new solution is better
            return 1.0;
        } else {
            // Calculate acceptance probability for a worse solution
            double exponent = -(nextFitness - currentFitness) / temperature;
            // Check for extremely negative exponent to avoid underflow
            if (exponent < -50) {
                return 0.0;
            } else {
                return Math.exp(exponent);
            }
        }
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