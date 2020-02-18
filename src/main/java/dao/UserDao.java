package dao;

import models.User;

import java.util.List;

public interface UserDao {
    //create
    void add(User user);

    //read
    List<User> getAll();
    List<User> getAllUsersByDepartment(int departmentId);
    List<User> getAllUsersByDepartmentSortedNewestToOldest(int departmentId);

    //update
    //omit for now

    //delete
    void deleteById(int id);
    void clearAll();
}
