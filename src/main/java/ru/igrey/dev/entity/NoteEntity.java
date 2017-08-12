package ru.igrey.dev.entity;

import java.time.LocalDateTime;

public class NoteEntity {

    private Long id;
    private LocalDateTime createDate;
    private String text;
    private Long categoryId;
    private Long userId;

    public NoteEntity(Long id, LocalDateTime createDate, String text, Long categoryId, Long userId) {
        this.id = id;
        this.createDate = createDate;
        this.text = text;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    public NoteEntity() {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
