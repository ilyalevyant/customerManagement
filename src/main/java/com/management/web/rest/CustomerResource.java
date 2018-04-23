package com.management.web.rest;

import com.management.domain.Customer;
import com.management.repository.CustomerRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
@Api(value = "/api")
public class CustomerResource {

    Logger log = Logger.getLogger("");
    @Inject
    private CustomerRepository customerRepository;

    @GetMapping("/customers")
    @ApiOperation(value = "getAllCustomers")
    public ResponseEntity<?> getAllCustomers(){
        log.info("GET /customers");
        List<Customer> result = customerRepository.findAll();
        if(result.isEmpty()) return ResponseEntity.badRequest().body("NO DATA");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/customer/{id}")
    @ApiOperation(value = "getCustomerById")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id){
        log.info("GET /customer");
        Customer customer = customerRepository.findOne(id);
        if(customer==null)return ResponseEntity.badRequest().body("NO CUSTOMERS WAS FOUND BY ID:"+id);
        return ResponseEntity.ok().body(customer);
    }

    @PostMapping("/customer")
    @ApiOperation(value = "addCustomer")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer){
        log.info("POST /customer");
        if (customer.getId() != null) {
            return ResponseEntity.badRequest().body("ID should be NULL for new customer");
        }
        customerRepository.save(customer);
        return getAllCustomers();
    }


    @PutMapping("/customer")
    @ApiOperation(value = "updateCustomer")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer){
        log.info("PUT /customer");
        if (customer.getId()==null) return addCustomer(customer);
        customerRepository.save(customer);
        return getCustomerById(customer.getId());
    }

    @DeleteMapping("/customer/{id}")
    @ApiOperation(value = "deleteCustomer")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id){
        log.info("DELETE /customer");
        try {
            customerRepository.delete(id);
            return getAllCustomers();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("CHECK CUSTOMER ID AND TRY AGAINE");
        }
    }
}
