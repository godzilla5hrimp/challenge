package com.n26.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.n26.dto.TransactionDto;
import com.n26.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

/**
 * REST Controller for all transactions related
 */
@Controller(value = "/transactions")
@Validated
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Method to add transaction.
     * @param transaction
     * @return
     */
    @PostMapping(produces= MediaType.APPLICATION_JSON_VALUE, headers = {"Content-type=application/json"})
    public ResponseEntity<?> postTransactions (@Valid @RequestBody TransactionDto transaction) {
        try {
            switch (transactionService.createTransaction(transaction, LocalDateTime.now().toInstant(OffsetDateTime.now().getOffset()))) {
                case TR_INV:
                    return new ResponseEntity<String>(HttpStatus.UNPROCESSABLE_ENTITY);
                case TR_OLD:
                    return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
                default:
                    return new ResponseEntity<String>(HttpStatus.CREATED);
            }
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


    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<String> handleConstraintViolationException(InvalidFormatException e) {
        return new ResponseEntity<>( HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<String> handleConstraintViolationException(DateTimeParseException e) {
        return new ResponseEntity<>( HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
