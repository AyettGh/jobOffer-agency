package org.example.recrutement.Controller;

import org.example.recrutement.Entity.Application;
import org.example.recrutement.Entity.Category;
import org.example.recrutement.Service.ServiceImpl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/recrutement")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/allcategory")
    public List<Category> all()
    {
        return categoryService.all();
    }
    @GetMapping("/getcategory/{id}")
    public Category getById(@PathVariable Long id)
    {
        return categoryService.getById(id);
    }
    @PostMapping("/addcategory")
    public Category save(@RequestBody Category category)
    {
        return categoryService.add(category);
    }

    @PutMapping("/updatecategory/{id}")
    public ResponseEntity<?> update(@RequestBody Category category, @PathVariable Long id)
    {
        if( categoryService.existById(id))
        {
            Category category1=categoryService.getById(id);
            category1.setOffer(category.getOffer());
            category1.setName(category.getName());
            category1.setText(category.getText());
            categoryService.add(category1);
            return ResponseEntity.ok().body(category1);
        }
        else {
            HashMap<String,String> map=new HashMap<>();
            map.put("error","error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
    }

    @DeleteMapping("/deletecategory/{id}")
    public ResponseEntity<?> delete(@RequestBody Category category,@PathVariable Long id) {
        if (categoryService.existById(id))
        {
            categoryService.delete(id);
            HashMap<Category,String> map=new HashMap<>();
            map.put(category,"deleted with success");
            return
                    ResponseEntity.ok().body(map);
        }
        else{
            HashMap<String,String>map=new HashMap<>();
            map.put("error","in deleting");
            return    ResponseEntity.ok().body(map);

        }
    }
}
