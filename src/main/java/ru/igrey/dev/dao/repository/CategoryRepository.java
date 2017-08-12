package ru.igrey.dev.dao.repository;

import ru.igrey.dev.dao.CategoryDao;
import ru.igrey.dev.dao.TelegramUserDao;
import ru.igrey.dev.domain.Category;
import ru.igrey.dev.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    private TelegramUserDao userDao;
    private NoteRepository noteRepository;
    private CategoryDao categoryDao;

    public CategoryRepository(NoteRepository noteRepository, CategoryDao categoryDao) {
        this.noteRepository = noteRepository;
        this.categoryDao = categoryDao;
    }


    public List<Category> findCategoryByUserId(Long userId) {
        List<CategoryEntity> categoryEntities = categoryDao.findByUserId(userId);
        List<Category> categories = new ArrayList<>();
        for (CategoryEntity categoryEntity : categoryEntities) {
            categories.add(
                    new Category(
                            categoryEntity,
                            noteRepository.findByCategoryId(categoryEntity.getId())
                    )
            );
        }
        return categories;
    }

    public Category findCategoryByUserIdAndTitle(Long userId, String title) {
        CategoryEntity categoryEntity = categoryDao.findByUserIdAndTitle(userId, title);
        return new Category(
                categoryEntity,
                noteRepository.findByCategoryId(categoryEntity.getId()));
    }

    public void saveCategory(Category category, Long userId) {
        categoryDao.save(category.toEntity(userId));
    }

}
