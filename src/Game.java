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
 *      5. Regexes in replaceAll(). See below to see the list of all regular expressions in Java
 *         Link: https://www.vogella.com/tutorials/JavaRegularExpressions/article.html
 *
 *      6. "continue" statement
 *         Link: https://www.javatpoint.com/java-continue
 *
 *      6. indexOf()
 *         Link: https://www.w3schools.com/java/ref_string_indexof.asp
 *
 *      7. StringBuilder .replace() method
 *         Link: https://www.tutorialspoint.com/java/lang/stringbuilder_replace.htm
 *
 */

import java.io.*;
import java.util.*;

public class Game {

    // Declare a static game input scanner as it will be used in multiple different methods
    static Scanner gameInput = new Scanner(System.in);

    /**
     * The main method will be the menu
     * The user will be directed to the menu when the program is first ran and
     * when the procedure he/she chose is finished executing
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // Declare scanner to get procedure inputs and a String that takes the value of the procedure input
        Scanner getProcedure = new Scanner(System.in);
        String proceed = "";

        // Main menu loop (keeps repeating until the user wants to exit the game)
        while (!proceed.equalsIgnoreCase("Quit")) {

            // Welcoming statement and get procedure
            System.out.println("Welcome to Hangman!");
            System.out.println("How would you like to proceed? (Type one of the following)");
            System.out.println("Play       Rules       Quit");
            proceed = getProcedure.next();

            // Corresponding if-statement will br executed based on procedure input
            if (proceed.equalsIgnoreCase("Quit")) {
                // Goodbye message and exit the program by breaking the loop
                System.out.println("");
                System.out.println("Thank you for playing Hangman, we hope to see you again soon!");
                break;
            } else if (proceed.equalsIgnoreCase("Rules")) {
                // Ask which type of rules they want to see and pass the choice into the showRules method
                System.out.println("");
                System.out.println("Would you like to see the Singleplayer rules or Multiplayer rules?");
                System.out.println("Please enter \"1\" for Singleplayer and \"2\" for Multiplayer");
                int ruleType = getProcedure.nextInt();
                showRules(ruleType);
            } else {
                // Ask which game mode they want to play and pass the choice into the corresponding method
                System.out.println("");
                System.out.println("Would you like to play Singleplayer or Multiplayer?");
                System.out.println("Please enter \"1\" for Singleplayer and \"2\" for Multiplayer");
                int gamemodeType = getProcedure.nextInt();

                if (gamemodeType == 1) {
                    playSingle();
                } else {
                    playMulti();
                }
            }
        }

        getProcedure.close(); // Close scanner then program is finished
    }

    /**
     * This method will show the user the rules based on which type of rules they want to see
     * This is achieved by taking ruleType from the main method as an argument
     * and only showing the user the txt file (which contain the rules themselves) that
     * corresponds to the type of rules they want to see
     * @param ruleType
     * @throws Exception
     */
    public static void showRules (int ruleType) throws Exception {

        File rules; // Declare file object

        // Initialize file object based on the argument ruleType
        if (ruleType == 1) {
            rules = new File("RulesSingle.txt");
        } else {
            rules = new File("RulesMulti.txt");
        }

        // Declare scanner that will read the file and help output them to the screen
        Scanner getRules = new Scanner(rules);
        System.out.println("");

        // While the file has another line to read, advance the scanner and output the line read
        while (getRules.hasNextLine()) {
            System.out.println(getRules.nextLine());
        }

        System.out.println("");
        getRules.close(); // Close the scanner when the file has successfully been read
    }

    /**
     *  This method will show the corresponding hangman image based on how
     *  many lives the user has left while also telling the user all the letters he/she have already guessed.
     *  This method is used in both the single player and multiplayer game methods
     * @param lives
     * @param alreadyGuessed
     * @throws Exception
     */
    public static void showHangman (int lives, List<String> alreadyGuessed) throws Exception {

        // Determine which file to show the user based on how many lives he/she has
        // The algorithm is (starting lives - current lives) + 1
        String pictureNum = "HangPic" + (10-lives+1) + ".txt";
        File hangPicture = new File(pictureNum);
        Scanner showPicture = new Scanner(hangPicture); // Scanner to read the file

        System.out.println("");

        // While the file has another line to read, advance scanner and output the line read
        // This while loop will be the loop that will show the actual picture, not the letters already guessed
        while (showPicture.hasNextLine()) {
            System.out.println(showPicture.nextLine());
        }

        System.out.println("");
        showPicture.close(); // Close scanner after picture has been shown
        Collections.sort(alreadyGuessed); // Sort the ArrayList lexicographically
        System.out.print("Letters already guessed: ");

        // This loop will loop through all the elements in the alreadyGuessed ArrayList
        // Every element represents a letter the user already guessed, and we must print all of them out
        for (int i = 0; i < alreadyGuessed.size(); i++) {
            System.out.print(alreadyGuessed.get(i) + " ");
        }
        System.out.println("\n");
    }

