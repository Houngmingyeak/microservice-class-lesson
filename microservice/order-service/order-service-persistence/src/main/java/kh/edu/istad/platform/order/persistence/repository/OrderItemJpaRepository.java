//package kh.edu.istad.platform.order.persistence.repository;
//
//import kh.edu.istad.platform.order.persistence.entity.OrderItemEntity;
//import org.springframework.data.repository.reactive.ReactiveCrudRepository;
//import reactor.core.publisher.Flux;
//
//import java.util.UUID;
//
///*
// * Repository នេះសម្រាប់គ្រប់គ្រង table "order_items" ដោយប្រើ R2DBC។
// *
// * ហេតុអ្វីបានជាត្រូវមាន Repository នេះដាច់ដោយឡែក?
// * - ក្នុង JPA យើងប្រើ @OneToMany + CascadeType.ALL ដូច្នេះ save OrderEntity
// *   ម្តង វា save items ទាំងអស់ដោយស្វ័យប្រវត្តិ។
// * - ក្នុង R2DBC មិនមាន relationship ឬ cascade ទេ។ ដូច្នេះត្រូវមាន Repository
// *   សម្រាប់ OrderItemEntity ផ្ទាល់ ដើម្បី save និង query items ដោយដៃ។
// *
// * ReactiveCrudRepository<OrderItemEntity, UUID>:
// * - OrderItemEntity = entity ដែលត្រូវគ្រប់គ្រង
// * - UUID = ប្រភេទនៃ @Id ក្នុង OrderItemEntity
// *   (ត្រូវតែដូចគ្នាជាមួយ field ដែលមាន @Id ក្នុង OrderItemEntity)
// *
// * Method ដែលមានស្រាប់ពី ReactiveCrudRepository:
// * - save(entity)      → Mono<OrderItemEntity>
// * - saveAll(entities) → Flux<OrderItemEntity>   (adapter ប្រើ method នេះ)
// * - findById(id)      → Mono<OrderItemEntity>
// */
//public interface OrderItemJpaRepository extends ReactiveCrudRepository<OrderItemEntity, UUID> {
//
//    /*
//     * Derived query: Spring Data បង្កើត SQL ដោយស្វ័យប្រវត្តិពីឈ្មោះ method
//     *   SELECT * FROM order_items WHERE order_id = ?
//     *
//     * សំខាន់: ឈ្មោះ "OrderId" ក្នុង findAllByOrderId ត្រូវតែត្រូវនឹងឈ្មោះ field
//     * "orderId" ក្នុង OrderItemEntity (adapter ហៅ itemEntity.setOrderId(...))។
//     *
//     * Return Flux ព្រោះ order មួយអាចមាន items ច្រើន។
//     */
//    Flux<OrderItemEntity> findAllByOrderId(UUID orderId);
//}