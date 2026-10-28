package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.common.domain.Money;
import kh.edu.istad.platform.order.domain.entity.Business;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.order.domain.valueobject.BusinessId;
import kh.edu.istad.platform.order.domain.valueobject.ProductId;
import kh.edu.istad.platform.order.persistence.entity.BusinessEntity;
import kh.edu.istad.platform.order.persistence.entity.BusinessProductEntity;
import kh.edu.istad.platform.order.persistence.repository.BusinessJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BusinessRepositoryAdapter implements BusinessRepository {

    private final BusinessJpaRepository businessJpaRepository;

    public BusinessRepositoryAdapter(BusinessJpaRepository businessJpaRepository) {
        this.businessJpaRepository = businessJpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Business> findBusinessInformation(BusinessId businessId) {
        // ប្តូរមកប្រើ .blockOptional() ដើម្បីទាញយកទិន្នន័យពី Mono
        Optional<BusinessEntity> optionalEntity = businessJpaRepository.findById(businessId.id()).blockOptional();

        if (optionalEntity.isEmpty()) {
            return Optional.empty();
        }
        BusinessEntity businessEntity = optionalEntity.get();

        List<Product> products = new ArrayList<>();
        if (businessEntity.getProducts() != null) {
            for (BusinessProductEntity productEntity : businessEntity.getProducts()) {
                Product product = new Product(
                        new ProductId(productEntity.getProductId()),
                        productEntity.getName(),
                        new Money(productEntity.getPrice())
                );
                products.add(product);
            }
        }

        Business business = new Business(
                new BusinessId(businessEntity.getBusinessId()),
                products,
                businessEntity.isActive()
        );

        return Optional.of(business);
    }
}