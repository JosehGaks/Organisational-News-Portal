package dao;

import models.Department;
import models.User;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oUserDao implements UserDao{
    private final Sql2o sql2o;
    public Sql2oUserDao(Sql2o sql2o){ this.sql2o = sql2o; }

    @Override
    public void add(User user) {
        String sql = "INSERT INTO users (name) VALUES (:name)";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql, true)
                    .bind(user)
                    .executeUpdate()
                    .getKey();
            user.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<User> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM users")
                    .executeAndFetch(User.class);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from users WHERE id=:id";
        String deleteJoin = "DELETE from departments_users WHERE userid = :userId";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();

            con.createQuery(deleteJoin)
                    .addParameter("userId", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAll() {
        String sql = "DELETE from users";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void addUserToDepartment(User user, Department restaurant){
        String sql = "INSERT INTO departments_users (restaurantid, userid) VALUES (:restaurantId, :userId)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("restaurantId", restaurant.getId())
                    .addParameter("userId", user.getId())
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public List<Department> getAllDepartmentsForAUser(int userId) {
        List<Department> departments = new ArrayList();
        String joinQuery = "SELECT restaurantid FROM departments_users WHERE userid = :userId";

        try (Connection con = sql2o.open()) {
            List<Integer> allDepartmentIds = con.createQuery(joinQuery)
                    .addParameter("userId", userId)
                    .executeAndFetch(Integer.class); //what is happening in the lines above?
            for (Integer restaurantId : allDepartmentIds){
                String restaurantQuery = "SELECT * FROM departments WHERE id = :restaurantId";
                departments.add(
                        con.createQuery(restaurantQuery)
                                .addParameter("restaurantId", restaurantId)
                                .executeAndFetchFirst(Department.class));
            } //why are we doing a second sql query - set?
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
        return departments;
    }

    @Override
    public User findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM users WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(User.class);
        }
    }

}
