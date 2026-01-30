// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Models;

import java.util.List;

public class RecommendedBookDTO {
    private String title;
    private List<String> userNicknames;

    public RecommendedBookDTO() {
    }

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
