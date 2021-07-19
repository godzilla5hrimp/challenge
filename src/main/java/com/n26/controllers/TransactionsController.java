package com.n26.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.n26.dto.TransactionDto;
import com.n26.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

/**
 * REST Controller for all transactions related
 */
@Controller(value = "/transactions")
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Method to add transaction.
     * @param transaction
     * @return
     */
    @PostMapping(produces= MediaType.APPLICATION_JSON_VALUE, headers = {"Content-type=application/json"})
    public ResponseEntity<?> postTransactions (@RequestBody JsonNode transaction) {
        try {
            TransactionDto transac = new TransactionDto(transaction);
            switch (transactionService.createTransaction(transac, LocalDateTime.now().toInstant(OffsetDateTime.now().getOffset()))) {
                case TR_INV:
                    System.out.println("");
                    return new ResponseEntity<String>(HttpStatus.UNPROCESSABLE_ENTITY);
                case TR_OLD:
                    return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
                default:
                    return new ResponseEntity<String>(HttpStatus.CREATED);
            }
       } catch (DateTimeParseException e) {
            return new ResponseEntity<String>(HttpStatus.UNPROCESSABLE_ENTITY);
       } catch (NumberFormatException e) {
            return new ResponseEntity<String>(HttpStatus.UNPROCESSABLE_ENTITY);
       } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
       }
    }

    /**
     * Method to delete all transactions stored
     * @return
     */
    @DeleteMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTransactions () {
      transactionService.deleteAllTransactions();
      return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }


//    @ExceptionHandler({ CustomException1.class, CustomException2.class })
//    public void handleException() {
//        //
//    }

}
