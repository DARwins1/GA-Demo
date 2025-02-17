import java.util.ArrayList;
import java.util.Random;

public class Knapsack {

	static final int AVG_WEIGHT = 20;
	static final int AVG_VALUE = 10;
	static final double KNAPSACK_GROWTH_FACTOR = 5d / 3;

	static ArrayList<Integer> values;
	static ArrayList<Integer> weights;

	public static void main(String[] args) {

		values = new ArrayList<Integer>();
		weights = new ArrayList<Integer>();

		// Get the upper bound for items and GA specs from the command line
		int itemLimit = Integer.parseInt(args[0]);
		int generationSize = Integer.parseInt(args[1]);
		int numGenerations = Integer.parseInt(args[2]);
		
		int knapsackSize = AVG_WEIGHT;
		for (int i = 0; i < itemLimit; i++) {
			addItem(); // Add a new randomized item to the pool
			
			// Evaluate & report...
			System.out.print("Items: " + values.size());
			GeneticAlgorithm genetic = new GeneticAlgorithm(values, weights, knapsackSize, generationSize, numGenerations);
			System.out.print(", GA Value: " + genetic.value + ", GA Time: " + genetic.time / 1000d);
			BruteForce naive = new BruteForce(values, weights, knapsackSize);
			System.out.print(", Optimal Value: " + naive.value + ", Naive Time: " + naive.time / 1000d);
			System.out.print(", GA Optimality: " + ((double)genetic.value / naive.value));
			System.out.print(", GA Speedup Factor: " + ((double)naive.time / ((genetic.time == 0) ? 1 : genetic.time)));
			System.out.println();
			
			// Increase the knapsack size for the next iteration
			knapsackSize = knapsackSize + (int)(AVG_WEIGHT * KNAPSACK_GROWTH_FACTOR);
		}
	}

	// Adds a new item with randomized
	private static void addItem() {
		Random random = new Random();
		// Values and weights range +/-50%
		values.add(AVG_VALUE / 2 + random.nextInt(AVG_VALUE + 1));
		weights.add(AVG_WEIGHT / 2 + random.nextInt(AVG_WEIGHT + 1));
	}

}
