import java.util.List;

public class BPPInstance {
    private String problemName;
    private int numItemWeights;
    private int binCapacity;
    private List<Integer> itemWeights;
    private List<Integer> itemCounts;

    public BPPInstance(String problemName, int numItemWeights, int binCapacity, List<Integer> itemWeights, List<Integer> itemCounts) {
        this.problemName = problemName;
        this.numItemWeights = numItemWeights;
        this.binCapacity = binCapacity;
        this.itemWeights = itemWeights;
        this.itemCounts = itemCounts;
    }

    public String getProblemName() {
        return problemName;
    }

    public int getNumItemWeights() {
        return numItemWeights;
    }

    public int getBinCapacity() {
        return binCapacity;
    }

    public List<Integer> getItemWeights() {
        return itemWeights;
    }

    public List<Integer> getItemCounts() {
        return itemCounts;
    }
}