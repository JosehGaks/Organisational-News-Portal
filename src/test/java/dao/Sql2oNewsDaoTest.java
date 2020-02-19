package dao;

import models.Department;
import models.News;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Sql2oNewsDaoTest {
    private static Connection conn; //these variables are now static.
    private static Sql2oDepartmentDao departmentDao; //these variables are now static.
    private static Sql2oUserDao userDao; //these variables are now static.
    private static Sql2oNewsDao newsDao; //these variables are now static.

    @BeforeClass
    public static void setUp() throws Exception {
        String connectionString = "jdbc:postgresql://localhost:5432/jadle_test"; //
        Sql2o sql2o = new Sql2o(connectionString, "v", "1234"); //changed user and pass to null for mac users...Linux & windows need strings
        departmentDao = new Sql2oDepartmentDao(sql2o);
        userDao = new Sql2oUserDao(sql2o);
        newsDao = new Sql2oNewsDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("clearing database");
        departmentDao.clearAll(); //clear all departments after every test
        userDao.clearAll(); //clear all departments after every test
        newsDao.clearAll();
    }

    @AfterClass
    public static void shutDown() throws Exception{ //changed to static
        conn.close();
        System.out.println("connection closed");
    }

    @Test
    public void addingNewsSetsId() throws Exception {
        Department testDepartment = setupDepartment();
        departmentDao.add(testDepartment);
        News testNews = new News("new shifts to be done",testDepartment.getId());
        int originalNewsId = testNews.getId();
        newsDao.add(testNews);
        assertNotEquals(originalNewsId,testNews.getId());
    }

    @Test
    public void getAll() throws Exception {
        News news1 = setupNews();
        News news2 = setupNews();
        assertEquals(2, newsDao.getAll().size());
    }

    @Test
    public void getAllNewssByDepartment() throws Exception {
        Department testDepartment = setupDepartment();
        Department otherDepartment = setupDepartment(); //add in some extra data to see if it interferes
        News news1 = setupNewsForDepartment(testDepartment);
        News news2 = setupNewsForDepartment(testDepartment);
        News newsForOtherDepartment = setupNewsForDepartment(otherDepartment);
        assertEquals(2, newsDao.getAllNewsByDepartment(testDepartment.getId()).size());
    }

    @Test
    public void deleteById() throws Exception {
        News testNews = setupNews();
        News otherNews = setupNews();
        assertEquals(2, newsDao.getAll().size());
        newsDao.deleteById(testNews.getId());
        assertEquals(1, newsDao.getAll().size());
    }

    @Test
    public void clearAll() throws Exception {
        News testNews = setupNews();
        News otherNews = setupNews();
        newsDao.clearAll();
        assertEquals(0, newsDao.getAll().size());
    }



    @Test
    public void newssAreReturnedInCorrectOrder() throws Exception {
        Department testDepartment = setupDepartment();
        departmentDao.add(testDepartment);
        News testNews = new News("new ceo to come in", testDepartment.getId());
        newsDao.add(testNews);
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }

        News testSecondNews = new News("Mr. Spock", testDepartment.getId());
        newsDao.add(testSecondNews);

        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }

        News testThirdNews = new News("Today was cool,",testDepartment.getId());
        newsDao.add(testThirdNews);

        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }

        News testFourthNews = new News("welcome everyone", testDepartment.getId());
        newsDao.add(testFourthNews);

        assertEquals(4, newsDao.getAllNewsByDepartment(testDepartment.getId()).size()); //it is important we verify that the list is the same size.
        assertEquals("I prefer home cooking", newsDao.getAllNewsByDepartmentSortedNewestToOldest(testDepartment.getId()).get(0).getContent());
    }

    //helpers

    public News setupNews() {
        News news = new News("great job today", 4);
        newsDao.add(news);
        return news;
    }

    public News setupNewsForDepartment(Department department) {
        News news = new News("great!!!",department.getId());
        newsDao.add(news);
        return news;
    }

    public Department setupDepartment() {
        Department department = new Department("finance", 6);
        departmentDao.add(department);
        return department;
    }

}