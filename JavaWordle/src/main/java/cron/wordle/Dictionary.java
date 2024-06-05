package cron.wordle;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Dictionary {

    private static String[] dictionary;

    public static String[] getDictionary() {
        if (dictionary == null) {
            dictionary = getDictionaryFromJson();
        }
        return dictionary;
    }

    /*
     * note: this method is slightly inefficient because you would ideally just filter each next guess.
     * However, since the sample size is fairly low this time loss is un-noticeable
     */

    public static String[] getFilteredDictionary(List<GuessResult> guessResults) {
        System.out.println(guessResults);
        return Arrays.stream(getDictionary())
                .filter(word -> guessResults.stream()
                        .allMatch(gr -> gr.validateWord(word)))
                .toArray(String[]::new);
    }

    private static String[] getDictionaryFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File("build/resources/main/dictionary.json");
            return objectMapper.readValue(jsonFile, String[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRandomString() {
        Random random = new Random();
        int randomIndex = random.nextInt(getDictionary().length);
        return getDictionary()[randomIndex];
    }

}
