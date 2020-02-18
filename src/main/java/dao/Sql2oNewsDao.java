package dao;
import java.util.List;

import models.News;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;

public class Sql2oNewsDao implements NewsDao{
    private final Sql2o sql2o;
    public Sql2oFoodtypeDao(Sql2o sql2o){ this.sql2o = sql2o; }

    @Override
    public void add(News news) {
        String sql = "INSERT INTO foodtypes (name) VALUES (:name)";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql, true)
                    .bind(foodtype)
                    .executeUpdate()
                    .getKey();
            foodtype.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<News> getAll() {
        return null;
    }

    @Override
    public List<News> getAllNewsByDepartment(int departmentId) {
        return null;
    }

    @Override
    public List<News> getAllNewsByDepartmentSortedNewestToOldest(int departmentId) {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void clearAll() {

    }
}
