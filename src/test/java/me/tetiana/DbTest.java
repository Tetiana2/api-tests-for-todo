package me.tetiana;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbTest {
    static Connection con;

    private static Statement stmt;

    public static String DB_URL = "jdbc:mariadb://localhost:3306/appwrite";

    public static String DB_USER = "user";

    public static String DB_PASSWORD = "password";

    public static final String EMAIL = "tetiana.babenko1@gmail.com";

    @BeforeEach
    public void setUp() throws SQLException {
        Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        stmt = con.createStatement();
    }

    @Test
    public void deleteUserByEmail() throws SQLException {
        String query = format("DELETE FROM _1_users WHERE email = '%s'", EMAIL);
        int result = stmt.executeUpdate(query);
        assertEquals(1, result);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (con != null) {
            con.close();
        }
    }
}