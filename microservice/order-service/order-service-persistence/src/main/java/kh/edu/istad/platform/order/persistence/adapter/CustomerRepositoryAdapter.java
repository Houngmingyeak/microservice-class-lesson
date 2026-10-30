//package kh.edu.istad.platform.order.persistence.adapter;
//
//import kh.edu.istad.platform.order.domain.entity.Customer;
//import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
//import kh.edu.istad.platform.order.domain.valueobject.CustomerId;
//import kh.edu.istad.platform.order.persistence.entity.CustomerEntity;
//import kh.edu.istad.platform.order.persistence.repository.CustomerJpaRepository;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Repository
//public class CustomerRepositoryAdapter implements CustomerRepository {
//
//    private final CustomerJpaRepository customerJpaRepository;
//
//    public CustomerRepositoryAdapter(CustomerJpaRepository customerJpaRepository) {
//        this.customerJpaRepository = customerJpaRepository;
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Optional<Customer> findCustomer(CustomerId customerId) {
//        /*
//         * មូលហេតុនៃ Error:
//         * នៅក្នុង Spring Data R2DBC (Reactive), method findById() ផ្តល់តម្លៃត្រឡប់មកវិញជា `Mono<CustomerEntity>`
//         * មិនមែនជា `Optional<CustomerEntity>` ដូចកាលប្រើ Spring Data JPA ទៀតឡើយ។
//         *
//         * ដំណោះស្រាយ:
//         * ហៅ `.blockOptional()` នៅលើ Mono ដើម្បីទាញយកទិន្នន័យពី Reactive Stream
//         * មកជា `java.util.Optional<CustomerEntity>` វិញ។
//         */
//        Optional<CustomerEntity> optionalEntity = customerJpaRepository
//                .findById(customerId.id())
//                .blockOptional(); // បម្លែងពី Mono<CustomerEntity> ទៅជា Optional<CustomerEntity>
//
//        // ត្រួតពិនិត្យប្រសិនបើគ្មានទិន្នន័យក្នុង Database
//        if (optionalEntity.isEmpty()) {
//            return Optional.empty();
//        }
//
//        // ទាញយក Entity រួច map ទៅកាន់ Domain Object
//        CustomerEntity customerEntity = optionalEntity.get();
//        Customer customer = new Customer(new CustomerId(customerEntity.getCustomerId()));
//
//        return Optional.of(customer);
//    }
//}