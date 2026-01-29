package com.lab_lib.frontend.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.lab_lib.frontend.Interfaces.IPersonalLibraryService;
import com.lab_lib.frontend.Models.PersonalLibrary;
import com.lab_lib.frontend.Models.Book;
import com.lab_lib.frontend.Models.PaginatedResponse;
import com.lab_lib.frontend.Utils.HttpUtil;
import java.util.List;
import java.util.Map;

public class PersonalLibraryService implements IPersonalLibraryService {
    private final HttpUtil httpUtil;

    @Inject
    public PersonalLibraryService(HttpUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    @Override
    public List<PersonalLibrary> getPersonalLibraries() {
        return httpUtil.get("/personallibraries", new TypeReference<List<PersonalLibrary>>() {});
    }

    @Override
    public void addBookToLibrary(long libraryId, long bookId) {
        String endpoint = "/personallibraries/" + libraryId + "/book/" + bookId;
        httpUtil.postVoid(endpoint, null);
    }

    @Override
    public void removeBookFromLibrary(long libraryId, long bookId) {
        String endpoint = "/personallibraries/" + libraryId + "/book/" + bookId;
        httpUtil.deleteVoid(endpoint);
    }

    @Override
    public PaginatedResponse<Book> getBooksInLibrary(long libraryId, int page, int size) {
        String endpoint = "/personallibraries/" + libraryId + "/search_books?page=" + page + "&size=" + size;
        return httpUtil.get(endpoint, new TypeReference<PaginatedResponse<Book>>() {});
    }

    @Override
    public void deletePersonalLibrary(long libraryId) {
        String endpoint = "/personallibraries/" + libraryId;
        httpUtil.deleteVoid(endpoint);
    }

    @Override
    public PersonalLibrary getDefaultPersonalLibrary() {
        try {
            List<PersonalLibrary> libs = getPersonalLibraries();
            return (libs != null && !libs.isEmpty()) ? libs.get(0) : null;
        } catch (com.lab_lib.frontend.Exceptions.ApiException ex) {
            return null;
        }
    }

    @Override
    public PersonalLibrary createDefaultPersonalLibrary() {
        try {
            // Create a default personal library via controller typical endpoint: POST /personallibraries
            String defaultName = System.getenv("LIBRARY_DEFAULT_NAME");
            if (defaultName == null || defaultName.isBlank()) defaultName = "Libreria Personale";
            Map<String, String> body = java.util.Collections.singletonMap("name", defaultName);
            httpUtil.postVoid("/personallibraries", body);
            // After creation, return first/default
            return getDefaultPersonalLibrary();
        } catch (com.lab_lib.frontend.Exceptions.ApiException ex) {
            // If backend doesn't support creation, return null gracefully
            return null;
        }
    }

    @Override
    public PersonalLibrary createPersonalLibrary(String name) {
        try {
            String nm = (name != null && !name.isBlank()) ? name.trim() : "Libreria";
            Map<String, String> body = java.util.Collections.singletonMap("name", nm);
            httpUtil.postVoid("/personallibraries", body);
            // Fetch libraries and return the one matching the name
            List<PersonalLibrary> libs = getPersonalLibraries();
            if (libs != null) {
                for (PersonalLibrary pl : libs) {
                    if (pl != null && pl.getName() != null && pl.getName().equalsIgnoreCase(nm)) {
                        return pl;
                    }
                }
            }
            return null;
        } catch (com.lab_lib.frontend.Exceptions.ApiException ex) {
            System.err.println("[Groups] createPersonalLibrary failed for name='" + name + "': " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}
