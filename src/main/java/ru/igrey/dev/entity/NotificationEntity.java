package ru.igrey.dev.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
public class NotificationEntity {
    private String note;
    private LocalDateTime notifyDate;
    private Long userId;
    private Long noteId;

    public NotificationEntity(String note, LocalDateTime notifyDate, Long userId, Long noteId) {
        this.note = note;
        this.notifyDate = notifyDate;
        this.userId = userId;
        this.noteId = noteId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(LocalDateTime notifyDate) {
        this.notifyDate = notifyDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }
}
