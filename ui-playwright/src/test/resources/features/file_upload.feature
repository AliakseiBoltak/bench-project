Feature: File Upload

  Scenario Outline: User uploads a file successfully
    Given User is on the file upload page "https://the-internet.herokuapp.com/upload"
    When User uploads the file "src/test/resources/upload/<fileName>"
    And User submits the upload form
    Then Uploaded file name "<fileName>" should be displayed on the page
    Examples:
      | fileName              |
      | json-file-upload.json |
      | xml-file-upload.xml   |