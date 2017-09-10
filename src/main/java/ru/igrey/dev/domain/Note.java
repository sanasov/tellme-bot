package ru.igrey.dev.domain;

import ru.igrey.dev.constant.Delimiter;
import ru.igrey.dev.entity.NoteEntity;

import java.time.LocalDateTime;

public class Note {
    private Long id;
    private Long categoryId;
    private Long userId;
    private LocalDateTime createDate;
    private String text;
    private String notifyRule;

    public Note(Long id, Long categoryId, Long userId, LocalDateTime createDate, String text, String notifyRule) {
        this.id = id;
        this.categoryId = categoryId;
        this.userId = userId;
        this.createDate = createDate;
        this.text = text;
        this.notifyRule = notifyRule;
    }

    public Note(NoteEntity entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.createDate = entity.getCreateDate();
        this.text = entity.getText();
        this.notifyRule = entity.getNotifyRule();
    }

    public static Note createNewNote(String text, Long categoryId, Long userId) {
        String[] notifyRuleAndNoteName = text.split(Delimiter.NOTIFY_DELIMITER);
        if (notifyRuleAndNoteName.length == 2) {
            return new Note(null, categoryId, userId, null, notifyRuleAndNoteName[1].trim(), notifyRuleAndNoteName[0].trim());
        }
        return new Note(null, categoryId, userId, null, text, null);
    }


    public NoteEntity toEntity() {
        return new NoteEntity(id, createDate, text, categoryId, userId, notifyRule);
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

    public String getNotifyRule() {
        return notifyRule;
    }

    public void setNotifyRule(String notifyRule) {
        this.notifyRule = notifyRule;
    }
}
