package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;


public class GeneticAlgorithm {

    private static final int POPULATION_SIZE = 100;
    private static final double MUTATION_RATE = 0.1;
    private static final int MAX_GENERATIONS = 1000;
    private static List<Integer> itemWeights;
    private static List<Integer> itemCounts;
    private static int binCapacity;

    public static void geneticAlgorithm(List<Integer> weights, List<Integer> counts, int capacity) {
        itemWeights = weights;
        itemCounts = counts;
        binCapacity = capacity;

        // Initialize population
        List<Chromosome> population = initializePopulation();

        int generation = 0;
        while (generation < MAX_GENERATIONS) {
            // Evaluate fitness
            evaluateFitness(population);

            // Create new generation
            List<Chromosome> newPopulation = new ArrayList<>();
            for (int i = 0; i < POPULATION_SIZE; i++) {
                Chromosome parent1 = tournamentSelection(population);
                Chromosome parent2 = tournamentSelection(population);
                Chromosome offspring = crossover(parent1, parent2);
                mutate(offspring);
                newPopulation.add(offspring);
            }
            population = newPopulation;
            generation++;

            // Print the best solution in the current generation
            Chromosome bestChromosome = population.stream()
                    .sorted((c1, c2) -> Integer.compare(c2.fitness, c1.fitness))
                    .findFirst()
                    .orElse(null);
            if (bestChromosome != null) {
                System.out.println("Generation " + generation + ": Best Fitness = " + bestChromosome.fitness);
            }
        }

        // Print the best solution found
        Chromosome bestChromosome = population.stream()
                .sorted((c1, c2) -> Integer.compare(c2.fitness, c1.fitness))
                .findFirst()
                .orElse(null);
        if (bestChromosome != null) {
            System.out.println("Best Solution:");
            System.out.println("Fitness: " + bestChromosome.fitness);
            System.out.println("Bins: " + bestChromosome.bins.size());
            for (List<Integer> bin : bestChromosome.bins) {
                System.out.print("Bin: ");
                for (Integer item : bin) {
                    System.out.print(item + " ");
                }
                System.out.println();
            }
        }
    }

    private static Chromosome tournamentSelection(List<Chromosome> population) {
        Random rand = new Random();
        Chromosome parent1 = population.get(rand.nextInt(population.size()));
        Chromosome parent2 = population.get(rand.nextInt(population.size()));
        return (parent1.fitness >= parent2.fitness) ? parent1 : parent2;
    }



    private static List<Chromosome> initializePopulation() {
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(generateBestFitChromosome());
        }
        return population;
    }

    private static void evaluateFitness(List<Chromosome> population) {
        for (Chromosome chromosome : population) {
            int totalWeight = 0;
            for (List<Integer> bin : chromosome.bins) {
                int binWeight = bin.stream().mapToInt(Integer::intValue).sum();
                if (binWeight > binCapacity) {
                    chromosome.fitness = 0;
                    break;
                }
                totalWeight += binWeight;
            }
            if (chromosome.fitness != 0) {
                chromosome.fitness = totalWeight;
            }
        }
    }

    private static Chromosome generateBestFitChromosome() {
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

    private static Chromosome crossover(Chromosome parent1, Chromosome parent2) {
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

    private static void mutate(Chromosome chromosome) {
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
                if (rand.nextDouble() < MUTATION_RATE) {
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

    private static class Chromosome {
        List<List<Integer>> bins;
        int fitness;

        Chromosome(List<List<Integer>> bins) {
            this.bins = bins;
            this.fitness = Integer.MAX_VALUE;
        }
    }
}
