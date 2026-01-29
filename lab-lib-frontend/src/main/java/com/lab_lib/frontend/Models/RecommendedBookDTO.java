package com.lab_lib.frontend.Models;

import java.util.List;

public class RecommendedBookDTO {
    private String title;
    private List<String> userNicknames;

    public RecommendedBookDTO(String title, List<String> userNicknames) {
        this.title = title;
        this.userNicknames = userNicknames;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserNicknames(List<String> userNicknames) {
        this.userNicknames = userNicknames;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getUserNicknames() {
        return userNicknames;
    }
}
