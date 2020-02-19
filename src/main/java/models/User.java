package models;

import java.util.Objects;

public class User {
    private int id;
    private String name;
    private String positionInCompany;

    public User(String name, String positionInCompany) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(positionInCompany, user.positionInCompany);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, positionInCompany);
    }
}
