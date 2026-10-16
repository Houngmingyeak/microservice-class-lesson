package kh.edu.istad.platform.business.persistence.adapter;

import kh.edu.istad.platform.business.domain.entity.Business;
import kh.edu.istad.platform.business.domain.entity.Product;
import kh.edu.istad.platform.business.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.business.persistence.document.BusinessDocument;
import kh.edu.istad.platform.business.persistence.document.ProductDocument;
import kh.edu.istad.platform.business.persistence.repository.BusinessMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BusinessRepositoryAdapter implements BusinessRepository {

    private final BusinessMongoRepository businessMongoRepository;

    public BusinessRepositoryAdapter(BusinessMongoRepository businessMongoRepository) {
        this.businessMongoRepository = businessMongoRepository;
    }

    @Override
    public Mono<Business> save(Business business) {
        BusinessDocument businessDocument = new BusinessDocument();
        businessDocument.setBusinessId(business.getId().value().toString());
        businessDocument.setLegalName(business.getBusinessName().legalName());
        businessDocument.setDisplayName(business.getBusinessName().displayName());
        businessDocument.setEmail(business.getBusinessContact().email());
        businessDocument.setPhoneNumber(business.getBusinessContact().phoneNumber());
        businessDocument.setActive(business.isActive());

        List<ProductDocument> productDocuments = new ArrayList<>();
        for (Product product : business.getProducts()) {
            ProductDocument productDocument = new ProductDocument();
            productDocument.setProductId(product.getId().value().toString());
            productDocument.setName(product.getName());
            productDocument.setPrice(product.getPrice().amount());
            productDocument.setQuantity(product.getQuantity());
            productDocument.setAvailable(product.isAvailable());
            productDocuments.add(productDocument);
        }
        businessDocument.setProducts(productDocuments);

        return businessMongoRepository.save(businessDocument)
                .map(savedDocument -> business);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return businessMongoRepository.existsByEmail(email);
    }
}
