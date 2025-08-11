package stepdefs;

import context.CucumberTestContext;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.FileUploadPage;
import com.google.inject.Inject;

@ScenarioScoped
public class FileUploadStepDefs {

    private final CucumberTestContext testContext;
    private FileUploadPage fileUploadPage;

    @Inject
    public FileUploadStepDefs(CucumberTestContext testContext) {
        this.testContext = testContext;
    }

    @Given("User is on the file upload page {string}")
    public void the_user_is_on_the_file_upload_page(String url) {
        fileUploadPage = new FileUploadPage(testContext.getPage());
        fileUploadPage.navigateToFileUploadPage(url);
    }

    @When("User uploads the file {string}")
    public void the_user_uploads_the_file(String filePath) {
        fileUploadPage.uploadFile(filePath);
    }

    @When("User submits the upload form")
    public void the_user_submits_the_upload_form() {
        fileUploadPage.submitUpload();
    }

    @Then("Uploaded file name {string} should be displayed on the page")
    public void the_uploaded_file_name_should_be_displayed(String expectedFileName) {
        String uploadedFileName = fileUploadPage.getUploadedFileName();
        Assert.assertTrue(uploadedFileName.contains(expectedFileName),
                "Uploaded file name '" + expectedFileName + "' should be displayed on the page.");
    }
}