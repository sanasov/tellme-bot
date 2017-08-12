package ru.igrey.dev.dao.repository;

import ru.igrey.dev.dao.NoteDao;
import ru.igrey.dev.domain.Note;

import java.util.List;
import java.util.stream.Collectors;

public class NoteRepository {

    private NoteDao noteDao;

    public NoteRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
    }


    public Note saveNote(Note note, Long categoryId) {
        return new Note(noteDao.save(note.toEntity(categoryId)));
    }

    public List<Note> findByCategoryId(Long categoryId) {
        return noteDao.findByCategoryId(categoryId).stream()
                .map(entity -> new Note(entity))
                .collect(Collectors.toList());
    }
}
