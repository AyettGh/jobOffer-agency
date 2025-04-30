package org.example.recrutement.Service;

import jakarta.persistence.EntityNotFoundException;
import org.example.recrutement.Entity.Category;
import org.example.recrutement.Repository.CategoryRepository;
import org.example.recrutement.Service.ServiceImpl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> all() {
        return categoryRepository.findAll();
    }

    @Override
    public Category add(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("not found")
        );
    }

    @Override
    public void delete(Long id) {
            categoryRepository.deleteById(id);
    }

    @Override
    public Boolean existById(Long id) {
        return categoryRepository.existsById(id);
    }
}
