/**
 * @author Mark Wang
 * 12 14 2021
 *
 * Hangman Game Program
 *
 * List of concepts not learned in class (with reference links):
 *
 *      1. StringBuilder
 *         Link: https://www.geeksforgeeks.org/stringbuilder-class-in-java-with-examples/
 *
 *      2. ArrayList
 *         Link: https://www.w3schools.com/java/java_arraylist.asp
 *
 *      3. String.valueOf()
 *         Link: https://www.javatpoint.com/java-string-valueof
 *
 *      4. Collections.sort()
 *         Link: https://beginnersbook.com/2013/12/how-to-sort-arraylist-in-java/
 *
 */

import java.io.*;
import java.util.*;

public class Game {

    public static void main(String[] args) throws Exception {

        Scanner getProcedure = new Scanner(System.in);
        String proceed = "";

        while (!proceed.equalsIgnoreCase("Quit")) {

            System.out.println("Welcome to Hangman!");
            System.out.println("How would you like to proceed? (Type one of the following)");
            System.out.println("Play       Rules       Quit");
            proceed = getProcedure.next();

            if (proceed.equalsIgnoreCase("Quit")) {
                System.out.println("");
                System.out.println("Thank you for playing Hangman, we hope to see you again soon!");
                break;
            } else if (proceed.equalsIgnoreCase("Rules")) {
                showRules();
            } else {
                playGame();
            }
        }

        getProcedure.close();
    }

    public static void showRules() throws Exception {

        File rules = new File("Rules.txt");
        Scanner getRules = new Scanner(rules);

        System.out.println("");

        while (getRules.hasNextLine()) {
            System.out.println(getRules.nextLine());
        }

        System.out.println("");
        getRules.close();
    }

    public static void showHangman (int lives, List<String> alreadyGuessed) throws Exception {

        String pictureNum = "HangPic" + (10-lives+1) + ".txt";
        File hangPicture = new File(pictureNum);
        Scanner showPicture = new Scanner(hangPicture);

        System.out.println("");
        while (showPicture.hasNextLine()) {
            System.out.println(showPicture.nextLine());
        }

        System.out.println("");
        showPicture.close();
        Collections.sort(alreadyGuessed); // Sort lexicographically

        System.out.print("Letters already guessed: ");
        for (int i = 0; i < alreadyGuessed.size(); i++) {
            System.out.print(alreadyGuessed.get(i) + " ");
        }
        System.out.println("\n");
    }

    public static void playGame() throws Exception {

        Scanner gameInput = new Scanner(System.in);

        System.out.println("");
        System.out.println("Welcome to the Hangman Game!");
        System.out.println("Player 1, please enter a word or phrase: ");
        String chosenWord = gameInput.nextLine();
        String chosenLowerCase = chosenWord.toLowerCase();

        // To ensure that Player 2 does not see the word entered and does not cheat
        for (int i = 0; i < 10; i++) {
            System.out.println("\n");
        }

        String wordReplace = "";

        for (int i = 0; i < chosenWord.length(); i++) {
            if (chosenWord.charAt(i) == ' ') {
                wordReplace += " ";
            } else {
                wordReplace += "_";
            }
        }

        StringBuilder replaceBuilder = new StringBuilder(wordReplace.toLowerCase());
        int lives = 10;
        boolean p2Won = false;
        List<String> alreadyGuessed = new ArrayList<>();

        while (lives != 0) {
            if (replaceBuilder.toString().equals(chosenWord)) {
                p2Won = true;
                break;
            }

            showHangman(lives, alreadyGuessed);

            if (lives > 1) {
                System.out.println("Player 2, you have " + lives + " lives left.");
            } else if (lives == 1) {
                System.out.println("Player 2, this is your last life!");
            } else {
                break;
            }

            System.out.println("You are guessing: " + replaceBuilder);
            System.out.println("");
            System.out.println("Enter a letter or a word/phrase: ");
            String guess = gameInput.nextLine().toLowerCase();

            if (guess.length() == 1 && alreadyGuessed.contains(guess)) {
                System.out.println("You have already guessed this letter. Please try again.");
                continue;
            }

            if (guess.length() > 1) {
                if (guess.equalsIgnoreCase(chosenWord)) {
                    p2Won = true;
                    break;
                } else {
                    System.out.println("Sorry, that is not the chosen word/phrase.");
                    lives--;
                }
            } else {
                int index = chosenLowerCase.indexOf(guess);
                alreadyGuessed.add(guess);

                if (index == -1) {
                    System.out.println("Sorry, that letter is not in the word/phrase.");
                    lives--;
                } else {
                    System.out.println("The letter you guessed is inside the word!");
                    replaceBuilder.replace(index, index + 1, String.valueOf(chosenWord.charAt(index)));

                    for (int i = 1; i < chosenWord.length(); i++) {
                        index = chosenLowerCase.indexOf(guess, i);
                        if (index == -1) {
                            break;
                        } else {
                            replaceBuilder.replace(index, index + 1, String.valueOf(chosenWord.charAt(index)));
                        }
                    }
                }
            }
        }

        if (p2Won) {
            System.out.println("Congratulations, Player 2, you won the game!");
        } else {
            System.out.println("You ran out of lives. Player 1 wins!");
        }

        System.out.println("");

        // We do not close the gameInput scanner to avoid NoSuchElementException error when going back to the menu
    }
}