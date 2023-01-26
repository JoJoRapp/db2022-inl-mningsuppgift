package se.iths;

public class Constants {
    public static final String JDBC_CONNECTION = "jdbc:mysql://localhost:3306/Chinook";
    public static final String JDBC_USER = "user1";
    public static final String JDBC_PASSWORD = "iths"; //insecure!
    public static final String SQL_COL_ARTIST_ID = "ArtistId";
    public static final String SQL_COL_ARTIST_NAME = "Name";
    public static final String SQL_COL_ALBUM_ID = "AlbumId";
    public static final String SQL_COL_ALBUM_TITLE = "Title";
    public static final String SQL_SELECT_ALL_ARTISTS_WITH_ALBUMS = "select ArtistId, AlbumId, Name, Title from Artist join Album using (ArtistId) order by ArtistId";
    public static final String TEST_USER = "Nisse Hult";
    public static final String TEST_ROLE = "Admin";
    public static final String TEST_NEWROLE = "User";
}
