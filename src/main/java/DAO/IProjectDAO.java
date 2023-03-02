package DAO;

import DTO.Project;

import java.util.List;

public interface IProjectDAO {
    Project getProjectById(int id);
    Project getProjectByName(String name);
    List<Project> getAllProjects();
    boolean addProject(Project project);
    boolean updateProject(Project project);
    boolean deleteProject(int id);
}
