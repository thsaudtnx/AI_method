package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;

public class GeneticAlgorithm {
    private final List<Integer> itemWeights;
    private final List<Integer> itemCounts;
    private final int binCapacity;
    private final int populationSize;
    private final double mutationRate;
    private final double crossoverRate;
    private final int maxGeneration;

    public GeneticAlgorithm(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity, int populationSize, double mutationRate, double crossoverRate, int maxGeneration){
        this.itemWeights = itemWeights;
        this.itemCounts = itemCounts;
        this.binCapacity = binCapacity;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.maxGeneration = maxGeneration;
    }

    public void geneticAlgorithm() {

        // Set the start time
        long startTime = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        // Run garbage collector to free up memory
        runtime.gc();

        // Initialize population
        List<Chromosome> currentPopulation = generateInitializePopulation();


        // Generations iteration
        for (int generation = 0; generation < maxGeneration; generation++){

            // Evaluate fitness
            evaluateFitness(currentPopulation);

            // Create new generation
            List<Chromosome> nextPopulation = new ArrayList<>();
            for (int i = 0; i < populationSize; i++) {
                // Selection
                Chromosome parent1 = selection(currentPopulation);
                Chromosome parent2 = selection(currentPopulation);

                // Crossover
                Chromosome offspring = crossover(parent1, parent2, crossoverRate);

                // Mutation
                Chromosome newChromosome = mutate(offspring, mutationRate);

                // Add the offspring into next population
                nextPopulation.add(newChromosome);
            }

            // Update the population (moved it here)
            currentPopulation = nextPopulation;
        }

        // Find the best solution
        Chromosome bestChromosome = findBestChromosome(currentPopulation);

        // Calculate the runtime
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total execution time: " + totalTime + " milliseconds");

        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory: " + memory + " bytes");

        // Display the result
        System.out.println("Number of bin used: " + bestChromosome.fitness);
        System.out.println("Bins: " + bestChromosome.bins.toString());

        // Display Wastage
        double meanWastage = calculateMeanWastage(bestChromosome.bins);
        System.out.println("Mean wastage per bin: " + String.format("%.2f", meanWastage) + " units");
        System.out.println();
    }

    private Chromosome selection(final List<Chromosome> population) {
        // Calculate total fitness of the population
        double totalFitness = 0;
        for (Chromosome chromosome : population) {
            totalFitness += chromosome.fitness;
        }

        // Roulette Wheel Selection
        Chromosome parent = new Chromosome(new ArrayList<>());
        Random random = new Random();
        double spin = random.nextDouble();
        double cumulativeFitness = 0;
        //System.out.println("Roulette Wheel Selection");
        for (int i=0;i<population.size();i++){
            Chromosome chromosome = population.get(i);

            double lowerBound = cumulativeFitness / totalFitness;
            double upperBound = lowerBound + chromosome.fitness / totalFitness;

            if (lowerBound <= spin && spin < upperBound){
                parent = chromosome;
            }

            cumulativeFitness += chromosome.fitness;
        }

        return parent;
    }

    private List<Chromosome> generateInitializePopulation() {
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(generateInitialChromosome());
        }
        return population;
    }

    private void evaluateFitness(final List<Chromosome> population) {
        for (Chromosome chromosome : population) {
            chromosome.fitness = chromosome.bins.size();
        }
    }

    private Chromosome generateInitialChromosome() {
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

        // Optional: sort items by weight in descending order for First Fit Decreasing
        items.sort(Collections.reverseOrder());

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
        return new Chromosome(bins);
    }

    private Chromosome crossover(final Chromosome parent1, final Chromosome parent2, final double crossoverRate) {
        Random random = new Random();
        // Check if crossover should occur based on crossover rate
        if (random.nextDouble() > crossoverRate) {
            // If crossover rate is not met, randomly return one of the parents
            return random.nextBoolean() ? parent1 : parent2;
        }

        // Calculate minimum size to ensure compatibility
        int size = Math.min(parent1.bins.size(), parent2.bins.size());

        // Choose a random crossover point
        int crossoverPoint = random.nextInt(size);

        // Create a new chromosome from parts of both parents
        List<List<Integer>> bins = new ArrayList<>(size);
        bins.addAll(parent1.bins.subList(0, crossoverPoint)); // Copy from parent1 up to the crossover point
        bins.addAll(parent2.bins.subList(crossoverPoint, parent2.bins.size())); // Copy from parent2 from the crossover point

        // Return new child chromosome
        return new Chromosome(bins);
    }

    private Chromosome mutate(final Chromosome chromosome, final double mutationRate) {
        Random random = new Random();
        List<List<Integer>> bins = new ArrayList<>(chromosome.bins);

        // Determine the number of mutations based on the mutation rate
        int numberOfMutations = (int) (mutationRate * bins.size());

        for (int i = 0; i < numberOfMutations; i++) {
            int indexToMutate = random.nextInt(bins.size());
            int swapIndex = random.nextInt(bins.size());
            Collections.swap(bins, indexToMutate, swapIndex);
        }

        return new Chromosome(bins);
    }

    private Chromosome findBestChromosome(final List<Chromosome> population){
        Chromosome bestChromosome = population.get(0);
        for (Chromosome chromosome : population){
            if (chromosome.fitness < bestChromosome.fitness){
                bestChromosome = chromosome;
            }
        }
        return bestChromosome;
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

    private int getBinWeight(final List<Integer> bin) {
        int totalWeight = 0;
        for (int item : bin) {
            totalWeight += item;
        }
        return totalWeight;
    }

    private class Chromosome {
        List<List<Integer>> bins;
        int fitness;

        Chromosome(List<List<Integer>> bins) {
            this.bins = bins;
            this.fitness = bins.size();
        }
    }
}