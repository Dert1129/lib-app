package com.lib_data;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String authorName;
    private String isbn;
    @Column(name = "genre")
    private String genre;
    @Transient
    private List<String> genreList;
    private String imageLink;
    @Column(name = "is_read")
    private int read;
    private int copies;
    private String description;
    private String category;
    private String publisher;
    private Date startDate;
    private Date endDate;
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getGenreList() {
        if (genre != null && !genre.isEmpty()) {
            try {
                return objectMapper.readValue(genre, List.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setGenreList(List<String> genreList) {
        if (genreList != null) {
            try {
                this.genre = objectMapper.writeValueAsString(genreList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            this.genre = null;
        }
    }
    
       public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageLink () {
        return imageLink;
    }

    public void setImageLink (String imageLink){
        this.imageLink = imageLink;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read){
        this.read = read;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(Integer copies){
        this.copies = copies;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher){
        this.publisher = publisher;
    }


    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
