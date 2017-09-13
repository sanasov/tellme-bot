package ru.igrey.dev.dao.repository;

import ru.igrey.dev.dao.NoteDao;
import ru.igrey.dev.domain.Note;
import ru.igrey.dev.domain.Notification;
import ru.igrey.dev.entity.NoteEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NoteRepository {

    private NoteDao noteDao;
    private NotificationRepository notificationRepository;

    public NoteRepository(NoteDao noteDao, NotificationRepository notificationRepository) {
        this.noteDao = noteDao;
        this.notificationRepository = notificationRepository;
    }


    public Note saveNote(Note note) {
        if (findById(note.getId()) == null) {
            Note savedNote = new Note(noteDao.insert(note.toEntity()));
            List<Notification> notifications = savedNote.createNotifications();
            for (Notification notification : notifications) {
                notificationRepository.save(notification);
            }
            return savedNote;
        }
        return new Note(noteDao.update(note.toEntity()));
    }

    public Note updateNote(Note note) {
        if (findById(note.getId()) == null) {
            Note savedNote = new Note(noteDao.insert(note.toEntity()));
            List<Notification> notifications = savedNote.createNotifications();
            for (Notification notification : notifications) {
                notificationRepository.save(notification);
            }
            return savedNote;
        }
        List<Notification> notifications = note.createNotifications();
        for (Notification notification : notifications) {
            notificationRepository.save(notification);
        }
        return new Note(noteDao.update(note.toEntity()));
    }

    public List<Note> findByCategoryId(Long categoryId) {
        return Optional.ofNullable(noteDao.findByCategoryId(categoryId)).orElse(new ArrayList<>())
                .stream()
                .map(entity -> new Note(entity))
                .collect(Collectors.toList());
    }


    public Note findById(Long id) {
        NoteEntity entity = noteDao.findById(id);
        return entity != null ? new Note(entity) : null;
    }

    public Note findLastInsertedNoteWithoutCategory(Long userId) {
        NoteEntity entity = noteDao.findLastInsertedNoteWithoutCategory(userId);
        return entity != null ? new Note(noteDao.findLastInsertedNoteWithoutCategory(userId)) : null;
    }

    public void delete(Long nodeId) {
        noteDao.delete(nodeId);
        notificationRepository.delete(nodeId);
    }

    public void deleteByCategoryId(Long categoryId) {
        noteDao.deleteByCategoryId(categoryId);
    }

    public List<Note> findAllNewUserNotes(Long userId) {
        return Optional.ofNullable(noteDao.findAllNewUserNotes(userId)).orElse(new ArrayList<>())
                .stream()
                .map(entity -> new Note(entity))
                .collect(Collectors.toList());
    }

    public void saveNewNotesInCategory(Long userId, Long categoryId) {
        List<Note> newNotes = findAllNewUserNotes(userId);
        for (Note note : newNotes) {
            note.setCategoryId(categoryId);
            saveNote(note);
        }
    }
}
