package cron.wordle;

import cron.driver.WordleDriver;

import java.util.List;

public class Wordle {



    public static void main(String[] args) {
        WordleDriver wordleDriver = new WordleDriver();
        try {
            Thread.sleep(3000);
            wordleDriver.makeGuess("crane");
            Thread.sleep(100);
            for (int i = 0; i < 5; i++) {
                List<GuessResult> guessResults = wordleDriver.getGuessResults();
                if (guessResults == null) {
                    //happens on victory
                    break;
                }
                String[] possibilities = Dictionary.getFilteredDictionary(guessResults);
                EntropyMap entropyMap = EntropyUtils.getBestGuessList(possibilities, true);
                String bestGuess = entropyMap.getByIndex(0);
                wordleDriver.makeGuess(bestGuess);
                Thread.sleep(100);
            }

            Thread.sleep(20000);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            wordleDriver.quit();
        }
    }

}
