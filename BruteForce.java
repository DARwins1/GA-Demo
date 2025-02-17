import java.util.ArrayList;
import java.util.BitSet;

public class BruteForce {
	public int value;
	public long time;

	public BruteForce(ArrayList<Integer> values, ArrayList<Integer> weights, int maxWeight) {
		// Record the start time
		long startTime = System.currentTimeMillis();

		BitSet comb = new BitSet();
		int idx = 0;
		while (!comb.get(values.size())) {
			if (!comb.get(idx)) {
				// If the bit at idx is set to 0...
				comb.set(0, idx, false); // Set all previous bits to 0
				comb.set(idx, true); // Set the bit at idx to 1
				idx = 0; // Send idx to the first bit
			} else {
				// Otherwise...
				comb.set(idx, true); // Set the bit at idx to 1
				idx++; // Increment idx
			}

			if (!comb.get(values.size())) {
				// Get the value of this combination...
				int totalValue = 0;
				int totalWeight = 0;
				for (int i = 0; i < comb.length(); i++) {
					if (comb.get(i)) {
						totalValue += values.get(i);
						totalWeight += weights.get(i);
					}
				}

				if (totalWeight <= maxWeight && totalValue > value) {
					// If this is the best solution we've found so far, store it.
					value = totalValue;
				}
			}

		}

		// Record the execution time
		time = System.currentTimeMillis() - startTime;
	}
}
