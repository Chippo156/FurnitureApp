package org.ecommerce.ecommerce.services;

import jakarta.validation.constraints.Size;
import org.ecommerce.ecommerce.dtos.SizeDTO;
import org.ecommerce.ecommerce.models.ProductSize;

public interface iSizeService {

    ProductSize createSize(SizeDTO sizeDTO);
}
