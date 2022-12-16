package ru.wmh.springcourse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@PropertySource("classpath:MvcApp.properties")
public class DataBaseConnection {
    @Value("${base.url}")
    private static String URL;
    @Value("${base.user}")
    private static String USERNAME;
    @Value("${base.pass}")
    private static String PASSWORD;

    public DataBaseConnection() {
    }

    public static Connection setConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Драйвер на месте");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Пробуем запустить коннект - " + URL + USERNAME + PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    /*
    Подгружаем postgresql драйвер
    Создаем соединение */
}