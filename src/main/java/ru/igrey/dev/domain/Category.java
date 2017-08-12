package ru.igrey.dev.domain;

import lombok.ToString;
import ru.igrey.dev.entity.CategoryEntity;

import java.time.LocalDateTime;
import java.util.List;

@ToString
public class Category {
    private Long id;
    private LocalDateTime createDate;
    private String title;
    private List<Note> notes;

    public Category(Long id, Long userId, LocalDateTime createDate, String title, List<Note> notes) {
        this.id = id;
        this.createDate = createDate;
        this.title = title;
        this.notes = notes;
    }


    public Category(CategoryEntity categoryEntity, List<Note> notes) {
        this.id = categoryEntity.getId();
        this.createDate = categoryEntity.getCreateDate();
        this.title = categoryEntity.getTitle();
        this.notes = notes;
    }


    public CategoryEntity toEntity(Long userId) {
        return new CategoryEntity(id, userId, createDate, title);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

}
