import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

public class GeneticAlgorithm {

	static final double SELECTION_RATIO = 0.5d;
	static final double ELITE_RATIO = 0.1d;
	
	private ArrayList<Integer> values;
	private ArrayList<Integer> weights;
	private int maxWeight;
	
	public int value;
	public long time;

	// Starts a genetic algorithm
	public GeneticAlgorithm(ArrayList<Integer> values, ArrayList<Integer> weights, int knapsackSize, int generationSize, int numGenerations) {
		// Record the start time
		long startTime = System.currentTimeMillis();

		int numItems = values.size();
		this.values = values;
		this.weights = weights;
		this.maxWeight = knapsackSize;

		// Create a randomized population
		// Each specimen is represented by a bitset
		ArrayList<BitSet> population = new ArrayList<BitSet>();
		Random random = new Random();
		for (int i = 0; i < generationSize; i++) {
			population.add(new BitSet());
			for (int j = 0; j < values.size(); j++) {
				if (random.nextInt(2) == 1) {
					population.get(i).set(j, true); // Randomize each bit in each genome to 0 or 1
				}
				else {
					population.get(i).set(j, false);
				}
			}
		}
		
		// Calculate how many times we need to perform a crossover function
		final int SELECTION_SIZE = (int)(generationSize * SELECTION_RATIO);
		final int ELITE_SIZE = (int)(generationSize * ELITE_RATIO);
		final int CROSSOVER_SIZE = generationSize - ELITE_SIZE;
		for (int gen = 0; gen < numGenerations; gen++) {
			// Sort the specimens in the current generation by fitness
			population.sort((a, b) -> {return fitness(b) - fitness(a);});
			
			// Start building the next generation...
			ArrayList<BitSet> nextGeneration = new ArrayList<BitSet>();
			
			for (int i = 0; i < CROSSOVER_SIZE; i++) {
				// Choose two random specimens from the selection
				BitSet parent1 = population.get(random.nextInt(SELECTION_SIZE));
				BitSet parent2 = population.get(random.nextInt(SELECTION_SIZE));
				
				// Create a new specimen by mixing the parent's genomes
				nextGeneration.add(new BitSet());
				// Uniform Crossover; each individual bit has a change of being from either parent
				for (int j = 0; j < numItems; j++) {
					if (random.nextInt(2) == 0) {
						nextGeneration.get(i).set(j, parent1.get(j)); // Inherit from parent 1
					}
					else {
						nextGeneration.get(i).set(j, parent2.get(j)); // Inherit from parent 2
					}
					
					if (random.nextInt(5) == 0) {
						// Mutation; 1 in 5 chance to flip any inherited bit
						nextGeneration.get(i).flip(j);
					}
				}
			}
			
			// Elitism; take some of the most fit members of the current generation and copy them to the next
			nextGeneration.addAll(population.subList(0, ELITE_SIZE));
			
			// Set the current population to the next generation, then repeat
			population = nextGeneration;
		}
		
		// Get the most fit specimen in the final generation
		population.sort((a, b) -> {return fitness(b) - fitness(a);});
		
		value = fitness(population.get(0));
		
		// Record the execution time
		time = System.currentTimeMillis() - startTime;
	}
	
	// Returns the value of the items this genome collects if it is below the weight threshold
	// Returns 0 if above the weight threshold
	private int fitness(BitSet genome) {
		int totalValue = 0;
		int totalWeight = 0;
		for (int i = 0; i < genome.size(); i++) {
			if (genome.get(i)) {
				totalValue += values.get(i);
				totalWeight += weights.get(i);
			}
		}
		
		// Return the collective value if under the weight limit
		return (totalWeight <= maxWeight) ? totalValue : 0;
	}
}
