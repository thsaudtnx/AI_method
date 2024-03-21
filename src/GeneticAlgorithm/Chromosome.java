package GeneticAlgorithm;

public class Chromosome {

    public int fitness;
    public int permutation[];
    public static Fitness fit;

    public Chromosome(int per[],Fitness fit){
        this.permutation = per;
        this.fit = fit;
        this.fitness = fit.getMinimumBins(this.permutation);
    }

}