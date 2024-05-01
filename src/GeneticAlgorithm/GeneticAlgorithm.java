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

        // Display Initial Generation
        //System.out.println("Initial Generation");
        //for (int i=0;i<currentPopulation.size();i++){
        //    System.out.println("Chromosome " + i + " " + currentPopulation.get(i).bins.toString());
        //}
        //System.out.println();

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

                //System.out.println("New Chromosome : " + newChromosome.bins.toString());
                //System.out.println();
            }

            // Update the population (moved it here)
            currentPopulation = nextPopulation;

            // Print the best solution in the current generation
            //System.out.println("Generation " + generation);
            //for (int i=0;i<currentPopulation.size();i++){
            //    System.out.println("Chromosome " + i + " " + currentPopulation.get(i).bins.toString());
            //}
            //System.out.println();
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

            //System.out.println("Chromosome " + i + ", Fitness " + chromosome.fitness + ", Range (" + lowerBound + ", " + upperBound +")");
        }

        //System.out.println("Spinned Value : " + spin);
        //System.out.println("Chosen Chromosome Index : " + index);
        //System.out.println("Bins : " + parent.bins.toString());
        //System.out.println();

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
        return new Chromosome(bins);
    }

    private Chromosome crossover(final Chromosome parent1, final Chromosome parent2, final double crossoverRate) {
        // Simple Crossover
        List<List<Integer>> bins = new ArrayList<>();
        Random random = new Random();

        // Check if crossover should occur based on crossover rate
        if (random.nextDouble() > crossoverRate) {
            // If crossover rate is not met, return one of the parents
            boolean parent = random.nextBoolean();
            bins = new ArrayList<>(parent ? parent1.bins : parent2.bins);
            return new Chromosome(bins);
        }

        int size = parent1.bins.size() > parent2.bins.size() ? parent2.bins.size() : parent1.bins.size();

        // Choose a random crossover point
        int crossoverPoint = random.nextInt(size);

        // Copy genetic material from parent 1 up to the crossover point
        for (int i = 0; i < crossoverPoint; i++) {
            bins.add(parent1.bins.get(i));
        }

        // Copy genetic material from parent 2 after the crossover point
        for (int i = crossoverPoint; i < parent2.bins.size(); i++) {
            bins.add(parent2.bins.get(i));
        }

        // Update the child
        Chromosome child = new Chromosome(bins);

        // Display the crossover
        //System.out.println("Crossover");
        //System.out.println("Crossover point : " + crossoverPoint);
        //System.out.println("Parent1 : " + parent1.bins.toString());
        //System.out.println("Parent2 : " + parent2.bins.toString());
        //System.out.println("Child : " + child.bins.toString());
        //System.out.println();

        return child;
    }

    private Chromosome mutate(final Chromosome chromosome, final double mutationRate) {
        // Static mutation
        //System.out.println("Mutation");
        Random random = new Random();
        List<List<Integer>> bins = new ArrayList<>(chromosome.bins);
        for (int i = 0; i < bins.size()-1; i++) {
            // Swap the current bin with the next bin
            if (random.nextDouble() < mutationRate) {
                //System.out.println("Mutation point : " + i);
                List<Integer> temp = bins.get(i);
                bins.set(i, bins.get(i+1));
                bins.set(i+1, temp);
            }
        }

        // Update the new chromosome
        Chromosome newChromosome = new Chromosome(bins);

        // Display after the mutation
        //System.out.println("Before " + chromosome.bins.toString());
        //System.out.println("After " + newChromosome.bins.toString());
        //System.out.println();

        return newChromosome;
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