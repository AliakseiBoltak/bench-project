import com.google.inject.Inject;
import github.GitHubBaseTest;
import org.example.config.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.FileUploadPage;

public class FileUploadTest extends GitHubBaseTest {

    private static final String UPLOAD_FILE_URL = "https://the-internet.herokuapp.com/upload";
    private static final String UPLOAD_FILE_FOLDER = "src/test/resources/users/";
    private static final String UPLOAD_FILE_NAME = "users.json";

    @Inject
    public FileUploadTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @Override
    protected boolean useStoredGitHubSession() {
        return false;
    }

    @Test
    public void testFileUpload() {
        FileUploadPage fileUploadPage = new FileUploadPage(page)
                .navigateToFileUploadPage(UPLOAD_FILE_URL)
                .uploadFile(UPLOAD_FILE_FOLDER + UPLOAD_FILE_NAME)
                .submitUpload();

        Assert.assertTrue(fileUploadPage.getUploadedFileName().contains(UPLOAD_FILE_NAME),
                "Uploaded file name should be displayed on the page.");
    }
}