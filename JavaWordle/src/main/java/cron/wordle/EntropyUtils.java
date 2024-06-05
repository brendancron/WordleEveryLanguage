package cron.wordle;

import java.util.Arrays;

public class EntropyUtils {

    public static double calculateEntropy(String guess, String[] possibilities) {
        double total = possibilities.length;
        double entropy = 0.0;
        for(LetterResult a : LetterResult.values()) {
            for(LetterResult b : LetterResult.values()) {
                for(LetterResult c : LetterResult.values()) {
                    for(LetterResult d : LetterResult.values()) {
                        for(LetterResult e : LetterResult.values()) {
                            LetterResult[] score = {a,b,c,d,e};
                            GuessResult guessResult = new GuessResult(guess, score);
                            double num = Arrays.stream(possibilities).filter(guessResult::validateWord).count();
                            if(num > 0) {
                                double probability = num/total;
                                double information = Math.log(1/probability) / Math.log(2); // Log base 2 (1/prob)
                                entropy += probability * information;
                            }
                        }
                    }
                }
            }
        }
        return entropy;
    }

    public static EntropyMap getBestGuessList(String[] possibilities, boolean doPrint) {
        return getBestGuessList(possibilities, possibilities, doPrint);
    }

    public static EntropyMap getBestGuessList(String[] possibilities, String[] wordList, boolean doPrint) {
        String[] choices = possibilities.clone();
        int total = choices.length;
        EntropyMap entropyMap = new EntropyMap();
        for (int i = 0; i < total; i++) {
            String choice = choices[i];
            double entropy = entropyMap.addBasedOnEntropy(choices[i], wordList);
            if(doPrint) {
                System.out.println("(" + i + "/" + total + ") " + choice + " total entropy: " + entropy);
            }
        }
        return entropyMap;
    }

}
