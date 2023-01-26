package se.iths;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static se.iths.Constants.*;

public class App {
    public static void main(String[] args) {
        App app = new App();
        try {
            app.load();
        }
        catch (SQLException e) {
            System.err.println(String.format("Något gick fel vid inläsning av databas. (%s)", e));
        }
    }

    private void load() throws SQLException {
        Collection<Artist> artists = loadArtists();
        for (Artist a : artists) {
            System.out.println(a);
        }
    }

    private Collection<Artist> loadArtists() throws SQLException {
        Collection<Artist> artists = new ArrayList<>();
        Connection con = DriverManager.getConnection(JDBC_CONNECTION, JDBC_USER, JDBC_PASSWORD);
        ResultSet rs = con.createStatement().executeQuery(SQL_SELECT_ALL_ARTISTS_WITH_ALBUMS);
        long oldId = -1;
        Artist artist = null;
        while (rs.next()) {
            long id = rs.getLong(SQL_COL_ARTIST_ID);
            String name = rs.getString(SQL_COL_ARTIST_NAME);
            long albumId = rs.getLong(SQL_COL_ALBUM_ID);
            String title = rs.getString(SQL_COL_ALBUM_TITLE);
            if (id != oldId) {
                artist = new Artist(id, name);
                artists.add(artist);
                oldId = id;
            }
            artist.add(new Album(albumId, title));
        }
        rs.close();
        con.close();
        return artists;
    }
}
