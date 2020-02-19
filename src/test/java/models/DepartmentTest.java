package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

class DepartmentTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getNameReturnsCorrectName() throws Exception {
        Department testDepartment = setupDepartment();
        assertEquals("students", testDepartment.getDepartmentName());
    }

    @Test
    public void getAddressReturnsCorrectAddress() throws Exception {
        Department testDepartment = setupDepartment();
        assertEquals(8, testDepartment.getNumberOfEmployees());
    }

    @Test
    public void setNameSetsCorrectName() throws Exception {
        Department testDepartment = setupDepartment();
        testDepartment.setDepartmentName("cook");
        assertNotEquals("students",testDepartment.getDepartmentName());
    }

    public Department setupDepartment (){
        return new Department("students", 8);
    }

    public Department setupAltDepartment (){
        return new Department("interns", 6);
    }
}