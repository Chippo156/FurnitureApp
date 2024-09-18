package org.ecommerce.ecommerce.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.ecommerce.ecommerce.models.Role;
import org.ecommerce.ecommerce.responses.ProductResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface iRoleService {

    List<Role> getAllRoles();

}
