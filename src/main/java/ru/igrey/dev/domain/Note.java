package ru.igrey.dev.domain;

import ru.igrey.dev.entity.NoteEntity;

import java.time.LocalDateTime;

public class Note {
    private Long id;
    private Long categoryId;
    private Long userId;
    private LocalDateTime createDate;
    private String text;

    public Note(Long id, Long categoryId, Long userId, LocalDateTime createDate, String text) {
        this.id = id;
        this.categoryId = categoryId;
        this.userId = userId;
        this.createDate = createDate;
        this.text = text;
    }

    public Note(NoteEntity entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.createDate = entity.getCreateDate();
        this.text = entity.getText();
    }

    public static Note createNewNote(String text, Long categoryId, Long userId) {
        return new Note(null, categoryId, userId, null, text);
    }


    public NoteEntity toEntity() {
        return new NoteEntity(id, createDate, text, categoryId, userId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}
