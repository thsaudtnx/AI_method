import java.util.ArrayList;
import java.util.List;

public class Greedy {

    public static void main(String[] args) {
        BPPDatasetParser parser = new BPPDatasetParser();
        List<BPPInstance> instances = parser.parseBPPInstances();

        if (instances != null) {
            for (BPPInstance instance : instances) {
                System.out.println("Problem Name: " + instance.getProblemName());

                List<Integer> itemWeights = instance.getItemWeights();
                List<Integer> itemCounts = instance.getItemCounts();
                int binCapacity = instance.getBinCapacity();

                // First Fit
                List<Integer> binsFirstFit = firstFit(itemWeights, itemCounts, binCapacity);
                System.out.println("First Fit bins: " + binsFirstFit.size());

                // Best Fit
                List<Integer> binsBestFit = bestFit(itemWeights, itemCounts, binCapacity);
                System.out.println("Best Fit bins: " + binsBestFit.size());

                // Next Fit
                List<Integer> binsNextFit = nextFit(itemWeights, itemCounts, binCapacity);
                System.out.println("Next Fit bins: " + binsNextFit.size());

                System.out.println();
            }
        } else {
            System.out.println("Failed to parse the instances.");
        }
    }

    public static List<Integer> firstFit(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity) {
        List<Integer> bins = new ArrayList<>();
        for (int i = 0; i < itemWeights.size(); i++) {
            int weight = itemWeights.get(i);
            int count = itemCounts.get(i);
            boolean packed = false;
            for (int j = 0; j < bins.size(); j++) {
                if (bins.get(j) + weight <= binCapacity) {
                    bins.set(j, bins.get(j) + weight);
                    packed = true;
                    break;
                }
            }
            if (!packed) {
                bins.add(weight);
            }
        }
        return bins;
    }

    public static List<Integer> bestFit(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity) {
        List<Integer> bins = new ArrayList<>();
        for (int i = 0; i < itemWeights.size(); i++) {
            int weight = itemWeights.get(i);
            int count = itemCounts.get(i);
            int minSpaceIndex = -1;
            int minSpace = Integer.MAX_VALUE;
            for (int j = 0; j < bins.size(); j++) {
                int spaceLeft = binCapacity - bins.get(j);
                if (weight <= spaceLeft && spaceLeft < minSpace) {
                    minSpace = spaceLeft;
                    minSpaceIndex = j;
                }
            }
            if (minSpaceIndex != -1) {
                bins.set(minSpaceIndex, bins.get(minSpaceIndex) + weight);
            } else {
                bins.add(weight);
            }
        }
        return bins;
    }

    public static List<Integer> nextFit(List<Integer> itemWeights, List<Integer> itemCounts, int binCapacity) {
        List<Integer> bins = new ArrayList<>();
        int currentBinCapacity = 0;
        for (int i = 0; i < itemWeights.size(); i++) {
            int weight = itemWeights.get(i);
            int count = itemCounts.get(i);
            if (currentBinCapacity + weight <= binCapacity) {
                currentBinCapacity += weight;
            } else {
                bins.add(currentBinCapacity);
                currentBinCapacity = weight;
            }
        }
        bins.add(currentBinCapacity);
        return bins;
    }
}
