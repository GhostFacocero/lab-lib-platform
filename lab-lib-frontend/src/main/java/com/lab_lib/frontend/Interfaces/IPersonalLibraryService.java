// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Interfaces;

import java.util.List;
import com.lab_lib.frontend.Models.PersonalLibrary;
import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.PaginatedResponse;

public interface IPersonalLibraryService {
    List<PersonalLibrary> getPersonalLibraries();
    void addBookToLibrary(long libraryId, long bookId);
    void removeBookFromLibrary(long libraryId, long bookId);
    PaginatedResponse<Book> getBooksInLibrary(long libraryId, int page, int size);
    default PaginatedResponse<Book> getBooksInLibrary(long libraryId) {
        return getBooksInLibrary(libraryId, 0, 50);
    }
    void deletePersonalLibrary(long libraryId);
    PersonalLibrary getDefaultPersonalLibrary();
    PersonalLibrary createDefaultPersonalLibrary();
    PersonalLibrary createPersonalLibrary(String name);
}
