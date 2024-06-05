package cron.wordle;

public class EntropyMap extends ValueSortedMap<String, Double> {

    public Double addBasedOnEntropy(String word, String[] possibilities) {
        double entropy = EntropyUtils.calculateEntropy(word, possibilities);
        put(word, entropy);
        return entropy;
    }

}
