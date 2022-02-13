package com.demo.fulfillment;

import java.util.ArrayList;
import java.util.List;
import org.flywaydb.core.Flyway;

public class Migrations {
    public static void main(String[] args) {
        String[] connInfo = getDatabaseConnectionInfo();
        Flyway flyway = Flyway.configure().dataSource(connInfo[0], connInfo[1], connInfo[2]).load();

        // Matches functionality of https://flywaydb.org/documentation/usage/maven/
        switch (args[0]) {
            case "clean":
                flyway.clean();
                break;
            case "info":
                flyway.info();
                break;
            case "baseline":
                flyway.baseline();
                break;
            case "validate":
                flyway.validate();
                break;
            case "repair":
                flyway.repair();
                break;
            default:
                flyway.migrate();
        }
    }

    protected static String[] getDatabaseConnectionInfo() {
        List<String> connInfo = new ArrayList<>();

        // Example JDBC_DATABASE_URL
        // jdbc:postgresql://{host}:{port}/test?user={user_name}&password={password}
        String dbUrl = System.getenv("JDBC_DATABASE_URL");

        // Standard URI parser doesn't work with Postgresql URI format
        // See https://gist.github.com/BillyYccc/fb46baefab59d7aa2f0dc11d28e6f0e1
        String[] userParams = dbUrl.split("\\?")[1].split("&");

        connInfo.add(dbUrl);
        connInfo.add(userParams[0].split("=")[1]); //user
        connInfo.add(userParams[1].split("=")[1]); //pass

        return connInfo.toArray(String[]::new);
    }
}