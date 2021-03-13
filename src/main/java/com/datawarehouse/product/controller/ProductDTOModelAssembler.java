package com.datawarehouse.product.controller;

import com.datawarehouse.product.dto.ProductDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductDTOModelAssembler implements RepresentationModelAssembler<ProductDTO, EntityModel<ProductDTO>> {

    @Override
    public EntityModel<ProductDTO> toModel(ProductDTO employee) {
        return new EntityModel(employee,
            linkTo(methodOn(ProductController.class).findById(employee.getId())).withSelfRel(),
            linkTo(methodOn(ProductController.class).getAvailability()).withRel("availability"));
    }
}
