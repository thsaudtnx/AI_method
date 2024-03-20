import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class Decreasing {

    public static void main(String[] args) {
        BPPDatasetParser parser = new BPPDatasetParser();
        List<BPPInstance> instances = parser.parseBPPInstances();

        if (instances != null) {
            for (BPPInstance instance : instances) {
                System.out.println("Problem Name: " + instance.getProblemName());

                List<Integer> itemWeights = instance.getItemWeights();
                List<Integer> itemCounts = instance.getItemCounts();
                int binCapacity = instance.getBinCapacity();

                // Apply Decreasing Algorithm (Sort itemWeights in decreasing order)
                List<Integer> decreasingItemWeights = new ArrayList<>(itemWeights);
                Collections.sort(decreasingItemWeights, Collections.reverseOrder());

                // Decreasing First Fit
                List<Integer> binsDecreasingFirstFit = firstFit(decreasingItemWeights, itemCounts, binCapacity);
                System.out.println("Decreasing First Fit bins: " + binsDecreasingFirstFit.size());

                // Decreasing Best Fit
                List<Integer> binsDecreasingBestFit = bestFit(decreasingItemWeights, itemCounts, binCapacity);
                System.out.println("Decreasing Best Fit bins: " + binsDecreasingBestFit.size());

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
}
