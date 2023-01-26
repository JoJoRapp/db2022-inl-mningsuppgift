package se.iths;

import java.util.ArrayList;
import java.util.Collection;

public class Artist {
    private final long id;
    private String name;
    private Collection<Album> albums = new ArrayList<>();

    public Artist(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(Album album) {
        albums.add(album);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(String.valueOf(id));
        sb.append(": ");
        sb.append(name);
        sb.append("\nAlbums:\n");
        for (Album a : albums) {
            sb.append("\t");
            sb.append(a);
            sb.append("\n");
        }
        return sb.toString();
    }
}
