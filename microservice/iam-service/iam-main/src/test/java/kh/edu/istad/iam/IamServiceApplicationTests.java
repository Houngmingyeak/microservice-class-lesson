package kh.edu.istad.iam;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Requires active PostgreSQL container/instance at localhost:9543")
class IamServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
