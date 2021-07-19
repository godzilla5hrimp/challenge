package com.n26.service;

import com.n26.TransactionsStates;
import com.n26.dto.TransactionDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Transaction service class.
 */
@Service
@Getter
@NoArgsConstructor
@Scope("singleton")
public class TransactionService {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(TransactionService.class);
    private static final Integer TR_OLD_VALUE = 60;

    private CopyOnWriteArrayList<TransactionDto> runtimeBeans = new CopyOnWriteArrayList<TransactionDto>();

    /**
     * Create transaction
     * @param transactionDto - Transaction
     * @return
     */
    public TransactionsStates createTransaction (TransactionDto  transactionDto, Instant moment) {
       switch (checkTransaction(transactionDto, moment)) {
           case TR_INV:
               return TransactionsStates.TR_INV;
           case TR_OLD:
               return TransactionsStates.TR_OLD;
           default:
               runtimeBeans.add(transactionDto);
               return TransactionsStates.TR_FINE;
       }
    }

    /**
     * Validate transaction
     * @param dto - Transaction
     * @param moment - Moment in which controller got query
     * @return
     */
    private TransactionsStates checkTransaction(TransactionDto dto, Instant moment){
        Duration dur = Duration.between(dto.getTimestamp(), moment);
        if (moment.isBefore(dto.getTimestamp())) {
            LOGGER.error("Great Scott! It's from the future!");
            return TransactionsStates.TR_INV;
        } else if (dur.getSeconds() >= TR_OLD_VALUE){
            LOGGER.warn("This transaction is older than 60 seconds!");
            return TransactionsStates.TR_OLD;
        }
        return TransactionsStates.TR_FINE;
    }

    /**
     * Delete all existing transactions
     */
    public void deleteAllTransactions() {
        runtimeBeans.clear();
        LOGGER.info("All transactions cleared. Current active transactions:" + runtimeBeans.size());
    }

}
