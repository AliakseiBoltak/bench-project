package pages;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class HomePage {

    public boolean checkIfHomePageLoaded() {
        return $(By.xpath("./descendant::span[@class='Button-label' and text()='New'][1]")).isDisplayed()
                && $(By.xpath("//h2[text()='Home']")).isDisplayed();
    }

}