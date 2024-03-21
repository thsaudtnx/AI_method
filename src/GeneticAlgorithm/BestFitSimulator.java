package GeneticAlgorithm;

public class BestFitSimulator extends Fitness {

    public BestFitSimulator(int cap){
        super();
        this.capacity = cap;
    }

    public int getMinimumBins(int per[]){
        int bins=0;
        int bins_space[] = new int[per.length];
        int current_bin = -1;
        int current_space = this.capacity;

        for(int i=0;i<bins_space.length;i++){
            bins_space[i] = this.capacity;
        }

        for(int i=0;i<per.length;i++){
            int j;
            for(j=0;j<bins;j++){
                if(bins_space[j] >= per[i] && bins_space[j] < current_space){
                    current_bin = j;
                    current_space = bins_space[j];
                }
            }
            if(current_bin != -1){
                bins_space[current_bin] -= per[i];
            }else{
                bins++;
                bins_space[bins-1] -= per[i];
            }
            current_bin = -1;
            current_space = this.capacity;
        }

        return bins;
    }
}