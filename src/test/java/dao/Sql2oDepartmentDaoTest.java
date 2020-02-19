package dao;

import models.Department;
import models.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Sql2oDepartmentDaoTest {
    private static Connection conn; //these variables are now static.
    private static Sql2oDepartmentDao departmentDao; //these variables are now static.
    private static Sql2oUserDao userDao; //these variables are now static.
    private static Sql2oNewsDao newsDao; //these variables are now static.

    @BeforeClass                //
    public static void setUp() throws Exception {
        String connectionString = "jdbc:postgresql://localhost:5432/news_test";  //connect to postgres test database
        Sql2o sql2o = new Sql2o(connectionString, "", "123"); //changed user and pass to null
        departmentDao = new Sql2oDepartmentDao(sql2o);
        userDao = new Sql2oUserDao(sql2o);
        newsDao = new Sql2oNewsDao(sql2o);
        conn = sql2o.open();        //open connection once before this test file is run
    }

    @After              //run after every test
    public void tearDown() throws Exception {
        System.out.println("clearing database");
        departmentDao.clearAll(); //clear all departments after every test
        userDao.clearAll(); //clear all departments after every test
        newsDao.clearAll();
    }

    @AfterClass     //changed to @AfterClass (run once after all tests in this file completed)
    public static void shutDown() throws Exception{ //changed to static
        conn.close(); // close connection once after this entire test file is finished
        System.out.println("connection closed");
    }


    @Test
    public void addingFoodSetsId() throws Exception {
        Department testDepartment = setupDepartment();
        int originalDepartmentId = testDepartment.getId();
        departmentDao.add(testDepartment);
        assertNotEquals(originalDepartmentId, testDepartment.getId());
    }

    @Test
    public void addedDepartmentsAreReturnedFromGetAll() throws Exception {
        Department testDepartment = setupDepartment();
        assertEquals(1, departmentDao.getAll().size());
    }

    @Test
    public void noDepartmentsReturnsEmptyList() throws Exception {
        assertEquals(0, departmentDao.getAll().size());
    }

    @Test
    public void findByIdReturnsCorrectDepartment() throws Exception {
        Department testDepartment = setupDepartment();
        Department otherDepartment = setupDepartment();
        assertEquals(testDepartment, departmentDao.findById(testDepartment.getId()));
    }

    @Test
    public void updateCorrectlyUpdatesAllFields() throws Exception {
        Department testDepartment = setupDepartment();
        departmentDao.update(testDepartment.getId(), "a", 10);
        Department foundDepartment = departmentDao.findById(testDepartment.getId());
        assertEquals("a", foundDepartment.getDepartmentName());
        assertEquals("b", foundDepartment.getNumberOfEmployees());

    }

    @Test
    public void deleteByIdDeletesCorrectDepartment() throws Exception {
        Department testDepartment = setupDepartment();
        departmentDao.deleteById(testDepartment.getId());
        assertEquals(0, departmentDao.getAll().size());
    }

    @Test
    public void clearAll() throws Exception {
        Department testDepartment = setupDepartment();
        Department otherDepartment = setupDepartment();
        departmentDao.clearAll();
        assertEquals(0, departmentDao.getAll().size());
    }

    @Test
    public void getAllUsersForADepartmentReturnsUsersCorrectly() throws Exception {
        User testUser  = new User("mark","director");
        userDao.add(testUser);

        User otherUser  = new User("harry","engineer");
        userDao.add(otherUser);

        Department testDepartment = setupDepartment();
        departmentDao.add(testDepartment);
        departmentDao.addDepartmentToUser(testDepartment,testUser);
        departmentDao.addDepartmentToUser(testDepartment,otherUser);

        User[] users = {testUser, otherUser}; //oh hi what is this?

        assertEquals(Arrays.asList(users), departmentDao.getAllUsersByDepartment(testDepartment.getId()));
    }

    @Test
    public void deleteingDepartmentAlsoUpdatesJoinTable() throws Exception {
        User testUser  = new User("lucy","chairlady");
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


    //helpers

    public Department setupDepartment (){
        Department department = new Department("tech", 2);
        departmentDao.add(department);
        return department;
    }

    public Department setupAltDepartment (){
        Department department = new Department("innovation", 12);
        departmentDao.add(department);
        return department;
    }

}