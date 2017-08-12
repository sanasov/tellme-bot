package ru.igrey.dev.domain;

import ru.igrey.dev.entity.NoteEntity;

import java.time.LocalDateTime;

public class Note {
    private Long id;
    private Long userId;
    private LocalDateTime createDate;
    private String text;

    public Note(Long id, Long category, Long userId, LocalDateTime createDate, String text) {
        this.id = id;
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


    public NoteEntity toEntity(Long categoryId) {
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

}
