package pages;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class HomePage {

    public boolean checkIfHomePageLoaded() {
        $(By.xpath("//h2[text()='Home']")).shouldBe(Condition.visible);
        return $(By.xpath("./descendant::span[@class='Button-label' and text()='New'][1]")).isDisplayed();
    }

}