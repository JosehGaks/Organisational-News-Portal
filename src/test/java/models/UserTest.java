package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

class UserTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getName() {
        User testUser = setupUser();
        assertEquals("dessert", testUser.getName());
    }

    @Test
    public void setName() {
        User testUser = setupUser();
        testUser.setName("breakfast");
        assertNotEquals("dessert", testUser.getName());
    }

    @Test
    public void setId() {
        User testUser = setupUser();
        testUser.setId(5);
        assertEquals(5, testUser.getId());
    }

    // helper
    public User setupUser(){
        return new User("jose","managing director");
    }
}