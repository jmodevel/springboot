package com.jmo.devel.bookstore.api.hateoas;

import org.springframework.hateoas.EntityModel;

public class City extends EntityModel<String> {

    public City(String content) {
        super(content);
    }

}
