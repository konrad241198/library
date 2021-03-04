package pl.javastart.library.model;

import pl.javastart.library.exception.PublicationAlreayExistsException;

import java.io.Serializable;
import java.util.*;

public class Library implements Serializable {

    private Map<String, Publication> publications = new HashMap<>();
    private Map<String, LibraryUser> users = new HashMap<>();

    public Map<String, Publication> getPublications() {
        return publications;
    }

    public Collection<Publication> getSortedPublication(Comparator<Publication> comparator) {
        List<Publication> list = new ArrayList<>(this.publications.values());
        list.sort(comparator);
        return list;
    }

    public Map<String, LibraryUser> getUsers() {
        return users;
    }

    public Collection<LibraryUser> getSortedUsers(Comparator<LibraryUser> comparator) {
        List<LibraryUser> list = new ArrayList<>(users.values());
        list.sort(comparator);
        return list;
    }

    public Optional<Publication> findPublicationByTitle(String title) {
        return Optional.ofNullable(publications.get(title));
    }

    public void addPublication(Publication publication) {
        if(publications.containsKey(publication.getTitle())) {
            throw new PublicationAlreayExistsException("Publikacja o takim tytule już istnieje " + publication.getTitle());
        }
        publications.put(publication.getTitle(), publication);
    }

    public void addUser(LibraryUser user) {
        if(users.containsKey(user.getPesel())) {
            throw new PublicationAlreayExistsException("Użytkownik o takim peselu już istnieje " + user.getPesel());
        }
        users.put(user.getPesel(), user);
    }

    public boolean removePublication(Publication publication) {
        if(publications.containsValue(publication)) {
            publications.remove(publication.getTitle());
            return true;
        } else {
            return false;
        }
    }
}
