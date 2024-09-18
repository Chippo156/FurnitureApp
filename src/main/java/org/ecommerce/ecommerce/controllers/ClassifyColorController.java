package org.ecommerce.ecommerce.controllers;

import org.ecommerce.ecommerce.models.ClassifyColor;
import org.ecommerce.ecommerce.services.impl.ClassifyColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/classify-color")
public class ClassifyColorController {
    @Autowired
    private ClassifyColorService classifyColorService;
    @GetMapping("/{id}")
    public ResponseEntity<ClassifyColor> getClassifyColorById(@PathVariable Long id) {
        return ResponseEntity.ok(classifyColorService.getClassifyColorId(id));
    }
}
