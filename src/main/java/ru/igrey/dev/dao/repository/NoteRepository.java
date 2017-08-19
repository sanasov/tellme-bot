package ru.igrey.dev.dao.repository;

import ru.igrey.dev.dao.NoteDao;
import ru.igrey.dev.domain.Note;
import ru.igrey.dev.entity.NoteEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NoteRepository {

    private NoteDao noteDao;

    public NoteRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
    }


    public Note saveNote(Note note) {
        return new Note(noteDao.save(note.toEntity()));
    }

    public List<Note> findByCategoryId(Long categoryId) {
        return Optional.ofNullable(noteDao.findByCategoryId(categoryId)).orElse(new ArrayList<>())
                .stream()
                .map(entity -> new Note(entity))
                .collect(Collectors.toList());
    }

    public Note findById(Long id) {
        return new Note(noteDao.findById(id));
    }

    public Note findLastInsertedNoteWithoutCategory(Long userId) {
        NoteEntity entity = noteDao.findLastInsertedNoteWithoutCategory(userId);
        return entity != null ? new Note(noteDao.findLastInsertedNoteWithoutCategory(userId)) : null;
    }

    public void delete(Long nodeId) {
        noteDao.delete(nodeId);
    }

    public void deleteByCategoryId(Long categoryId) {
        noteDao.deleteByCategoryId(categoryId);
    }
}
