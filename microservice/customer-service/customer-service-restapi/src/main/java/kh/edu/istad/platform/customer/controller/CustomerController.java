package kh.edu.istad.platform.customer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void initiateCustomer(
            @RequestBody Cus
    ){}

}
