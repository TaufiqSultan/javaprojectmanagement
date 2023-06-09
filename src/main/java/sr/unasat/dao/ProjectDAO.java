package sr.unasat.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import sr.unasat.configuration.JPAConfig;
import sr.unasat.entities.Project;

import java.util.List;

public class ProjectDAO {
    private EntityManager entityManager = JPAConfig.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    public List<Project> findAllProjects() {
        if (!transaction.isActive()){
            transaction.begin();
        }
        String jpql = "select p from Project p";
        TypedQuery<Project> query = entityManager.createQuery(jpql, Project.class);
        List<Project> projectList = query.getResultList();
        transaction.commit();

        return projectList;
    }

    public Project findProjectById(int id) {
        transaction.begin();
        String jpql = "select p from Project p where p.id = :id";
        TypedQuery<Project> query = entityManager.createQuery(jpql, Project.class);
        Project project = query.setParameter("id", id).getSingleResult();
        transaction.commit();
        return project;
    }

    public Project insertProject(Project project) {
        transaction.begin();
        entityManager.persist(project);
        transaction.commit();
        return project;
    }

    public int updateProject(int id, String name, String desc) {
        transaction.begin();
        String jpql = "update Project p set p.projectName = :name, p.projectDescription = :description where p.projectID = :id";
       Query query = entityManager.createQuery(jpql);
        query.setParameter("name",name);
        query.setParameter("description", desc);
        query.setParameter("id",id);
        int rowsUpdated = query.executeUpdate();
        System.out.println("updared"+ rowsUpdated);
        transaction.commit();
        return rowsUpdated;
    }

    public int deleteProject(int id) {
        if (!transaction.isActive()){
            transaction.begin();
        }
        String taksjpql = "delete from Task t where t.project = :id";
        Query TaskQuery = entityManager.createQuery(taksjpql);
        String jpql = "delete from Project p where p.id = :id";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("id", id);
        int rowsDeleted = query.executeUpdate();

        System.out.println("row deleted"+rowsDeleted);
        transaction.commit();
        return rowsDeleted;

    }
}