    /**
     * This will be the single player game method and will run the
     * single player game mode (User vs. Computer)
     * @throws Exception
     */
    public static void playSingle() throws Exception {

        // Welcoming statement and ask user for the category he/she wants the word to be related to
        System.out.println("");
        System.out.println("Welcome to the Hangman Singleplayer Game!");
        System.out.println("Please choose a category listed below by typing it with correct capitalization and spelling: ");
        System.out.println("Fruits | Famous People | Brands and Platforms | Festivities | Movies and Shows");
        String category = gameInput.nextLine() + ".txt"; // Category preferred will be stored in this variable

        // Declare file and scanner objects on the corresponding file to the category the user wanted
        File categoryFile = new File(category);
        Scanner chooseWord = new Scanner(categoryFile);
        int lineCount = 0; // This counter variable will keep track of how many lines the file has

        // This while loop will update the lineCount variable
        while (chooseWord.hasNextLine()) {
            chooseWord.nextLine();
            lineCount++;
        }

        // Declare array that will take in all the words in the file
        // The array's length is the same as the number of lines in this file
        String[] words = new String[lineCount];

        // Close and re-initialize scanner as we have to use this scanner again at the beginning of the file
        chooseWord.close();
        chooseWord = new Scanner(categoryFile);

        // This for loop will fill the array with the words
        for (int i = 0; i < lineCount; i++) {
            words[i] = chooseWord.nextLine();
        }
        chooseWord.close(); // Close the scanner

        // Math.random() to choose a random word from the array
        // Algorithm: Math.random() * highest index (1 less than the array's length) + lowest index (0)
        String chosenWord = words[(int) (Math.random()*(words.length-1))];

        // Lowercase version of the word to prevent correct letter guesses but
        // with different capitalization than in the actual word to be considered incorrect
        // This will become more clear later on in the program
        String chosenLowerCase = chosenWord.toLowerCase();
        String wordReplace = ""; // This will be the underscore representation of the word shown to the user

        // This for loop will keep appending to the wordReplace string until it is the desired String
        for (int i = 0; i < chosenWord.length(); i++) {
            // Check if the letter is alphabetical or not by using a replaceAll method
            if (String.valueOf(chosenWord.charAt(i)).replaceAll("[^A-Za-z]", "").length() == 0) {
                // Append the character if it is not alphabetical
                // This includes: [. ! ' , " ?]  etc.
                wordReplace += chosenWord.charAt(i);
            } else {
                // Append an underscore if the character is alphabetical
                wordReplace += "_";
            }
        }

        // Declare StringBuilder object and point it to the wordReplace object
        // This is because will be manipulating wordReplace a lot as every round
        // this will be shown to the user
        StringBuilder replaceBuilder = new StringBuilder(wordReplace);
        int lives = 10; // Starting lives is 10
        boolean hasWon = false; // Boolean to keep track whether the user won or lost
        List<String> alreadyGuessed = new ArrayList<>(); // ArrayList that contains all the already-guessed Letters

        // Main game loop that will keep incrementing until the user either won or lost
        while (lives != 0) {
            // If the StringBuilder equals the word, then the user has won
            if (replaceBuilder.toString().equals(chosenWord)) {
                hasWon = true; // Assign hasWon to true since the user won
                break; // Exit loop
            }

            // Show corresponding picture based on the number of lives
            // Show every letter the user has already guessed
            showHangman(lives, alreadyGuessed);

            // Inform the user how many lives he/she has
            // The statement will be plural if the user has more than 1 life
            // The statement will be singular if the user only has 1 life left
            if (lives > 1) {
                System.out.println("You have " + lives + " lives left.");
            } else if (lives == 1) {
                System.out.println("This is your last life!");
            } else {
                break; // Break out of the loop when the user ran out of lives
            }

            // Show the word/phrase they are trying to guess
            // This will first be a set of underscores and other characters
            // But every letter the user guessed that is in the word will be shown
            // and updated in every iteration
            System.out.println("You are guessing: " + replaceBuilder);
            System.out.println("");

            // Prompt and store the user's guess, whether it be a letter or the word/phrase itself
            System.out.println("Enter a letter or a word/phrase: ");

            // Notice how the guess is always lowercase, this is to prevent duplicate letter guesses
            // But with different capitalization such as "a" and "A"
            String guess = gameInput.nextLine().toLowerCase();

            // If the guess is a single letter, and it is a letter the user already guessed
            // then re-iterate the loop and prompt the user once again
            if (guess.length() == 1 && alreadyGuessed.contains(guess)) {
                System.out.println("You have already guessed this letter. Please try again.");
                continue; // Ignore rest of the loop and re-iterate
            }

            // If the guess's length is more than one, then it must be assumed that the user
            // is attempting to guess the word/phrase, in which case we check whether it is
            // the correct one or not
            if (guess.length() > 1) {
                // If the guess equals the word chosen, then the user has won, and we can break out of the loop
                if (guess.equalsIgnoreCase(chosenWord)) {
                    hasWon = true; // Assign hasWon to true since the user won
                    break;
                } else { // If they aren't the same, then inform the user and subtract a life
                    System.out.println("Sorry, that is not the chosen word/phrase.");
                    lives--;
                }
            } else { // If the previous if-statement didn't run, then the user must have guessed a letter
                // Create index variable that searches the first index of the letter inside the lowercased word
                int index = chosenLowerCase.indexOf(guess);
                alreadyGuessed.add(guess);

                // If the value is -1, then that means that the letter is not inside the word/phrase
                // In which case we inform the user and subtract a life
                if (index == -1) {
                    System.out.println("Sorry, that letter is not in the word/phrase.");
                    lives--;
                } else {
                    // If the previous if-statement didn't run, then
                    // the letter guessed occurs at least once inside the word/phrase
                    System.out.println("The letter you guessed is inside the word!");
                    // We replace the first corresponding underscore to the letter that the user guessed
                    replaceBuilder.replace(index, index + 1, String.valueOf(chosenWord.charAt(index)));

                    // This for loop will replace every corresponding underscore with the letter
                    // that the user has guessed. This for loop will keep incrementing until index is -1
                    for (int i = 1; i < chosenWord.length(); i++) {
                        // Note: the second value in the indexOf() method
                        // corresponds to the first index to scan
                        // Consider the word "Comfort"
                        // If we do not tell the program which index to begin scanning,
                        // Then the program will only scan the first "o" and not the second
                        index = chosenLowerCase.indexOf(guess, i);
                        if (index == -1) { // If index is -1, break out of the loop
                            break;
                        } else {
                            // If the index is still not -1, then replace the corresponding underscore
                            // to the letter guessed, accordingly
                            replaceBuilder.replace(index, index + 1, String.valueOf(chosenWord.charAt(index)));
                        }
                    }
                }
            }
        }

        // If the user won, congratulate the user
        // Otherwise, inform the user that he/she ran out of lives, and it's Game Over
        if (hasWon) {
            System.out.println("Congratulations, you won the game!");
        } else {
            System.out.println("Game over. You ran out of lives.");
        }

        System.out.println("");

        // We do not close the gameInput scanner to avoid NoSuchElementException error when going back to the menu
        // Description of error: https://docs.oracle.com/javase/8/docs/api/java/util/NoSuchElementException.html
    }

