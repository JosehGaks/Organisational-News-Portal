package dao;


import models.Department;
import models.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Sql2oUserDaoTest {
    private static Connection conn;
    private static Sql2oDepartmentDao departmentDao;
    private static Sql2oUserDao userDao;
    private static Sql2oNewsDao newsDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:postgresql://localhost:5432/news_test";
        Sql2o sql2o = new Sql2o(connectionString, "josephgakunga", "123");
        departmentDao = new Sql2oDepartmentDao(sql2o);
        userDao = new Sql2oUserDao(sql2o);
        newsDao = new Sql2oNewsDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        departmentDao.clearAll();
        newsDao.clearAll();
        userDao.clearAll();
        System.out.println("clearing database");
    }

    @AfterClass
    public static void shutDown() throws Exception{ //changed to static
        conn.close();
        System.out.println("connection closed");
    }

    @Test
    public void addingUserSetsId() throws Exception {
        User testUser = setupNewUser();
        int originalUserId = testUser.getId();
        userDao.add(testUser);
        assertNotEquals(originalUserId,testUser.getId());
    }

    @Test
    public void addedUsersAreReturnedFromGetAll() throws Exception {
        User testuser = setupNewUser();
        userDao.add(testuser);
        assertEquals(1, userDao.getAll().size());
    }

    @Test
    public void noUsersReturnsEmptyList() throws Exception {
        assertEquals(0, userDao.getAll().size());
    }

    @Test
    public void deleteByIdDeletesCorrectUser() throws Exception {
        User user = setupNewUser();
        userDao.add(user);
        userDao.deleteById(user.getId());
        assertEquals(0, userDao.getAll().size());
    }

    @Test
    public void clearAll() throws Exception {
        User testUser = setupNewUser();
        User otherUser = setupNewUser();
        userDao.clearAll();
        assertEquals(0, userDao.getAll().size());
    }

//    @Test
//    public void addUserTypeToDepartment_AddsTypeCorrectly() throws Exception {
//
//        Department testDepartment = setupDepartment();
//        Department altDepartment = setupAltDepartment();
//
//        departmentDao.add(testDepartment);
//        departmentDao.add(altDepartment);
//
//        User testUser = setupNewUser();
//
//        userDao.add(testUser);
//
//        userDao.addUserToDepartment(testUser, testDepartment);
//        userDao.addUserToDepartment(testUser, altDepartment);
//
//        assertEquals(2, userDao.getAllDepartmentsForAUser(testUser.getId()).size());
//    }

    @Test
    public void deletingDepartmentAlsoUpdatesJoinTable() throws Exception {
        User testUser  = new User("joe","cto");
        userDao.add(testUser);

        Department testDepartment = setupDepartment();
        departmentDao.add(testDepartment);

        Department altDepartment = setupAltDepartment();
        departmentDao.add(altDepartment);

        departmentDao.addDepartmentToUser(testDepartment,testUser);
        departmentDao.addDepartmentToUser(altDepartment, testUser);

        departmentDao.deleteById(testDepartment.getId());
        assertEquals(0, departmentDao.getAllUsersByDepartment(testDepartment.getId()).size());
    }

//    @Test
//    public void deletingUserAlsoUpdatesJoinTable() throws Exception {
//
//        Department testDepartment = setupDepartment();
//
//        departmentDao.add(testDepartment);
//
//        User testUser = setupNewUser();
//        User otherUser = new User("jack","manager");
//
//        userDao.add(testUser);
//        userDao.add(otherUser);
//
//        userDao.addUserToDepartment(testUser, testDepartment);
//        userDao.addUserToDepartment(otherUser,testDepartment);
//
//        userDao.deleteById(testDepartment.getId());
//        assertEquals(1, userDao.getAllDepartmentsForAUser(testUser.getId()).size());
//    }

    // helpers

    public User setupNewUser(){
        return new User("George","ceo");
    }

    public Department setupDepartment (){
        return new Department("marketing", 10);
    }

    public Department setupAltDepartment (){
        return new Department("distribution", 5);
    }
}