package scoreSystem;

/**
 * Generates the probability of winning, tying, or losing a competition with a
 * certain amount of games. Can be used with custom scoring system or default
 * (the point value increases by 1 after every game).
 * 
 * @author Clayton Allard
 */
public class ScoringSystem {

	public static void main(String[] args) {
		System.out.println(probOfTie(16, .50));
	}

	/**
	 * Returns the exact probability of a certain outcome.
	 * 
	 * @param arr           - the array in which we insert the binary outcomes.
	 * @param val           - the value being converted to binary
	 * @param probOfWinning - probability of winning each game.
	 * @return probability of the entire sequence of events.
	 * @throws IllegalStateException - throws if the value or probability of winning
	 *                               is out of bounds.
	 */
	private static double binaryConverterArray(int[] arr, int val, double probOfWinning) throws IllegalStateException {
		if (val < 0 || val >= Math.pow(2, arr.length) || probOfWinning < 0 || probOfWinning > 1) {
			throw new IllegalStateException();
		}
		int timesToLoop = 0;
		// amount of times person of interest wins.
		int countOfOnes = 0;
		// convert to binary
		while (val != 0) {
			if (val % 2 == 1) {
				countOfOnes++;
			}
			arr[timesToLoop] = val % 2;
			val /= 2;
			timesToLoop++;
		}
		// probability of getting exact result. (p)^wins * (p)^losses.
		return Math.pow(probOfWinning, countOfOnes) * Math.pow(1 - probOfWinning, arr.length - countOfOnes);
	}

