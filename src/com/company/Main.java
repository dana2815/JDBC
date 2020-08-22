package com.company;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.TimeZone;

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/moviesrental?serverTimezone=" +
            TimeZone.getDefault().getID();
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "92eH.2815";
    private static Connection DB_CONNECTION;
    private static final String QUERY = "SELECT title, releaseDate FROM moviesinfo";
    private static final String QUERY_PARAMETRIZED = "SELECT title, releaseDate FROM moviesinfo where releaseDate " + "between ? and ?";
    private static final String INSERT_MOVIE = "Insert into moviesInfo (title, genre, releaseDate, description)" + "values(?, ?, ?, ?)";
    private static final String INSERT_CUSTOMER = "Insert into customers (fullName, phone, email, address)" + "values(?, ?, ?, ?)";


    public static void main(String[] args) {


        String url = DB_URL;
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", DB_USER);
        connectionProperties.put("password", DB_PASSWORD);

        try {
            DB_CONNECTION = DriverManager.getConnection(DB_URL, connectionProperties);
            insertMovie("Matrix", "SFX", LocalDate.of(1999, 01, 01), "Film z Keanu Reavsem");
            DB_CONNECTION.close();
        } catch
        (SQLException e) {
            e.printStackTrace();
        }

        try {
            DB_CONNECTION = DriverManager.getConnection(DB_URL, connectionProperties);
            insertCustomer("Jan Kowalski", 123456789 , "asda@dsf.sdfsd", "ul.zielona");
            DB_CONNECTION.close();
        } catch
        (SQLException e) {
            e.printStackTrace();
        }

        try {
            DB_CONNECTION = DriverManager.getConnection(DB_URL, connectionProperties);
            LocalDate[] dates = getDateFromUser();
            printMovieReleasedBetween(dates[0], dates[1]);
        } catch
        (SQLException e) {
            e.printStackTrace();
        }

        try {
            DB_CONNECTION = DriverManager.getConnection(DB_URL, connectionProperties);
            printMovieReleasedBetween(LocalDate.of(2019, 1, 1),
                    LocalDate.of(2019, 12, 31));
            DB_CONNECTION.close();
        } catch
        (SQLException e) {
            e.printStackTrace();
        }


        try (Connection connection = DriverManager.getConnection(DB_URL, connectionProperties)) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            System.out.println("Movies:");
            System.out.println("Title:" + " \t|\t" + "Realease date:");

            while (rs.next()) {  //next - możliwość przechodzenia przez kolekcje
                System.out.println(rs.getString("title") + " \t|\t" + rs.getDate(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void printMovieReleasedBetween(final LocalDate from, final LocalDate to) {
        try (PreparedStatement stmt2 = DB_CONNECTION.prepareStatement(QUERY_PARAMETRIZED)) {
            ;
            stmt2.setDate(1, java.sql.Date.valueOf(from));
            stmt2.setDate(2, java.sql.Date.valueOf(to));

            try (ResultSet rs = stmt2.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("title") + " \t|\t" + rs.getDate(2));
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    private static LocalDate[] getDateFromUser() {
        Scanner scanner = new Scanner(System.in);
        LocalDate[] localDate = new LocalDate[2];

        System.out.println("Podaj date od");
        localDate[0] = LocalDate.parse(scanner.nextLine());
        System.out.println("Podaj date do");
        localDate[1] = LocalDate.parse(scanner.nextLine());

        return localDate;
    }

    private static void insertMovie(String title, String genre, LocalDate releaseDate, String description) {
        try (PreparedStatement stmt2 = DB_CONNECTION.prepareStatement(INSERT_MOVIE)) {
            stmt2.setString(1, title);
            stmt2.setString(2, genre);
            stmt2.setDate(3, java.sql.Date.valueOf(releaseDate));
            stmt2.setString(4, description);

            stmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void insertCustomer(String fullName, int phone, String email, String address) {
        try (PreparedStatement stmt2 = DB_CONNECTION.prepareStatement(INSERT_CUSTOMER)) {
            stmt2.setString(1, fullName);
            stmt2.setInt(2, phone);
            stmt2.setString(3, email);
            stmt2.setString(4, address);

            stmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}