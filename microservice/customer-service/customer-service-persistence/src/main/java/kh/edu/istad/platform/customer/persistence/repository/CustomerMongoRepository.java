package kh.edu.istad.platform.customer.persistence.repository;

import kh.edu.istad.common.domain.valueobject.Customerid;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface CustomerMongoRepository extends ReactiveMongoRepository<CustomerDocument, Customerid> {
}