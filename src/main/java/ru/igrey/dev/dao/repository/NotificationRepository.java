package ru.igrey.dev.dao.repository;

import ru.igrey.dev.dao.NotificationDao;
import ru.igrey.dev.domain.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NotificationRepository {

    private NotificationDao notificationDao;

    public NotificationRepository(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }


    public void save(Notification notification) {
        if (notification == null) {
            return;
        }
        notificationDao.insert(notification.toEntity());
    }

    public List<Notification> findAll() {
        return Optional.ofNullable(notificationDao.findAll()).orElse(new ArrayList<>())
                .stream()
                .map(entity -> Notification.fromEntity(entity))
                .collect(Collectors.toList());
    }

    public void delete(Long nodeId) {
        notificationDao.deleteByNoteId(nodeId);
    }

    public void deleteById(Long id) {
        notificationDao.deleteById(id);
    }

}
