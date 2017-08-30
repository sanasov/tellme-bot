package ru.igrey.dev.dao.repository;

import ru.igrey.dev.dao.CategoryDao;
import ru.igrey.dev.domain.Category;
import ru.igrey.dev.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    private NoteRepository noteRepository;
    private CategoryDao categoryDao;

    public CategoryRepository(NoteRepository noteRepository, CategoryDao categoryDao) {
        this.noteRepository = noteRepository;
        this.categoryDao = categoryDao;
    }


    public List<Category> findCategoryByUserId(Long userId) {
        List<CategoryEntity> categoryEntities = categoryDao.findByUserId(userId);
        if (categoryEntities == null) {
            return null;
        }
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

    public void deleteCategoryById(Long categoryId) {
        categoryDao.delete(categoryId);
        noteRepository.deleteByCategoryId(categoryId);
    }

    public Category findCategoryById(Long id) {
        CategoryEntity categoryEntity = categoryDao.findById(id);
        return categoryEntity == null ? null :
                new Category(
                        categoryEntity,
                        noteRepository.findByCategoryId(categoryEntity.getId()));
    }

    public Category findCategoryByUserIdAndCategoryName(Long userId, String title) {
        CategoryEntity categoryEntity = categoryDao.findByUserIdAndTitle(userId, title);
        return categoryEntity == null ? null :
                new Category(
                        categoryEntity,
                        noteRepository.findByCategoryId(categoryEntity.getId()));
    }

    public Category createCategoryIfNotExist(Long userId, String title) {
        Category category = findCategoryByUserIdAndCategoryName(userId, title);
        if (category == null) {
            category = Category.createNewCategory(userId, title);
        }
        return saveCategory(category);
    }

    public Category saveCategory(Category category) {
        return new Category(categoryDao.save(category.toEntity()), null);
    }

}
