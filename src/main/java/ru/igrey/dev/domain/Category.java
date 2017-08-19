package ru.igrey.dev.domain;

import ru.igrey.dev.constant.AnswerMessageText;
import ru.igrey.dev.entity.CategoryEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Category {
    private Long id;
    private LocalDateTime createDate;
    private String title;
    private Long userId;
    private List<Note> notes;

    public Category(Long id, Long userId, LocalDateTime createDate, String title, List<Note> notes) {
        this.id = id;
        this.createDate = createDate;
        this.title = title;
        this.notes = notes;
        this.userId = userId;
    }


    public Category(CategoryEntity categoryEntity, List<Note> notes) {
        this.id = categoryEntity.getId();
        this.createDate = categoryEntity.getCreateDate();
        this.title = categoryEntity.getTitle();
        this.userId = categoryEntity.getUserId();
        this.notes = notes;
    }

    public static Category createNewCategory(Long userId, String title) {
        return new Category(null, userId, null, title, null);
    }


    public String toBold(String text) {
        return "<b>" + text + "</b>";
    }

    public String toInlineFixedWidthCode(String text) {
        return "<code>" + text + "</code>";
    }

    public String toString() {
        return toBold(this.getTitle()) + "\n"
                + notes(this.getNotes());
    }

    private String notes(List<Note> notesList) {
        String result = "";
        Integer i = 1;
        if (notesList == null || notesList.isEmpty()) {
            return AnswerMessageText.EMPTY;
        }
        for (Note note : notesList) {
            result += toInlineFixedWidthCode(i + ") ") + note.getText() + "\n";
            i++;
        }
        return result;
    }


    public CategoryEntity toEntity() {
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
        return notes == null ? new ArrayList<>() : notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

}
