package kh.edu.istad.platform.order;

import kh.edu.istad.platform.order.persistence.repository.BusinessReactiveRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BusinessTest {

    @Autowired
    private BusinessReactiveRepository businessReactiveRepository;

    @Test
    void testFindBusiness() {
        // collectList() ប្រមូលលទ្ធផលទាំងអស់ ហើយ block() រង់ចាំរហូតទាល់តែ query ចប់
        // ទើប query ទៅ Database ពិតប្រាកដ
        var businesses = businessReactiveRepository.findAll().collectList().block();
        System.out.println("Total businesses: " + businesses.size());
    }
}
