package org.ecommerce.ecommerce.services.impl;

import org.ecommerce.ecommerce.models.ClassifyColor;
import org.ecommerce.ecommerce.repository.ClassifyColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassifyColorService {

    @Autowired
    private ClassifyColorRepository classifyColorRepository;

    public ClassifyColor getClassifyColorId(Long id){
        return classifyColorRepository.findById(id).orElse(null);
    }
}
