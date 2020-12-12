package com.company.micro.domain.ebook.customer1.v1.product;

import com.company.micro.common.ApplicationMode;
import com.company.micro.common.MessageByLocale;
import com.company.micro.v1.product.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("Wiley_Scholar_ProductService_V1")
public class ProductService extends com.company.micro.domain.ebook.v1.product.ProductService {

    @Autowired
    public ProductService(ProductRepo repository, ApplicationMode appMode, MessageByLocale messageByLocale) {
        super(repository, appMode, messageByLocale);
    }
}
