package pl.javastart.library.io.file;

import pl.javastart.library.exception.DataExportException;
import pl.javastart.library.exception.DataImportException;
import pl.javastart.library.exception.InvalidDataException;
import pl.javastart.library.model.*;

import java.io.*;
import java.util.Collection;
import java.util.Scanner;

public class CsvFileManager implements FileManager {
    private static final String FILE_NAME = "Library.csv";
    private static final String USERS_FILE_NAME = "Library_users.csv";

    @Override
    public Library importData() {
        Library library = new Library();
        importPublications(library);
        importUsers(library);
        return library;
    }

    private void importUsers(Library library) {
        try(Scanner fileReader = new Scanner(new File(USERS_FILE_NAME))) {
            while(fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                LibraryUser user = createUserFromString(line);
                library.addUser(user);
            }
        } catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku " + USERS_FILE_NAME);
        }
    }

    private LibraryUser createUserFromString(String line) {
        String[] split = line.split(";");
        String firstName = split[0];
        String lasstName = split[1];
        String pesel = split[2];

        return new LibraryUser(firstName, lasstName, pesel);
    }

    private void importPublications(Library library) {
        try(Scanner fileReader = new Scanner(new File(FILE_NAME))) {
            while(fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                Publication publication = createObjectFromString(line);
                library.addPublication(publication);
            }
        } catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku " + FILE_NAME);
        }
    }

    private Publication createObjectFromString(String line) {
        String[] split = line.split(";");
        String type = split[0];
        if(Book.TYPE.equals(type)) {
            return createBook(split);
        } else if(Magazine.TYPE.equals(type)) {
            return createMagazine(split);
        }
        throw new InvalidDataException("Nieznany typ publikacji " + type);
    }

    private Magazine createMagazine(String[] split) {
        String title = split[1];
        String publisher = split[2];
        int year = Integer.parseInt(split[3]);
        int month = Integer.parseInt(split[4]);
        int day = Integer.parseInt(split[5]);
        String language = split[6];

        return new Magazine(title, publisher, language, year, month, day);
    }

    private Book createBook(String[] split) {
        String title = split[1];
        String publisher = split[2];
        int year = Integer.parseInt(split[3]);
        String author = split[4];
        int pages = Integer.parseInt(split[5]);
        String isbn = split[6];

        return new Book(title, author, year, pages, publisher, isbn);
    }

    @Override
    public void exportData(Library library) {
        exportPublications(library);
        exportUsers(library);
    }

    private void exportUsers(Library library) {
        Collection<LibraryUser> users = library.getUsers().values();
        exportToCsv(users, USERS_FILE_NAME);
    }

    private void exportPublications(Library library) {
        Collection<Publication> publications = library.getPublications().values();
        exportToCsv(publications, FILE_NAME);
    }

    private <T extends CsvConvertible> void exportToCsv(Collection<T> collection, String fileName) {
        try (
                var fileWriter = new FileWriter(fileName);
                var bufferedWriter = new BufferedWriter(fileWriter);
        ){
            for (T element : collection) {
                bufferedWriter.write(element.toCsv());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new DataExportException("Błąd zapius danych do pliku " + fileName);
        }
    }

}