    /**
     * This will be the multiplayer (two-player) game method and will run the
     * multiplayer game mode (Player 1 vs. Player 2)
     * @throws Exception
     */
    public static void playMulti() throws Exception {

        // Welcoming statement and ask Player 1 to enter the word/phrase that he/she wants Player 2 to guess
        System.out.println("");
        System.out.println("Welcome to the Hangman Multiplayer Game!");
        System.out.println("Player 1, please enter a word or phrase: ");
        String chosenWord = gameInput.nextLine();
        // Just like in the single player method, this word will also be converted into lowercase
        String chosenLowerCase = chosenWord.toLowerCase();

        // To ensure that Player 2 does not see the word entered and does not cheat
        for (int i = 0; i < 10; i++) {
            System.out.println("\n");
        }

        String wordReplace = ""; // This will be the underscore representation of the word shown to Player 2

        // This for loop will keep appending to the wordReplace string until it is the desired String
        for (int i = 0; i < chosenWord.length(); i++) {
            // Check if the letter is alphabetical or not by using a replaceAll method
            if (String.valueOf(chosenWord.charAt(i)).replaceAll("[^A-Za-z]", "").length() == 0) {
                // Append the character if it is not alphabetical
                // This includes: [. ! ' , " ?]  etc.
                wordReplace += chosenWord.charAt(i);
            } else {
                // Append an underscore if the character is alphabetical
                wordReplace += "_";
            }
        }

        // Declare StringBuilder object and point it to the wordReplace object
        // This is because will be manipulating wordReplace a lot as every round
        // this will be shown to Player 2
        StringBuilder replaceBuilder = new StringBuilder(wordReplace);
        int lives = 10; // Starting lives is 10
        boolean p2Won = false; // Boolean to keep track whether Player 2 has won or lost
        List<String> alreadyGuessed = new ArrayList<>(); // ArrayList that contains all the already-guessed Letters

        // Main game loop that will keep incrementing until Player 2 either won or lost
        while (lives != 0) {
            // If the StringBuilder equals the word, then Player 2 has won
            if (replaceBuilder.toString().equals(chosenWord)) {
                p2Won = true; // Assign hasWon to true since Player 2 won
                break; // Exit loop
            }

            // Show corresponding picture based on the number of lives
            // Show every letter Player 2 has already guessed
            showHangman(lives, alreadyGuessed);

            // Inform Player 2 how many lives he/she has
            // The statement will be plural if Player 2 has more than 1 life
            // The statement will be singular if Player 2 only has 1 life left
            if (lives > 1) {
                System.out.println("Player 2, you have " + lives + " lives left.");
            } else if (lives == 1) {
                System.out.println("Player 2, this is your last life!");
            } else {
                break; // Break out of the loop when Player 2 ran out of lives
            }

            // Show the word/phrase they are trying to guess
            // This will first be a set of underscores and other characters
            // But every letter Player 2 guessed that is in the word will be shown
            // and updated in every iteration
            System.out.println("You are guessing: " + replaceBuilder);
            System.out.println("");

            // Prompt and store Player 2's guess, whether it be a letter or the word/phrase itself
            System.out.println("Enter a letter or a word/phrase: ");

            // Notice how the guess is always lowercase, this is to prevent duplicate letter guesses
            // But with different capitalization such as "a" and "A"
            String guess = gameInput.nextLine().toLowerCase();

            // If the guess is a single letter, and it is a letter Player 2 already guessed
            // then re-iterate the loop and prompt Player 2 once again
            if (guess.length() == 1 && alreadyGuessed.contains(guess)) {
                System.out.println("You have already guessed this letter. Please try again.");
                continue; // Ignore rest of the loop and re-iterate
            }

            // If the guess's length is more than one, then it must be assumed that Player 2
            // is attempting to guess the word/phrase, in which case we check whether it is
            // the correct one or not
            if (guess.length() > 1) {
                // If the guess equals the word chosen, then Player 2 has won, and we can break out of the loop
                if (guess.equalsIgnoreCase(chosenWord)) {
                    p2Won = true; // Assign hasWon to true since Player 2 won
                    break;
                } else { // If they aren't the same, then inform Player 2 and subtract a life
                    System.out.println("Sorry, that is not the chosen word/phrase.");
                    lives--;
                }
            } else { // If the previous if-statement didn't run, then Player 2 must have guessed a letter
                // Create index variable that searches the first index of the letter inside the lowercased word
                int index = chosenLowerCase.indexOf(guess);
                alreadyGuessed.add(guess);

                // If the value is -1, then that means that the letter is not inside the word/phrase
                // In which case we inform Player 2 and subtract a life
                if (index == -1) {
                    System.out.println("Sorry, that letter is not in the word/phrase.");
                    lives--;
                } else {
                    // If the previous if-statement didn't run, then
                    // the letter guessed occurs at least once inside the word/phrase
                    System.out.println("The letter you guessed is inside the word!");
                    // We replace the first corresponding underscore to the letter that Player 2 guessed
                    replaceBuilder.replace(index, index + 1, String.valueOf(chosenWord.charAt(index)));

                    // This for loop will replace every corresponding underscore with the letter
                    // that Player 2 has guessed. This for loop will keep incrementing until index is -1
                    for (int i = 1; i < chosenWord.length(); i++) {
                        // Note: the second value in the indexOf() method
                        // corresponds to the first index to scan
                        // Consider the word "Comfort"
                        // If we do not tell the program which index to begin scanning,
                        // Then the program will only scan the first "o" and not the second
                        index = chosenLowerCase.indexOf(guess, i);
                        if (index == -1) { // If index is -1, break out of the loop
                            break;
                        } else {
                            // If the index is still not -1, then replace the corresponding underscore
                            // to the letter guessed, accordingly
                            replaceBuilder.replace(index, index + 1, String.valueOf(chosenWord.charAt(index)));
                        }
                    }
                }
            }
        }

        // If Player 2 won, congratulate them and inform them that they won
        // If player 2 lost, inform them that they ran out of lives and congratulate Player 1 for winning
        if (p2Won) {
            System.out.println("Congratulations, Player 2, you won the game!");
        } else {
            System.out.println("You ran out of lives. Player 1 wins!");
        }

        System.out.println("");

        // We do not close the gameInput scanner to avoid NoSuchElementException error when going back to the menu
        // Description of error: https://docs.oracle.com/javase/8/docs/api/java/util/NoSuchElementException.html
    }
}
