package com.cakemate.cake_platform;

import com.cakemate.cake_platform.dummy.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class CakePlatformApplication {


    public static void main(String[] args) {
        SpringApplication.run(CakePlatformApplication.class, args);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//        CustomerBulkInsert customerBulkInsert = new CustomerBulkInsert(passwordEncoder);
//        System.out.println("customerBulkInsert 시작");
//        long startCustomerInsert = System.currentTimeMillis();
//        customerBulkInsert.bulkInsert();
//        long endCustomerInsert = System.currentTimeMillis();
//        System.out.println("customerBulkInsert 시간: " + (endCustomerInsert - startCustomerInsert) / 1000.0 + "초");
//
//        OwnerBulkInsert ownerBulkInsert = new OwnerBulkInsert(passwordEncoder);
//        System.out.println("ownerBulkInsert 시작");
//        long startOwnerInsert = System.currentTimeMillis();
//        ownerBulkInsert.bulkInsert();
//        long endOwnerInsert = System.currentTimeMillis();
//        System.out.println("ownerBulkInsert 시간: " + (endOwnerInsert - startOwnerInsert) / 1000.0 + "초");
//
//        StoreBulkInsert storeBulkInsert = new StoreBulkInsert();
//        System.out.println("storeBulkInsert 시작");
//        long startStoreInsert = System.currentTimeMillis();
//        storeBulkInsert.bulkInsert();
//        long endStorerInsert = System.currentTimeMillis();
//        System.out.println("storeBulkInsert 시간: " + (endStorerInsert - startStoreInsert) / 1000.0 + "초");

//        RequestFormBulkInsert requestFormBulkInsert = new RequestFormBulkInsert();
//        System.out.println("requestFormBulkInsert 시작");
//        long startRequestFormInsert = System.currentTimeMillis();
//        requestFormBulkInsert.bulkInsert();
//        long endRequestFormInsert = System.currentTimeMillis();
//        System.out.println("requestFormBulkInsert 시간: " + (endRequestFormInsert - startRequestFormInsert) / 1000.0 + "초");
//
//        ProposalFormBulkInsert proposalFormBulkInsert = new ProposalFormBulkInsert();
//        System.out.println("proposalFormBulkInsert 시작");
//        long startProposalFormBulkInsert = System.currentTimeMillis();
//        proposalFormBulkInsert.bulkInsert();
//        long endProposalFormBulkInsert = System.currentTimeMillis();
//        System.out.println("proposalFormBulkInsert 시간: " + (endProposalFormBulkInsert - startProposalFormBulkInsert) / 1000.0 + "초");


    }
}
