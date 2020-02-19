package dao;

import models.Department;
import models.User;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oDepartmentDao implements DepartmentDao{
    private final Sql2o sql2o;
    public Sql2oDepartmentDao(Sql2o sql2o){ this.sql2o = sql2o; }

    @Override
    public void add(Department department) {
        String sql = "INSERT INTO departments (departmentName,numberOfEmployees) VALUES (:departmentName,:numberOfEmployees)";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql, true)
                    .bind(department)
                    .executeUpdate()
                    .getKey();
            department.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void addDepartmentToUser(Department department, User user) {

    }

    @Override
    public List<Department> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM departments")
                    .executeAndFetch(Department.class);
        }
    }
    
    @Override
    public void deleteById(int id) {
        String sql = "DELETE from departments WHERE id=:id";
        String deleteJoin = "DELETE from departments_users WHERE departmentid = :departmentId";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();

            con.createQuery(deleteJoin)
                    .addParameter("departmentId", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAll() {
        String sql = "DELETE from departments";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }


    @Override
    public Department findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM departments WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Department.class);
        }
    }

    @Override
    public List<User> getAllUsersByDepartment(int departmentId) {

        List<User> users = new ArrayList<User>();
        String joinQuery = "SELECT userid FROM departments_users WHERE departmentid = :departmentId";

        try (Connection con = sql2o.open()) {
            List<Integer> allUsersIds = con.createQuery(joinQuery)
                    .addParameter("departmentId", departmentId)
                    .executeAndFetch(Integer.class);
            for (Integer foodId : allUsersIds){
                String userQuery = "SELECT * FROM users WHERE id = :userId";
                users.add(
                        con.createQuery(userQuery)
                                .addParameter("userId", foodId)
                                .executeAndFetchFirst(User.class));
            }
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
        return users;
    }

    @Override
    public void update(int id, String newDepartmentName, int newNumberOfEmployees) {
        String sql = "UPDATE departments SET (departmentName, numberOfEmployees) = (:departmentName, :numberOfEmployees) WHERE id=:id"; //CHECK!!!
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("departmentName", newDepartmentName)
                    .addParameter("numberOfEmployees", newNumberOfEmployees)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

}
