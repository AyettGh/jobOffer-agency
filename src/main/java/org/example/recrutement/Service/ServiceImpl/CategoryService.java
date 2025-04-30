package org.example.recrutement.Service.ServiceImpl;

import org.example.recrutement.Entity.Application;
import org.example.recrutement.Entity.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> all();
    public Category add(Category category);
    public Category getById(Long id);
    public void delete(Long id);
    public Boolean existById(Long id);
}
