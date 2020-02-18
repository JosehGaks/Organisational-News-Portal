package models;

public abstract class Department {
    private String departmentName;
    private int numberOfEmployees;
    private int id;

    public Department(String departmentName, int numberOfEmployees) {
        this.departmentName = departmentName;
        this.numberOfEmployees = numberOfEmployees;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
