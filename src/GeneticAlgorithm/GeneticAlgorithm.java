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

    public int geneticAlgorithm() {
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
                List<Chromosome> parents = tournamentSelection(currentPopulation);
                Chromosome parent1 = parents.get(0);
                Chromosome parent2 = parents.get(1);

                // Crossover
                Chromosome offspring = crossover(parent1, parent2);

                // Mutation
                mutate(offspring);
                nextPopulation.add(offspring);

                // Update the population (moved it here)
                currentPopulation = nextPopulation;
            }

            // Print the best solution in the current generation
            System.out.println("Generation " + generation + "- Best Fitness : " + findBestChromosome(currentPopulation));
        }

        // Find the best solution
        Chromosome bestChromosome = findBestChromosome(currentPopulation);

        // Display the result
        System.out.println("Best Solution:");
        System.out.println("Fitness: " + bestChromosome.fitness);
        System.out.println("Bins: " + bestChromosome.bins.toString());
        System.out.println();

        return bestChromosome.fitness;
    }

    private List<Chromosome> tournamentSelection(List<Chromosome> population) {
        List<Chromosome> parents = new ArrayList<>();
        Random random = new Random();
        int tournamentSize = random.nextInt(populationSize);
        for (int i = 0; i < populationSize; i++) {
            List<Chromosome> tournament = new ArrayList<>();
            for (int j = 0; j < tournamentSize; j++) {
                tournament.add(population.get(random.nextInt(populationSize)));
            }
            // Select the best individual (solution) from the tournament
            Chromosome bestChromosome = findBestChromosome(tournament);
            parents.add(bestChromosome);
        }
        return parents;
    }

    private List<Chromosome> generateInitializePopulation() {
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(generateBestFitChromosome());
        }
        return population;
    }

    private void evaluateFitness(List<Chromosome> population) {
        for (Chromosome chromosome : population) {
            chromosome.fitness = chromosome.bins.size();
        }
    }

    private Chromosome generateBestFitChromosome() {
        List<Integer> decreasingItemWeights = new ArrayList<>(itemWeights);
        Collections.sort(decreasingItemWeights, Collections.reverseOrder());
        List<List<Integer>> bins = new ArrayList<>();
        for (int i = 0; i < decreasingItemWeights.size(); i++) {
            int weight = decreasingItemWeights.get(i);
            int count = itemCounts.get(itemWeights.indexOf(weight));
            int minSpaceIndex = -1;
            int minSpace = Integer.MAX_VALUE;
            for (int j = 0; j < bins.size(); j++) {
                int spaceLeft = binCapacity - bins.get(j).stream().mapToInt(Integer::intValue).sum();
                if (weight <= spaceLeft && spaceLeft < minSpace) {
                    minSpace = spaceLeft;
                    minSpaceIndex = j;
                }
            }
            if (minSpaceIndex != -1) {
                for (int k = 0; k < count; k++) {
                    bins.get(minSpaceIndex).add(weight);
                }
            } else {
                List<Integer> bin = new ArrayList<>();
                for (int k = 0; k < count; k++) {
                    bin.add(weight);
                }
                bins.add(bin);
            }
        }
        return new Chromosome(bins);
    }

    private Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        List<List<Integer>> offspringBins = new ArrayList<>();
        int parent1Index = 0, parent2Index = 0;
        while (parent1Index < parent1.bins.size() && parent2Index < parent2.bins.size()) {
            List<Integer> parent1Bin = parent1.bins.get(parent1Index);
            List<Integer> parent2Bin = parent2.bins.get(parent2Index);
            List<Integer> offspringBin = new ArrayList<>();
            int offspringBinWeight = 0;
            for (int item : parent1Bin) {
                if (offspringBinWeight + item <= binCapacity) {
                    offspringBin.add(item);
                    offspringBinWeight += item;
                }
            }
            for (int item : parent2Bin) {
                if (offspringBinWeight + item <= binCapacity) {
                    offspringBin.add(item);
                    offspringBinWeight += item;
                }
            }
            offspringBins.add(offspringBin);
            parent1Index++;
            parent2Index++;
        }
        return new Chromosome(offspringBins);
    }

    private void mutate(Chromosome chromosome) {
        Random rand = new Random();
        List<Integer> remainingItems = new ArrayList<>();
        for (int i = 0; i < itemWeights.size(); i++) {
            for (int j = 0; j < itemCounts.get(i); j++) {
                remainingItems.add(itemWeights.get(i));
            }
        }
        for (int i = 0; i < chromosome.bins.size(); i++) {
            List<Integer> bin = chromosome.bins.get(i);
            for (int j = 0; j < bin.size(); j++) {
                if (rand.nextDouble() < mutationRate) {
                    bin.remove(j);
                    int minSpaceIndex = -1;
                    int minSpace = Integer.MAX_VALUE;
                    for (int k = 0; k < chromosome.bins.size(); k++) {
                        if (k != i) {
                            int spaceLeft = binCapacity - chromosome.bins.get(k).stream().mapToInt(Integer::intValue).sum();
                            if (remainingItems.get(0) <= spaceLeft && spaceLeft < minSpace) {
                                minSpace = spaceLeft;
                                minSpaceIndex = k;
                            }
                        }
                    }
                    if (minSpaceIndex != -1) {
                        chromosome.bins.get(minSpaceIndex).add(remainingItems.get(0));
                    } else {
                        List<Integer> newBin = new ArrayList<>();
                        newBin.add(remainingItems.get(0));
                        chromosome.bins.add(newBin);
                    }
                    remainingItems.remove(0);
                }
            }
        }
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

    private class Chromosome {
        List<List<Integer>> bins;
        int fitness;

        Chromosome(List<List<Integer>> bins) {
            this.bins = bins;
            this.fitness = Integer.MAX_VALUE;
        }
    }
}