	/**
	 * Automatically sets the scoring system to increment by 1 if it is unspecified.
	 * 
	 * @param arr - the arr that contains the scoring system per race.
	 */
	private static void generateGameValues(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = i + 1;
		}
	}

	/**
	 * Generates every possible sum of every possible sequence of events and is
	 * stored into an array of size 2^number of games. This is called if the scoring
	 * system is unspecified. Then we just assume that we increment by 1 for each
	 * game.
	 * 
	 * @param numberOfGames - the number of games in the competition.
	 * @return an array of 2^numberOfGames of every possible outcome.
	 */
	private static double[] generateTotalSums(int numberOfGames) {
		// if the scoring system isn't specified, generate the default one.
		int[] arr = new int[numberOfGames];
		generateGameValues(arr);
		// create the array of all outcomes.
		double[] totalSums = new double[(int) Math.pow(2, arr.length)];
		// convert each i into binary which will all be unique.
		for (int i = 0; i < totalSums.length; i++) {
			int[] binaryArray = new int[arr.length];
			binaryConverterArray(binaryArray, i, .5);
			for (int j = 0; j < arr.length; j++) {
				// each element of the binaryArray acts as an indicator.
				totalSums[i] += binaryArray[j] * arr[j];
			}
		}
		return totalSums;
	}

	/**
	 * Generates every possible sum of every possible sequence of events and is
	 * stored into an array of size 2^number of games. This is called if the scoring
	 * system is specified and we use it.
	 * 
	 * @param pointValueList - the custom scoring system.
	 * @return an array of 2^numberOfGames of every possible outcome.
	 */
	private static double[] generateTotalSums(double[] pointValueList) {
		// create the array of all outcomes.
		double[] totalSums = new double[(int) Math.pow(2, pointValueList.length)];
		// convert each i into binary which will all be unique.
		for (int i = 0; i < totalSums.length; i++) {
			int[] binaryArray = new int[pointValueList.length];
			binaryConverterArray(binaryArray, i, .5);
			for (int j = 0; j < pointValueList.length; j++) {
				// each element of the binaryArray acts as an indicator.
				totalSums[i] += binaryArray[j] * pointValueList[j];
			}
		}
		return totalSums;
	}

	/**
	 * The array that has one-to-one correspondence to the array of the total sum.
	 * Represents the probability of each outcome.
	 * 
	 * @param numberOfGames - number of games in the competition.
	 * @param probOfWinning - probability of winning each game.
	 * @return an array of 2^numberOfGames of the probability of every possible
	 *         outcome.
	 */
	private static double[] generateProbMass(int numberOfGames, double probOfWinning) {
		// scoring system doesn't matter for the sequence of wins.
		int[] arr = new int[numberOfGames];
		generateGameValues(arr);
		double[] probMass = new double[(int) Math.pow(2, arr.length)];
		// loop through every combination of a list of 0,1 in given list size.
		for (int i = 0; i < probMass.length; i++) {
			int[] binaryArray = new int[arr.length];
			double probOfOutcome = binaryConverterArray(binaryArray, i, probOfWinning);
			probMass[i] = probOfOutcome;
		}
		return probMass;
	}

	/**
	 * For the default (score increments by one after every race) scoring system,
	 * the value at which the tie occurs.
	 * 
	 * @param numberOfGames - number of games played in the competition.
	 * @return the value at which a tie occurs.
	 * @throws IllegalStateException - throws if number of games is less than 0.
	 */
	public static double valueToTie(int numberOfGames) throws IllegalStateException {
		if (numberOfGames < 0) {
			throw new IllegalStateException();
		}
		return (double) numberOfGames * (numberOfGames + 1) / 4;
	}

	/**
	 * For the custom scoring system, the value at which the tie occurs.
	 * 
	 * @param pointValueList - the custom scoring system.
	 * @return the value at which a tie occurs.
	 */
	public static double valueToTie(double[] pointValueList) {
		double targetValue = 0;
		for (int i = 0; i < pointValueList.length; i++) {
			targetValue += (double) pointValueList[i];
		}
		targetValue = (double) targetValue / 2;
		return targetValue;
	}

	/**
	 * For the default scoring system (score increments by one after every race).
	 * The probability of tying after every game in the competition is played given
	 * the probability of winning each race.
	 * 
	 * @param numberOfGames - number of games played in the competition.
	 * @param probOfWinning - probability of winning each game.
	 * @return probability of both competitors tying.
	 */
	public static double probOfTie(int numberOfGames, double probOfWinning) {
		// calling helper method to prevent the user from having the burden of calling
		// this themselves.
		return probOfTie(generateTotalSums(numberOfGames), generateProbMass(numberOfGames, probOfWinning),
				numberOfGames);
	}

	/**
	 * For the custom made scoring system. The probability of tying after every game
	 * in the competition is played given the probability of winning each race.
	 * 
	 * @param pointValueList - the custom scoring system.
	 * @param probOfWinning  - probability of winning each game.
	 * @return probability of both competitors tying.
	 */
	public static double probOfTie(double[] pointValueList, double probOfWinning) {
		// since the probability mass array doesn't depend on the point system.
		int numberOfGames = pointValueList.length;
		return probOfTie(generateTotalSums(pointValueList), generateProbMass(numberOfGames, probOfWinning),
				pointValueList);
	}

	/**
	 * This is the method where the work is actually done for the default scoring
	 * system. Iterates through every outcome and returns the proportion of scores
	 * that matches a tie.
	 * 
	 * @param totalSums     - the array of all possible outcomes.
	 * @param probMass      - the array of the probabilities of all possible
	 *                      outcomes.
	 * @param numberOfGames - number of games in the competition.
	 * @return probability of both competitors tying.
	 */
	private static double probOfTie(double[] totalSums, double[] probMass, int numberOfGames) {
		// the tying score.
		double targetValue = valueToTie(numberOfGames);
		double probability = 0;
		// adding the probability of each outcome where a tie occurs.
		for (int i = 0; i < totalSums.length; i++) {
			if (totalSums[i] == targetValue) {
				probability += probMass[i];
			}
		}
		return (double) probability;
	}

	/**
	 * This is the method where the work is actually done for the custom scoring
	 * system. Iterates through every outcome and returns the proportion of scores
	 * that matches a tie.
	 * 
	 * @param totalSums      - the array of all possible outcomes.
	 * @param probMass       - the array of the probabilities of all possible
	 *                       outcomes.
	 * @param pointValueList - the custom scoring system.
	 * @return probability of both competitors tying.
	 */
	private static double probOfTie(double[] totalSums, double[] probMass, double[] pointValueList) {
		// generating the tying score.
		double targetValue = valueToTie(pointValueList);
		double probability = 0;
		for (int i = 0; i < totalSums.length; i++) {
			if (totalSums[i] == targetValue) {
				probability += probMass[i];
			}
		}
		return (double) probability;
	}

	/**
	 * For the default scoring system (score increments by one after every race).
	 * The probability of winning after every game in the competition is played
	 * given the probability of winning each race. For ties, it is settled by
	 * treating it as an extra race. So the probability of winning the tiebreaker is
	 * probOfWinning.
	 * 
	 * @param numberOfGames - number of games played in the competition.
	 * @param probOfWinning - probability of winning each game.
	 * @return probability of player winning.
	 */
	public static double probOfWinning(int numberOfGames, double probOfWinning) {
		return probOfWinning(generateTotalSums(numberOfGames), generateProbMass(numberOfGames, probOfWinning),
				numberOfGames, probOfWinning);
	}

	/**
	 * For the custom scoring system. The probability of winning after every game in
	 * the competition is played given the probability of winning each race. For
	 * ties, it is settled by treating it as an extra race. So the probability of
	 * winning the tiebreaker is probOfWinning.
	 * 
	 * @param pointValueList - the custom scoring system.
	 * @param probOfWinning  - probability of winning each game.
	 * @return probability of player winning.
	 */
	public static double probOfWinning(double[] pointValueList, double probOfWinning) {
		int numberOfGames = pointValueList.length;
		return probOfWinning(generateTotalSums(pointValueList), generateProbMass(numberOfGames, probOfWinning),
				pointValueList, probOfWinning);
	}

	/**
	 * For the default scoring system. The probability of winning after every game
	 * in the competition is played given the probability of winning each race. For
	 * ties, it is settled by treating it as an extra race. So the probability of
	 * winning the tiebreaker is probOfWinning.
	 * 
	 * @param numberOfGames - number of games played in the competition.
	 * @param probOfWinning - probability of winning each game.
	 * @return probability of player winning.
	 */
	private static double probOfWinning(double[] totalSums, double[] probMass, int numberOfGames,
			double probOfWinning) {
		double targetValue = valueToTie(numberOfGames);
		double probability = 0;
		for (int i = 0; i < totalSums.length; i++) {
			if (totalSums[i] > targetValue) {
				probability += probMass[i];
				// if there is a tie, play a tiebreaker.
			}
		}
		return (double) probability;
	}

	/**
	 * For the custom scoring system. The probability of winning after every game in
	 * the competition is played given the probability of winning each race. For
	 * ties, it is settled by treating it as an extra race. So the probability of
	 * winning the tiebreaker is probOfWinning.
	 * 
	 * @param pointValueList - the custom scoring system.
	 * @param probOfWinning  - probability of winning each game.
	 * @return probability of player winning.
	 */
	private static double probOfWinning(double[] totalSums, double[] probMass, double[] pointValueList,
			double probOfWinning) {
		double targetValue = valueToTie(pointValueList);
		double probability = 0;
		for (int i = 0; i < totalSums.length; i++) {
			if (totalSums[i] > targetValue) {
				probability += probMass[i];
			}
		}
		return (double) probability;
	}

	/**
	 * For the default scoring system (score increments by one after every race).
	 * The probability of losing after every game in the competition is played given
	 * the probability of winning each race. For ties, it is settled by treating it
	 * as an extra race. So the probability of winning the tiebreaker is
	 * probOfWinning.
	 * 
	 * @param numberOfGames - number of games played in the competition.
	 * @param probOfWinning - probability of winning each game.
	 * @return probability of player losing.
	 */
	public static double probOfLosing(int numberOfGames, double probOfWinning) {
		return probOfLosing(generateTotalSums(numberOfGames), generateProbMass(numberOfGames, probOfWinning),
				numberOfGames, probOfWinning);
	}

	/**
	 * For the custom scoring system. The probability of losing after every game in
	 * the competition is played given the probability of winning each race. For
	 * ties, it is settled by treating it as an extra race. So the probability of
	 * winning the tiebreaker is probOfWinning.
	 * 
	 * @param pointValueList - the custom scoring system.
	 * @param probOfWinning  - probability of winning each game.
	 * @return probability of player losing.
	 */
	public static double probOfLosing(double[] pointValueList, double probOfWinning) {
		int numberOfGames = pointValueList.length;
		return probOfLosing(generateTotalSums(pointValueList), generateProbMass(numberOfGames, probOfWinning),
				pointValueList, probOfWinning);
	}

	/**
	 * For the default scoring system. The probability of losing after every game in
	 * the competition is played given the probability of winning each race. For
	 * ties, it is settled by treating it as an extra race. So the probability of
	 * winning the tiebreaker is probOfWinning.
	 * 
	 * @param numberOfGames - number of games played in the competition.
	 * @param probOfWinning - probability of winning each game.
	 * @return probability of player losing.
	 */
	private static double probOfLosing(double[] totalSums, double[] probMass, int numberOfGames, double probOfWinning) {
		double targetValue = valueToTie(numberOfGames);
		double probability = 0;
		for (int i = 0; i < totalSums.length; i++) {
			if (totalSums[i] < targetValue) {
				probability += probMass[i];
			} 
		}
		return (double) probability;
	}

	/**
	 * For the custom scoring system. The probability of losing after every game in
	 * the competition is played given the probability of winning each race. For
	 * ties, it is settled by treating it as an extra race. So the probability of
	 * winning the tiebreaker is probOfWinning.
	 * 
	 * @param pointValueList - the custom scoring system.
	 * @param probOfWinning  - probability of winning each game.
	 * @return probability of player losing.
	 */
	private static double probOfLosing(double[] totalSums, double[] probMass, double[] pointValueList,
			double probOfWinning) {
		double targetValue = valueToTie(pointValueList);
		double probability = 0;
		for (int i = 0; i < totalSums.length; i++) {
			if (totalSums[i] < targetValue) {
				probability += probMass[i];
			} 
		}
		return (double) probability;
	}

	/**
	 * For the default scoring system (score increments by one after every race).
	 * The probability of reaching a certain score threshold after every game in the
	 * competition is played given the probability of winning each race.
	 * 
	 * @param numberOfGames - number of games played in the competition.
	 * @param probOfWinning - probability of winning each game.
	 * @param targetValue   - the score threshold.
	 * @return probability of reaching score threshold.
	 */
	public static double probOfScore(int numberOfGames, double probOfWinning, double targetValue) {
		return probOfScore(generateTotalSums(numberOfGames), generateProbMass(numberOfGames, probOfWinning),
				numberOfGames, targetValue);
	}

	/**
	 * For the custom scoring system. The probability of reaching a certain score
	 * threshold after every game in the competition is played given the probability
	 * of winning each race.
	 * 
	 * @param pointValueList - the custom scoring system.
	 * @param probOfWinning  - probability of winning each game.
	 * @param targetValue    - the score threshold.
	 * @return probability of reaching score threshold.
	 */
	public static double probOfScore(double[] pointValueList, double probOfWinning, double targetValue) {
		int numberOfGames = pointValueList.length;
		return probOfScore(generateTotalSums(pointValueList), generateProbMass(numberOfGames, probOfWinning),
				pointValueList, targetValue);
	}

	/**
	 * For the default scoring system (score increments by one after every race).
	 * The probability of reaching a certain score threshold after every game in the
	 * competition is played given the probability of winning each race.
	 * 
	 * @param totalSums     - the array of all possible outcomes.
	 * @param probMass      - the array of the probabilities of all possible
	 *                      outcomes.
	 * @param numberOfGames - number of games played in the competition.
	 * @param targetValue   - the score threshold.
	 * @return probability of reaching score threshold.
	 */
	private static double probOfScore(double[] totalSums, double[] probMass, int numberOfGames, double targetValue) {
		double probability = 0;
		for (int i = 0; i < totalSums.length; i++) {
			if (totalSums[i] >= targetValue) {
				probability += probMass[i];
			}
		}
		return (double) probability;
	}

	/**
	 * For the custom. The probability of reaching a certain score threshold after
	 * every game in the competition is played given the probability of winning each
	 * race.
	 * 
	 * @param totalSums      - the array of all possible outcomes.
	 * @param probMass       - the array of the probabilities of all possible
	 *                       outcomes.
	 * @param pointValueList - the custom scoring system.
	 * @param targetValue    - the score threshold.
	 * @return probability of reaching score threshold.
	 */
	private static double probOfScore(double[] totalSums, double[] probMass, double[] pointValueList,
			double targetValue) {
		double probability = 0;
		for (int i = 0; i < totalSums.length; i++) {
			if (totalSums[i] >= targetValue) {
				probability += probMass[i];
			}
		}
		return (double) probability;
	}
}
