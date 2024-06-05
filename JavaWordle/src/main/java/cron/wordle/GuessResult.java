package cron.wordle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GuessResult {

    public final String guess;
    public final LetterResult[] score;

    public GuessResult(String guess, LetterResult[] score) {
        this.guess = guess;
        this.score = score;
    }

    public static GuessResult parseGuessResult(String result) {
        StringBuilder sb = new StringBuilder();
        LetterResult[] letterResults = new LetterResult[5];
        for (int i = 0; i < 5; i++) {
            char c = result.charAt(i*2);
            int num = Integer.parseInt(result.substring(i*2+1, i*2+2));
            letterResults[i] = num == 2 ? LetterResult.CORRECT : (num == 1 ? LetterResult.WRONG_SPOT : LetterResult.NOT_EXISTANT);
            sb.append(c);
        }
        return new GuessResult(sb.toString(), letterResults);
    }

    //mostly used for testing
    public static List<GuessResult> parseGuessResults(String ...guessResultStrings) {
        List<GuessResult> guessResults = new ArrayList<>();
        for (String grs : guessResultStrings) {
            guessResults.add(GuessResult.parseGuessResult(grs));
        }
        return guessResults;
    }

    public boolean isWinningResult() {
        for (LetterResult lr : score) {
            if (lr != LetterResult.CORRECT) {
                return false;
            }
        }
        return true;
    }

    public boolean validateWord(String word) {
        HashMap<Character, ArrayList<Tuple<Integer, LetterResult>>> guessData = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            char c = guess.charAt(i);
            LetterResult lr = score[i];
            if (!guessData.containsKey(c)) {
                guessData.put(c, new ArrayList<>());
            }
            guessData.get(c).add(new Tuple<>(i, lr));
        }

        for (char c : guessData.keySet()) {
            ArrayList<Tuple<Integer, LetterResult>> data = guessData.get(c);
            int numFound = 0;
            boolean zeroSeen = false;
            for (Tuple<Integer, LetterResult> dataItem : data) {
                int pos = dataItem.fst;
                LetterResult lr = dataItem.snd;
                switch (lr) {
                    case CORRECT:
                        numFound++;
                        if (word.charAt(pos) != c) {
                            return false;
                        }
                        break;
                    case WRONG_SPOT:
                        if (zeroSeen) {
                            return false;
                        }
                        numFound++;
                        if (word.charAt(pos) == c) {
                            return false;
                        }
                        break;
                    case NOT_EXISTANT:
                        zeroSeen = true;
                        if (word.charAt(pos) == c) {
                            return false;
                        }
                        break;
                }
            }
            final int finalNumFound = numFound;
            if (zeroSeen) {
                if (count(word, c) != finalNumFound) {
                    return false;
                }
            } else {
                if (count(word, c) < finalNumFound) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int count(String word, char c) {
        int count = 0;
        for (char wc : word.toCharArray()) {
            if (wc == c) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            char c = guess.charAt(i);
            int num = score[i] == LetterResult.CORRECT ? 2 : (score[i] == LetterResult.WRONG_SPOT ? 1 : 0);
            sb.append(c).append(num);
        }

        return sb.toString();
    }
}