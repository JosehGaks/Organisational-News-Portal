package models;

public class User extends Department{
    private int id;
    private String name;
    private String positionInCompany;
    private int departmentId;

    public User(String name, String positionInCompany, String departmentName, int numberOfEmployees) {
        super(departmentName,numberOfEmployees);
        this.name = name;
        this.positionInCompany = positionInCompany;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPositionInCompany() {
        return positionInCompany;
    }

    public void setPositionInCompany(String positionInCompany) {
        this.positionInCompany = positionInCompany;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

}
