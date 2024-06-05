import cron.wordle.Dictionary;
import cron.wordle.EntropyMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class EntropyMapTest {

    @Test
    public void testValuesAreInDescendingOrder() {
        EntropyMap entropyMap = new EntropyMap();

        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            double d = r.nextDouble();
            entropyMap.put(Dictionary.getRandomString(), d);
        }

        Iterator<Map.Entry<String, Double>> iterator = entropyMap.entrySet().iterator();
        assertTrue(iterator.hasNext(), "Map should not be empty");

        Map.Entry<String, Double> previousEntry = iterator.next();
        System.out.println("First entry: " + previousEntry.getKey() + " = " + previousEntry.getValue());

        while (iterator.hasNext()) {
            Map.Entry<String, Double> currentEntry = iterator.next();
            System.out.println("Current entry: " + currentEntry.getKey() + " = " + currentEntry.getValue());
            assertTrue(
                    currentEntry.getValue().compareTo(previousEntry.getValue()) <= 0,
                    "Each value should be greater than the last: " + currentEntry.getValue() + " <= " + previousEntry.getValue()
                        );
            previousEntry = currentEntry;
        }
    }

}
