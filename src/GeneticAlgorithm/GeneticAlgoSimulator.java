package GeneticAlgorithm;

import java.util.List;
import java.util.HashMap;
import java.util.Random;
import BPP.BPPDatasetParser;
import BPP.BPPInstance;

public class GeneticAlgoSimulator {
    private static int no_items;
    private static int items[];
    private static int population_size;
    private static int generation;
    private static Chromosome chromosomes[];
    private static Fitness fit;
    private static int global_minimum_bins;

    public static void main(String[] args) {
        BPPDatasetParser parser = new BPPDatasetParser();
        List<BPPInstance> instances = parser.parseBPPInstances();

        if (instances != null) {
            for (BPPInstance instance : instances) {
                System.out.println("Problem Name: " + instance.getProblemName());

                List<Integer> itemWeights = instance.getItemWeights();
                List<Integer> itemCounts = instance.getItemCounts();
                int binCapacity = instance.getBinCapacity();

                geneticAlgorithm(itemWeights, itemCounts, binCapacity);

                System.out.println();
            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }

    public static void geneticAlgorithm(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity){
        fit = new BestFitSimulator(binCapacity);
        createChromosomes(fit);
        global_minimum_bins = getMinimumBinsRequired();
        System.out.println("Minimum Bins Required : " + global_minimum_bins);
        System.out.println();

        while(generation!=0){
            // new generation creating
            System.out.println("Generation : " + generation);
            generation--;
            chromosomes = createNewGeneration();
            printPopulation();
            int local_minimum_bins = getMinimumBinsRequired();
            if(local_minimum_bins<global_minimum_bins){
                global_minimum_bins=local_minimum_bins;
            }
            System.out.println("Genetic Algorithm local bins : " + local_minimum_bins);
            System.out.println();
        }

        System.out.println("Genetic Algorithm global bins : " + global_minimum_bins);
    }

    private static void createChromosomes(Fitness fit){
        chromosomes = new Chromosome[population_size];
        int index_permutation[] = items;
        for(int i=0;i<chromosomes.length;i++){
            index_permutation = nextPermutation(index_permutation);
            chromosomes[i] = new Chromosome(index_permutation,fit);
        }
        printPopulation();
    }

    private static int[] nextPermutation(int main_permutation[]){
        Random ran = new Random();
        int new_permutation[] = new int[main_permutation.length];
        for(int i=0;i<new_permutation.length;i++){
            new_permutation[i] = main_permutation[i];
        }
        for(int i=0;i<new_permutation.length;i++){
            int pos = ran.nextInt(new_permutation.length);
            int a = new_permutation[i];
            new_permutation[i] = new_permutation[pos];
            new_permutation[pos] = a;
        }
        return new_permutation;
    }

    private static void printPopulation(){
        for(int i=0;i<population_size;i++){
            for(int j=0;j<chromosomes[i].permutation.length;j++){
                System.out.print(chromosomes[i].permutation[j] + "  ");
            }
            System.out.println("Fitness : " + chromosomes[i].fitness);
        }
    }

    private static Chromosome[] createNewGeneration(){
        Random ran = new Random();
        Chromosome new_generation[] = new Chromosome[population_size];
        int new_population_size = population_size;

        while(new_population_size!=0){
            // parent selection
            new_population_size--;
            Chromosome parent[] = new Chromosome[2];
            int parent0 = ran.nextInt(population_size);
            int parent1 = ran.nextInt(population_size);
            parent[0] = (chromosomes[parent0].fitness <= chromosomes[parent1].fitness) ? chromosomes[parent0] : chromosomes[parent1];
            parent[1] = (chromosomes[parent0].fitness <= chromosomes[parent1].fitness) ? chromosomes[parent1] : chromosomes[parent0];
            // crossover and offspring
            int new_permutation[] = new int[no_items];
            HashMap<Integer , Integer> parent0_state = new HashMap<>();
            HashMap<Integer , Integer> parent1_state = new HashMap<>();
            for(int i=0;i<parent[0].permutation.length;i++){
                if(!parent0_state.containsKey(parent[0].permutation[i])){
                    parent0_state.put(parent[0].permutation[i], 1);
                    parent1_state.put(parent[0].permutation[i], 1);
                }else{
                    parent0_state.put(parent[0].permutation[i], parent0_state.get(parent[0].permutation[i])+1);
                    parent1_state.put(parent[0].permutation[i], parent1_state.get(parent[0].permutation[i])+1);
                }
            }
            int index=0;
            for(int i=0;i<parent[0].permutation.length;i++){
                if(parent0_state.get(parent[0].permutation[i]) <= parent1_state.get(parent[0].permutation[i])){
                    new_permutation[index] = parent[0].permutation[i];
                    index++;
                }
                parent0_state.put(parent[0].permutation[i], parent0_state.get(parent[0].permutation[i])-1);
                if(parent0_state.get(parent[1].permutation[i]) >= parent1_state.get(parent[1].permutation[i])){
                    new_permutation[index] = parent[1].permutation[i];
                    index++;
                }
                parent1_state.put(parent[1].permutation[i], parent1_state.get(parent[1].permutation[i])-1);
            }
            //mutation
            int pos0 = ran.nextInt(new_permutation.length);
            int pos1 = ran.nextInt(new_permutation.length);
            int temp = new_permutation[pos0];
            new_permutation[pos0] = new_permutation[pos1];
            new_permutation[pos1] = temp;
            // create new chromosome
            Chromosome new_chromosome = new Chromosome(new_permutation, fit);
            new_generation[new_population_size] = new_chromosome;
        }
        return new_generation;
    }

    private static int getMinimumBinsRequired(){
        int min_bins=Integer.MAX_VALUE;
        for(int i=0;i<chromosomes.length;i++){
            if(chromosomes[i].fitness < min_bins){
                min_bins = chromosomes[i].fitness;
            }
        }
        return min_bins;
    }
}