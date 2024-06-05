package cron.driver;

import cron.wordle.GuessResult;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;

public class WordleDriver {

    private static final String WORDLE_URL = "https://hellowordl.net/";

    private WebDriver driver;

    public WordleDriver() {

        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-web-security");
            options.addArguments("--allow-running-insecure-content");
            driver = new ChromeDriver(options);
            driver.get(WORDLE_URL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void makeGuess(String guess) {
        System.out.printf("Making guess %s%n", guess);

        Actions guessAction = new Actions(driver);
        guessAction.sendKeys(guess);
        guessAction.perform();

        //todo some update here?

        Actions enterAction = new Actions(driver);
        enterAction.sendKeys(Keys.ENTER);
        enterAction.perform();
    }

    // returns null on victory
    public List<GuessResult> getGuessResults() {
        List<GuessResult> guessResults = new ArrayList<>();
        List<WebElement> rows = driver.findElements(By.cssSelector("tr.Row-locked-in"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.cssSelector("td.Row-letter"));

            StringBuilder guessResultString = new StringBuilder();

            for (WebElement cell : cells) {
                String cellClass = cell.getAttribute("class");
                String letter = cell.getText().toLowerCase();
                guessResultString.append(letter);

                if (cellClass.contains("letter-absent")) {
                    guessResultString.append("0");
                } else if (cellClass.contains("letter-elsewhere")) {
                    guessResultString.append("1");
                } else if (cellClass.contains("letter-correct")) {
                    guessResultString.append("2");
                } else {
                    //default to 0
                    guessResultString.append("0");
                }
            }
            GuessResult guessResult = GuessResult.parseGuessResult(guessResultString.toString());
            if (guessResult.isWinningResult()) {
                return null;
            }
            guessResults.add(guessResult);

        }

        return guessResults;
    }

}
