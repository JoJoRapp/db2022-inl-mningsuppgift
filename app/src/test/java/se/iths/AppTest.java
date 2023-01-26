package se.iths;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static se.iths.Constants.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppTest {

    private static long actualIdAfterInsert;

    public static Connection con = null;

    @BeforeAll
    public static void setUp() throws Exception {
        con = DriverManager.getConnection(JDBC_CONNECTION, JDBC_USER, JDBC_PASSWORD);
        con.createStatement().execute("drop table if exists User");
        con.createStatement().execute("create table User (Id int not null auto_increment, Name varchar(255), Role varchar(255), primary key (Id))");
    }

    @AfterAll
    public static void tearDown () throws Exception {
        con.close();
    }

    @Order(1)
    @Test
    void shouldCreateRowInDatabase() throws Exception {
        PreparedStatement stmt = con.prepareStatement("insert into User (Name, Role) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, TEST_USER);
        stmt.setString(2, TEST_ROLE);
        stmt.execute();
        ResultSet rs = stmt.getGeneratedKeys();

        assertTrue(rs.next(), "Should have a row with generated id");
        final long expectedIdAfterInsert = 1l;
        actualIdAfterInsert = rs.getLong(1);
        assertEquals(expectedIdAfterInsert, actualIdAfterInsert, "Should have correct id after insert");

        rs.close();
        stmt.close();
    }

    @Order(2)
    @Test
    void shouldFindRowInDatabase() throws Exception {
        PreparedStatement stmt = con.prepareStatement("select * from User where Id = ?");
        stmt.setLong(1, actualIdAfterInsert);
        ResultSet rs = stmt.executeQuery();

        assertTrue(rs.next(), "Should find one row");
        assertEquals(actualIdAfterInsert, rs.getLong("Id"), "Selected id should match");
        assertTrue(TEST_USER.equalsIgnoreCase(rs.getString("Name")), "Selected user should match");
        assertTrue(TEST_ROLE.equalsIgnoreCase(rs.getString("Role")), "Selected role should match");

        rs.close();
        stmt.close();
    }

    @Order(3)
    @Test
    void shouldUpdateRowInDatabase() throws Exception {
        PreparedStatement stmt = con.prepareStatement("update User set Role = ? where Id = ?");
        stmt.setString(1, TEST_NEWROLE);
        stmt.setLong(2, actualIdAfterInsert);
        stmt.execute();

        stmt = con.prepareStatement("select Role from User where Id = ?");
        stmt.setLong(1, actualIdAfterInsert);
        ResultSet rs = stmt.executeQuery();

        assertTrue(rs.next(), "Should find one row");
        assertTrue(TEST_NEWROLE.equalsIgnoreCase(rs.getString("Role")), "Updated role should match");

        rs.close();
        stmt.close();
    }

    @Order(4)
    @Test
    void shouldDeleteRowInDatabase() throws Exception {
        con.createStatement().execute("delete from User");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select count(*) from User");

        assertTrue(rs.next(), "Should find one row with count");
        assertEquals(0, rs.getInt(1), "Table should be empty");

        rs.close();
        stmt.close();
    }
}
