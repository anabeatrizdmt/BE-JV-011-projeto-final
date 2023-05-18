import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class SeleniumTest {

    @Test
    void adicionarLivro() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
        var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        WebDriver webDriver = new ChromeDriver(chromeOptions);

        webDriver.get("http://localhost:8080/books");
        assumeTrue(webDriver.getTitle().equals("Book Store"));
        Thread.sleep(2000);

        var buttonFound = webDriver.findElement(By.name("add-book"));
        buttonFound.click();
        Thread.sleep(2000);

        var title = webDriver.findElement(By.name("title"));
        title.sendKeys("Book 1");

        var summary = webDriver.findElement(By.name("summary"));
        summary.sendKeys("Summary 1");

        var tableOfContents = webDriver.findElement(By.name("tableOfContents"));
        tableOfContents.sendKeys("Table of contents 1");

        var price = webDriver.findElement(By.name("price"));
        price.sendKeys("30");

        var pages = webDriver.findElement(By.name("pages"));
        pages.sendKeys("150");

        var isbn = webDriver.findElement(By.name("isbn"));
        isbn.sendKeys("123-4567890");

        var publicationDate = webDriver.findElement(By.name("publicationDate"));
        publicationDate.sendKeys("18072023");
        Thread.sleep(2000);

        publicationDate.submit();

        Thread.sleep(4000);

        webDriver.close();
        webDriver.quit();
    }

    @Test
    void excluirLivro() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
        var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        WebDriver webDriver = new ChromeDriver(chromeOptions);

        webDriver.get("http://localhost:8080/books");
        assumeTrue(webDriver.getTitle().equals("Book Store"));
        Thread.sleep(2000);

        var buttonFound = webDriver.findElement(By.name("add-book"));
        buttonFound.click();
        Thread.sleep(2000);

        var title = webDriver.findElement(By.name("title"));
        title.sendKeys("Book 1");

        var summary = webDriver.findElement(By.name("summary"));
        summary.sendKeys("Summary 1");

        var tableOfContents = webDriver.findElement(By.name("tableOfContents"));
        tableOfContents.sendKeys("Table of contents 1");

        var price = webDriver.findElement(By.name("price"));
        price.sendKeys("30");

        var pages = webDriver.findElement(By.name("pages"));
        pages.sendKeys("150");

        var isbn = webDriver.findElement(By.name("isbn"));
        isbn.sendKeys("123-4567890");

        var publicationDate = webDriver.findElement(By.name("publicationDate"));
        publicationDate.sendKeys("18072023");
        Thread.sleep(2000);

        publicationDate.submit();

        Thread.sleep(4000);

        webDriver.get("http://localhost:8080/books");
        assumeTrue(webDriver.getTitle().equals("Book Store"));
        Thread.sleep(2000);

        var deleteButtonFound = webDriver.findElement(By.name("delete-book"));
        deleteButtonFound.click();
        Thread.sleep(2000);

        webDriver.close();
        webDriver.quit();
    }
}
