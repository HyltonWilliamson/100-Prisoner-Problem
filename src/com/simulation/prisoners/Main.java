package com.simulation.prisoners;
/*
 *  **Hylton Williamson**
 *  **07/2021**
 *
 * This a program that simulates the 100 prisoner problem.
 * Problem:
 * "The 100 prisoners problem is a mathematical problem in probability theory and combinatorics.
 * In this problem, 100 numbered prisoners must find their own numbers in one of 100 drawers in
 * order to survive. The rules state that each prisoner may open only 50 drawers and cannot
 * communicate with other prisoners."[Wikipedia] If all prisoners find their number, they win. If
 * any of the prisoners fail to find their number, they all lose.
 *
 * Worst solution:
 * Each prisoner chooses a drawer at random
 * Chances of success: 7.8886E-29%
 *
 * Basic solution:
 * 1. Check the drawer with your number and check the number inside.
 * 2. If that is your number, move to the next prisoner. If not, check the drawer
 * of that number.
 * 3. Repeat 1-2 until you find your number or run out of chances.
 * Chances of success: ~31%
 *
 * Alternative Adjustment:
 * 1. Check the drawer with your number and check the number inside.
 * 2. If that is your number, move to the next prisoner. If not, check the drawer
 * of that number.
 * 3. If a prisoner gets stuck in a loop, reaching a drawer they have opened before,
 * they choose a random drawer they have not opened.
 * 4. Repeat steps 1-3 until you find your number or run out of chances.
 * Chances of success: ~%31
 *
 * Conclusion:
 * Whether a prisoner blindly follows the contents of the drawer, possibly ending up
 * in a loop, or switches to a new drawer when a loop is encountered, the possibility
 * of choosing the correct drawer is still the same.
 * */

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.print("Enter the number of boxes/prisoners: ");
        Scanner s = new Scanner(System.in);
        int numberOfBoxes = s.nextInt();
        int numberOfPrisoners = numberOfBoxes;
        System.out.print("How many runs? ");
        int runs = s.nextInt();
        System.out.print("How many sets? ");
        int setsOfRuns = s.nextInt();

        double totalPercentages = 0.0;
        double averagePercentage;
        for (int j = 0; j < setsOfRuns; j++) {

            double result;
            int totalWins = 0;
            for (int k = 0; k < runs; k++) {

                int idx = 1;
                ArrayList<Integer> numbers = new ArrayList<>();
                while (numbers.size() < numberOfBoxes) {
                    numbers.add(idx++);
                }

                Random r = new Random();
                int[] drawers = new int[numberOfBoxes];
                int randomIdx;
                idx = 0;
                while (!numbers.isEmpty()) {
                    randomIdx = r.nextInt(numberOfBoxes - idx);
                    drawers[idx++] = numbers.get(randomIdx);
                    numbers.remove(randomIdx);
                }

                int correct = 0;
                int prisonerNumber = 1;
                while (prisonerNumber <= numberOfPrisoners) {
                    int[] exploration = new int[numberOfBoxes];
                    correct += tranverseDrawersBasic(prisonerNumber, prisonerNumber - 1, drawers, 1);
                    //correct += tranverseDrawersAlternative(prisonerNumber, prisonerNumber - 1, drawers, 1, exploration);
                    prisonerNumber++;
                }

                System.out.println(correct + " correct out of " + numberOfPrisoners + " found their number");
                if (correct == numberOfPrisoners) {
                    totalWins++;
                }
            }

            result = ((double) totalWins / (double) runs) * 100;
            System.out.println("Total Wins: " + totalWins + " out of " + runs + " runs.");
            System.out.println("The prisoners had a " + result + "% chance of winning.");
            totalPercentages += result;
            System.out.println("---------------------------------------------------------------------");
        }
        averagePercentage = totalPercentages / setsOfRuns;
        System.out.println(averagePercentage + "% is the average percent");
    }

    public static int tranverseDrawersAlternative(int numberToLookFor, int locationIdx, int[] drawers, int currentAttempt, int[] visited) {
        if (drawers[locationIdx] == numberToLookFor) {
            return 1;
        }
        if (currentAttempt == (drawers.length / 2)) {
            return 0;
        }
        visited[locationIdx] = 1;
        int newLocationIdx = drawers[locationIdx] - 1;
        if (visited[newLocationIdx] == 1 && currentAttempt != (drawers.length / 2)) {

            Random rn = new Random();
            int idx = rn.nextInt(drawers.length - 1);
            newLocationIdx = idx;
            while (visited[idx] != 0) {
                idx = rn.nextInt(drawers.length - 1);
                if (visited[idx] == 0) {
                    newLocationIdx = idx;
                    break;
                }
            }
            return tranverseDrawersAlternative(numberToLookFor, drawers[newLocationIdx], drawers, ++currentAttempt, visited);
        } else {
            return tranverseDrawersAlternative(numberToLookFor, drawers[locationIdx] - 1, drawers, ++currentAttempt, visited);
        }
    }

    public static int tranverseDrawersBasic(int numberToLookFor, int locationIdx, int[] drawers, int currentAttempt) {
        if (drawers[locationIdx] == numberToLookFor) {
            return 1;
        }
        if (currentAttempt == (drawers.length / 2)) {
            return 0;
        }
        return tranverseDrawersBasic(numberToLookFor, drawers[locationIdx] - 1, drawers, ++currentAttempt);
    }
}
