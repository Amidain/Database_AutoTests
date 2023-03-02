package db_tests_v2;

import DAO.AuthorDAO;
import DAO.ProjectDAO;
import DAO.SessionDAO;
import DAO.TestDAO;
import DTO.Author;
import DTO.Project;
import DTO.Session;
import DTO.Test;
import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BaseTest {
    ISettingsFile configReader = new JsonSettingsFile("config.json");
    Author author = null;
    Session session = null;
    List<Test> tests = new ArrayList<>();
    Project project = null;
    AuthorDAO authorDAO = new AuthorDAO();
    SessionDAO sessionDAO = new SessionDAO();
    ProjectDAO projectDAO = new ProjectDAO();
    TestDAO testDAO= new TestDAO();
    LocalDateTime start_time = null;
    long status_id;

    @BeforeMethod
    protected void beforeMethod(){
        initiateProject();
    }

    @AfterMethod
    protected void afterMethod(ITestResult result) {

        for (Test test : tests
        ) {
            if (result.isSuccess()) {
                status_id = 1;
            } else if (result.wasRetried()) {
                status_id = 3;
            } else if (result.isNotRunning()) {
                status_id = 2;
            }

            test.setStatus_id(status_id);
            test.setStart_time(start_time);
            test.setEnd_time(LocalDateTime.now());
            test.setProject_id(project.getId());
            test.setAuthor_id(author.getId());

            testDAO.addTest(test);
        }
    }

    @AfterTest
    void afterTest(){
        for (Test test: tests
             ) {
            testDAO.deleteTest(test.getId());
        }
    }

    private void initiateProject() {
        start_time = LocalDateTime.now();
        tests = testDAO.getTestWithSameDigits();
        session = sessionDAO.getSessionById(3);

        author = new Author(
                configReader.getValue("/project_details/project_author/name").toString(),
                configReader.getValue("/project_details/project_author/login").toString(),
                configReader.getValue("/project_details/project_author/email").toString()
        );

        if (authorDAO.ifRecordExistsInDb(author.getName())) {
            author = authorDAO.getAuthorByName(author.getName());
        } else {
            authorDAO.addAuthor(author);
        }

        project = new Project(
                configReader.getValue("/project_details/project_name").toString()
        );

        if (projectDAO.ifRecordExistsInDb(project.getName())) {
            project = projectDAO.getProjectByName(project.getName());
        } else {
            projectDAO.addProject(project);
        }
    }
}
