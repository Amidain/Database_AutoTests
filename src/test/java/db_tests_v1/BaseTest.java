package db_tests_v1;

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
import org.testng.annotations.BeforeMethod;
import java.time.LocalDateTime;


public class BaseTest{
    ISettingsFile configReader = new JsonSettingsFile("config.json");
    Author author = null;
    Session session = null;
    Test test = null;
    Project project = null;
    TestDAO testDAO = new TestDAO();
    AuthorDAO authorDAO = new AuthorDAO();
    SessionDAO sessionDAO = new SessionDAO();
    ProjectDAO projectDAO = new ProjectDAO();
    LocalDateTime start_time = null;
    long status_id;

    @BeforeMethod
    protected void beforeMethod(){
        initiateProject();
    }

    @AfterMethod
    protected void afterMethod(ITestResult result){
        if (result.isSuccess()){
            status_id = 1;
        } else if (result.wasRetried()) {
            status_id = 3;
        } else if (result.isNotRunning()){
            status_id = 2;
        }

            testDAO.addTest(test = new Test(
                    result.getTestClass().getName(),
                    status_id,
                    result.getMethod().getMethodName(),
                    project.getId(),
                    session.getId(),
                    start_time,
                    LocalDateTime.now(),
                    configReader.getValue("/project_details/env").toString(),
                    configReader.getValue("/project_details/browser").toString(),
                    author.getId()
            ));
    }

    private void initiateProject() {
        start_time = LocalDateTime.now();

        author = new Author(
                configReader.getValue("/project_details/project_author/name").toString(),
                configReader.getValue("/project_details/project_author/login").toString(),
                configReader.getValue("/project_details/project_author/email").toString()
        );

        session = sessionDAO.getSessionById(3);

        project = new Project(
                configReader.getValue("/project_details/project_name").toString()
        );

        if (authorDAO.ifRecordExistsInDb(author.getName())) {
            author = authorDAO.getAuthorByName(author.getName());
        } else {
            authorDAO.addAuthor(author);
        }

        if (projectDAO.ifRecordExistsInDb(project.getName())) {
            project = projectDAO.getProjectByName(project.getName());
        } else {
            projectDAO.addProject(project);
        }
    }
}
