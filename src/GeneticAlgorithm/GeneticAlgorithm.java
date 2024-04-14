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
    private final int maxGeneration;

    public GeneticAlgorithm(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity, int populationSize, double mutationRate, int maxGeneration){
        this.itemWeights = itemWeights;
        this.itemCounts = itemCounts;
        this.binCapacity = binCapacity;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.maxGeneration = maxGeneration;
    }

    public void geneticAlgorithm() {
        // Initialize population
        List<Chromosome> currentPopulation = generateInitializePopulation();

        // Display Initial Generation
        System.out.println("Initial Generation");
        for (int i=0;i<currentPopulation.size();i++){
            System.out.println("Chromosome " + i + " " + currentPopulation.get(i).bins.toString());
        }
        System.out.println();



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
                Chromosome offspring = crossover(parent1, parent2);

                // Mutation
                mutate(offspring, mutationRate);

                // Add the offspring into next population
                nextPopulation.add(offspring);

                // Update the population (moved it here)
                currentPopulation = nextPopulation;
            }

            // Print the best solution in the current generation
            System.out.println("Generation " + generation);
            for (int i=0;i<currentPopulation.size();i++){
                System.out.println("Chromosome " + i + " " + currentPopulation.get(i).bins.toString());
            }
            System.out.println();
            System.out.println("Best Fitness : " + findBestChromosome(currentPopulation).fitness);
            System.out.println();
        }

        // Find the best solution
        Chromosome bestChromosome = findBestChromosome(currentPopulation);

        // Display the result
        System.out.println("Best Solution:");
        System.out.println("Fitness: " + bestChromosome.fitness);
        System.out.println("Bins: " + bestChromosome.bins.toString());
        System.out.println();

    }

    private Chromosome selection(List<Chromosome> population) {
        // Calculate total fitness of the population
        double totalFitness = 0;
        for (Chromosome chromosome : population) {
            totalFitness += chromosome.fitness;
        }

        // Roulette Wheel Selection
        Chromosome parent = new Chromosome(new ArrayList<>());
        Random random = new Random();
        int index = 0;
        double spin = random.nextDouble();
        double cumulativeFitness = 0;
        System.out.println("Roulette Wheel Selection");
        for (int i=0;i<population.size();i++){
            Chromosome chromosome = population.get(i);

            double lowerBound = cumulativeFitness / totalFitness;
            double upperBound = lowerBound + chromosome.fitness / totalFitness;

            if (lowerBound <= spin && spin < upperBound){
                parent = chromosome;
                index = i;
            }

            cumulativeFitness += chromosome.fitness;

            System.out.println("Chromosome " + i + ", Fitness " + chromosome.fitness + ", Range (" + lowerBound + ", " + upperBound +")");
        }

        System.out.println("Spinned Value : " + spin);
        System.out.println("Chosen Chromosome Index : " + index);
        System.out.println();

        return parent;
    }

    private List<Chromosome> generateInitializePopulation() {
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(generateInitialChromosome());
        }
        return population;
    }

    private void evaluateFitness(List<Chromosome> population) {
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

        return new Chromosome(bins);
    }

    private Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        // Simple Crossover
        List<List<Integer>> bins = new ArrayList<>();
        Chromosome child = new Chromosome(bins);
        Random random = new Random();
        int size = parent1.bins.size() > parent2.bins.size() ? parent2.bins.size() : parent1.bins.size();

        // Choose a random crossover point
        int crossoverPoint = random.nextInt(size);

        // Copy genetic material from parent 1 up to the crossover point
        for (int i = 0; i < crossoverPoint; i++) {
            child.bins.add(parent1.bins.get(i));
        }

        // Copy genetic material from parent 2 after the crossover point
        for (int i = crossoverPoint; i < parent2.bins.size(); i++) {
            child.bins.add(parent2.bins.get(i));
        }

        // Update the fitness of the child
        child.fitness = child.bins.size();

        // Display the crossover
        System.out.println("Crossover");
        System.out.println("Crossover point : " + crossoverPoint);
        System.out.println("Parent1 : " + parent1.bins.toString());
        System.out.println("Parent2 : " + parent2.bins.toString());
        System.out.println("Child : " + child.bins.toString());
        System.out.println();

        return child;
    }

    private void mutate(Chromosome chromosome, double mutationRate) {
        // Static mutation
        System.out.println("Mutation");
        System.out.println("Before " + chromosome.bins.toString());
        Random random = new Random();
        for (int i = 0; i < chromosome.bins.size()-1; i++) {
            int item = chromosome.bins.get(i).get(0);
            int binWeight = getBinWeight(chromosome.bins.get(i+1));

            // Move the first item from the bin to the other bin
            if (random.nextDouble() < mutationRate && binWeight + item <= binCapacity) {
                System.out.println("Mutation point : " + i);
                chromosome.bins.get(i+1).add(item); // Add the item to the new bin
                chromosome.bins.get(i).remove(0); // Remove the item

                // Remove the empty bin
                if (chromosome.bins.get(i).isEmpty()){
                    chromosome.bins.remove(i);
                    chromosome.fitness -= 1;
                }
            }
        }

        // Display after the mutation
        System.out.println("After " + chromosome.bins.toString());
        System.out.println();
    }

    private Chromosome findBestChromosome(List<Chromosome> population){
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
