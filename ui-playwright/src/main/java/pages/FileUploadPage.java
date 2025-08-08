package pages;

import com.microsoft.playwright.Page;
import org.example.exception.DataException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUploadPage {

    private final Page page;
    private static final String FILE_INPUT = "input[type='file']";
    private static final String SUBMIT_BUTTON = "#file-submit";
    private static final String UPLOADED_FILES_TEXT = "#uploaded-files";

    public FileUploadPage(Page page) {
        this.page = page;
    }

    public FileUploadPage navigateToFileUploadPage(String url) {
        page.navigate(url);
        return this;
    }

    public FileUploadPage uploadFile(String filePath) {
        if (!Files.exists(Path.of(filePath))) {
            throw new DataException("File does not exist: " + filePath);
        }
        page.setInputFiles(FILE_INPUT, Paths.get(filePath));
        return this;
    }

    public FileUploadPage submitUpload() {
        page.click(SUBMIT_BUTTON);
        return this;
    }

    public String getUploadedFileName() {
        page.waitForSelector(UPLOADED_FILES_TEXT);
        return page.textContent(UPLOADED_FILES_TEXT);
    }
}
