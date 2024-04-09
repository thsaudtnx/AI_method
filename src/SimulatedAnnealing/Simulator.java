package SimulatedAnnealing;

import BPP.BPPDatasetParser;
import BPP.BPPInstance;

import java.util.ArrayList;
import java.util.List;


public class Simulator {
    public static void main(String[] args) {
        BPPDatasetParser parser = new BPPDatasetParser();
        List<BPPInstance> instances = parser.parseBPPInstances();

        if (instances != null) {
            for (BPPInstance instance : instances) {
                System.out.println("Problem Name: " + instance.getProblemName());

                List<Integer> itemWeights = instance.getItemWeights();
                List<Integer> itemCounts = instance.getItemCounts();
                int binCapacity = instance.getBinCapacity();

                // T1 = 0.2Zc,
                // T2 = 0.5T1,
                // T3 = 0.5T2,
                // T4 = 0.5T3,
                // T5 = 0.5T4
                List<Double> temperatures = new ArrayList<>();
                int zc = 0;
                for (int i=0;i<itemCounts.size();i++){
                    zc += itemCounts.get(i);
                }
                double t1 = 0.2 * zc;
                double t2 = 0.5 * t1;
                double t3 = 0.5 * t2;
                double t4 = 0.5 * t3;
                double t5 = 0.5 * t4;
                temperatures.add(t1);
                temperatures.add(t1);
                temperatures.add(t1);
                temperatures.add(t1);
                temperatures.add(t1);
                temperatures.add(t2);
                temperatures.add(t2);
                temperatures.add(t2);
                temperatures.add(t2);
                temperatures.add(t2);
                temperatures.add(t3);
                temperatures.add(t3);
                temperatures.add(t3);
                temperatures.add(t3);
                temperatures.add(t3);
                temperatures.add(t4);
                temperatures.add(t4);
                temperatures.add(t4);
                temperatures.add(t4);
                temperatures.add(t4);
                temperatures.add(t5);
                temperatures.add(t5);
                temperatures.add(t5);
                temperatures.add(t5);
                temperatures.add(t5);

                SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(itemWeights, itemCounts, binCapacity, temperatures);
                int bestFitness = simulatedAnnealing.simulatedAnnealing();
                System.out.println("Simulated Annealing : " + bestFitness);

                System.out.println();
            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }
}
