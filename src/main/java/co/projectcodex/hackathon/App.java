package co.projectcodex.hackathon;

import java.util.ArrayList;
import java.util.List;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    static Connection getDatabaseConnection(String defualtJdbcUrl) throws URISyntaxException, SQLException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String database_url = processBuilder.environment().get("DATABASE_URL");
        if (database_url != null) {

            URI uri = new URI(database_url);
            String[] hostParts = uri.getUserInfo().split(":");
            String username = hostParts[0];
            String password = hostParts[1];
            String host = uri.getHost();

            int port = uri.getPort();

            String path = uri.getPath();
            String url = String.format("jdbc:postgresql://%s:%s%s", host, port, path);

            return DriverManager.getConnection(url, username, password);

        }

        return DriverManager.getConnection(defualtJdbcUrl);

    }

    public static void main(String[] args) {
        try  {

            List<Person> people = new ArrayList<>();

            staticFiles.location("/public");
            port(getHerokuAssignedPort());

//            Connection connection = getDatabaseConnection("jdbc:postgresql://localhost/spark_hbs_jdbi");

            get("/", (req, res) -> {

                Map<String, Object> map = new HashMap<>();
                map.put("people", people);

                return new ModelAndView(map, "index.handlebars");

            }, new HandlebarsTemplateEngine());


            post("/person", (req, res) -> {

                String firstName = req.queryParams("firstName");
                String lastName = req.queryParams("lastName");
                String email = req.queryParams("email");

                Person person = new Person(firstName, lastName, email);

                people.add(person);

                res.redirect("/");
                return "";
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
