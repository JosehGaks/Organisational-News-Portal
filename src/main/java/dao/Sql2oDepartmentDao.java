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
        String sql = "INSERT INTO departments (departmentName) VALUES (:departmentName)";
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

//    @Override
//    public List<Department> getAllRestaurantsForAFoodtype(int id) {
//        return null;
//    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from departments WHERE id=:id";
        String deleteJoin = "DELETE from restaurants_departments WHERE departmentid = :departmentId";
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

//    @Override
//    public void addDepartmentToRestaurant(Department department, Restaurant restaurant){
//        String sql = "INSERT INTO restaurants_departments (restaurantid, departmentid) VALUES (:restaurantId, :departmentId)";
//        try (Connection con = sql2o.open()) {
//            con.createQuery(sql)
//                    .addParameter("restaurantId", restaurant.getId())
//                    .addParameter("departmentId", department.getId())
//                    .executeUpdate();
//        } catch (Sql2oException ex){
//            System.out.println(ex);
//        }
//    }

//    @Override
//    public List<Restaurant> getAllRestaurantsForADepartment(int departmentId) {
//        List<Restaurant> restaurants = new ArrayList();
//        String joinQuery = "SELECT restaurantid FROM restaurants_departments WHERE departmentid = :departmentId";
//
//        try (Connection con = sql2o.open()) {
//            List<Integer> allRestaurantIds = con.createQuery(joinQuery)
//                    .addParameter("departmentId", departmentId)
//                    .executeAndFetch(Integer.class); //what is happening in the lines above?
//            for (Integer restaurantId : allRestaurantIds){
//                String restaurantQuery = "SELECT * FROM restaurants WHERE id = :restaurantId";
//                restaurants.add(
//                        con.createQuery(restaurantQuery)
//                                .addParameter("restaurantId", restaurantId)
//                                .executeAndFetchFirst(Restaurant.class));
//            } //why are we doing a second sql query - set?
//        } catch (Sql2oException ex){
//            System.out.println(ex);
//        }
//        return restaurants;
//    }

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
        return null;
    }

    @Override
    public void update(int id, String departmentName, int numberOfEmployees) {

    }

}
