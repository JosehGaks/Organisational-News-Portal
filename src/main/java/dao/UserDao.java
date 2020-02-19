package dao;

import models.Department;
import models.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    void addUserToDepartment(User user, Department department);

    //read
    List<User> getAll();
    List<Department> getAllDepartmentsForAUser(int id);
    User findById(int id);

    //update
    //omit for now

    //delete
    void deleteById(int id);
    void clearAll();
}
