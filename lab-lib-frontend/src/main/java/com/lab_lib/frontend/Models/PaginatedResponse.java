// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginatedResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private int size;
    private int number;
    private int numberOfElements;
    private boolean first;
    private boolean empty;

    public void setContent(List<T> content) {
        this.content = content;
    }

    // Getters
    public List<T> getContent() {
        return content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public boolean isLast() {
        return last;
    }

    public int getSize() {
        return size;
    }

    public int getNumber() {
        return number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isEmpty() {
        return empty;
    }
}
