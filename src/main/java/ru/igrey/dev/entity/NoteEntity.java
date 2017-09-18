package ru.igrey.dev.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
public class NoteEntity {

    private Long id;
    private LocalDateTime createDate;
    private String text;
    private Long categoryId;
    private Long userId;
    private String notifyRule;
    private String fileName;
    private String caption;
    private Integer timezoneInMinutes;

    public NoteEntity(Long id, LocalDateTime createDate, String text, Long categoryId, Long userId, String notifyRule, String fileName, String caption, Integer timezoneInMinutes) {
        this.id = id;
        this.createDate = createDate;
        this.text = text;
        this.categoryId = categoryId;
        this.userId = userId;
        this.notifyRule = notifyRule;
        this.fileName = fileName;
        this.caption = caption;
        this.timezoneInMinutes = timezoneInMinutes;
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

    public String getNotifyRule() {
        return notifyRule;
    }

    public void setNotifyRule(String notifyRule) {
        this.notifyRule = notifyRule;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getTimezoneInMinutes() {
        return timezoneInMinutes == null ? 0 : timezoneInMinutes;
    }

    public void setTimezoneInMinutes(Integer timezoneInMinutes) {
        this.timezoneInMinutes = timezoneInMinutes;
    }
}
