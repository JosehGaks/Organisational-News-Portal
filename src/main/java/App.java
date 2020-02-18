import dao.Sql2oDepartmentDao;
import dao.Sql2oUserDao;
import models.Department;
import models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        Sql2oUserDao userDao;
        Sql2oDepartmentDao departmentDao;
        Sql2oReviewDao newsDao;
        Connection conn;
        Gson gson = new Gson();

        staticFileLocation("/public");
        String connectionString = "jdbc:postgresql://localhost:5432/jadle";   //connect to jadle, not jadle_test! try not to copy paste
        Sql2o sql2o = new Sql2o(connectionString, "v", "1234");

        departmentDao = new Sql2oDepartmentDao(sql2o);
        userDao = new Sql2oUserDao(sql2o);
        newsDao = new Sql2oReviewDao(sql2o);
        conn = sql2o.open();

        //CREATE
        post("/departments/:departmentId/user/:userId", "application/json", (req, res) -> {

            int departmentId = Integer.parseInt(req.params("departmentId"));
            int userId = Integer.parseInt(req.params("userId"));
            Department department = departmentDao.findById(departmentId);
            User user = userDao.findById(userId);


            if (department != null && user != null){
                //both exist and can be associated
                userDao.addUserToDepartment(user, department);
                res.status(201);
                return gson.toJson(String.format("Department '%s' and User '%s' have been associated",user.getName(), department.getName()));
            }
            else {
                throw new ApiException(404, String.format("Department or User does not exist"));
            }
        });

        get("/departments/:id/users", "application/json", (req, res) -> {
            int departmentId = Integer.parseInt(req.params("id"));
            Department departmentToFind = departmentDao.findById(departmentId);
            if (departmentToFind == null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists", req.params("id")));
            }
            else if (departmentDao.getAllUsersByDepartment(departmentId).size()==0){
                return "{\"message\":\"I'm sorry, but no users are listed for this department.\"}";
            }
            else {
                return gson.toJson(departmentDao.getAllUsersByDepartment(departmentId));
            }
        });

        get("/users/:id/departments", "application/json", (req, res) -> {
            int userId = Integer.parseInt(req.params("id"));
            User userToFind = userDao.findById(userId);
            if (userToFind == null){
                throw new ApiException(404, String.format("No user with the id: \"%s\" exists", req.params("id")));
            }
            else if (userDao.getAllDepartmentsForAUser(userId).size()==0){
                return "{\"message\":\"I'm sorry, but no departments are listed for this user.\"}";
            }
            else {
                return gson.toJson(userDao.getAllDepartmentsForAUser(userId));
            }
        });


        post("/departments/:departmentId/news/new", "application/json", (req, res) -> {
            int departmentId = Integer.parseInt(req.params("departmentId"));
            Review news = gson.fromJson(req.body(), Review.class);
            news.setCreatedat(); //I am new!
            news.setFormattedCreatedAt();
            news.setDepartmentId(departmentId); //we need to set this separately because it comes from our route, not our JSON input.
            newsDao.add(news);
            res.status(201);
            return gson.toJson(news);
        });

        post("/users/new", "application/json", (req, res) -> {
            User user = gson.fromJson(req.body(), User.class);
            userDao.add(user);
            res.status(201);
            return gson.toJson(user);
        });

        //READ
        get("/departments", "application/json", (req, res) -> {
            System.out.println(departmentDao.getAll());

            if(departmentDao.getAll().size() > 0){
                return gson.toJson(departmentDao.getAll());
            }

            else {
                return "{\"message\":\"I'm sorry, but no departments are currently listed in the database.\"}";
            }

        });

        get("/departments/:id", "application/json", (req, res) -> { //accept a request in format JSON from an app
            int departmentId = Integer.parseInt(req.params("id"));
            Department departmentToFind = departmentDao.findById(departmentId);
            if (departmentToFind == null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists", req.params("id")));
            }
            return gson.toJson(departmentToFind);
        });

        get("/departments/:id/news", "application/json", (req, res) -> {
            int departmentId = Integer.parseInt(req.params("id"));

            Department departmentToFind = departmentDao.findById(departmentId);
            List<Review> allReviews;

            if (departmentToFind == null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists", req.params("id")));
            }

            allReviews = newsDao.getAllReviewsByDepartment(departmentId);

            return gson.toJson(allReviews);
        });

        get("/departments/:id/sortedReviews", "application/json", (req, res) -> { //// TODO: 1/18/18 generalize this route so that it can be used to return either sorted newss or unsorted ones.
            int departmentId = Integer.parseInt(req.params("id"));
            Department departmentToFind = departmentDao.findById(departmentId);
            List<Review> allReviews;
            if (departmentToFind == null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists", req.params("id")));
            }
            allReviews = newsDao.getAllReviewsByDepartmentSortedNewestToOldest(departmentId);
            return gson.toJson(allReviews);
        });

        get("/users", "application/json", (req, res) -> {
            return gson.toJson(userDao.getAll());
        });


        //CREATE
        post("/departments/new", "application/json", (req, res) -> {
            Department department = gson.fromJson(req.body(), Department.class);
            departmentDao.add(department);
            res.status(201);
            return gson.toJson(department);
        });

        //FILTERS
        exception(ApiException.class, (exception, req, res) -> {
            ApiException err = exception;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatusCode());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json");
            res.status(err.getStatusCode());
            res.body(gson.toJson(jsonMap));
        });


        after((req, res) ->{
            res.type("application/json");
        });

    }
}
