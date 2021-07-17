package com.n26.service;

import com.n26.dto.StatisticsDto;
import com.n26.dto.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Statistics Service class.
 */
@Service
public class StaticticService {

    private static final org.slf4j.Logger LOGGER = (Logger) LoggerFactory.getLogger(StaticticService.class);
    private static final Integer TR_OLD_VALUE = 60;

    @Autowired
    private TransactionService transactionService;

    /**
     * Get statistics
     * @return
     */
    public StatisticsDto getStatistics () {
        StatisticsDto statistics = new StatisticsDto();
        try {
            return countStat(transactionService.getRuntimeBeans());
        } catch (IndexOutOfBoundsException e) {
            return statistics;
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return null;
        }
    }

    /**
     * Count statistics
     * @param transactions
     * @return
     */
    private StatisticsDto countStat (List<TransactionDto> transactions) {
        StatisticsDto stat = new StatisticsDto();
        BigDecimal sum = BigDecimal.valueOf(0);
        BigDecimal max = BigDecimal.valueOf(Double.MIN_VALUE);
        BigDecimal min = transactions.get(0).getAmount();
        Integer count = 0;
        for(TransactionDto listElement  : transactions){
            if (Math.abs(
                    ChronoUnit.SECONDS.between(
                        LocalDateTime.now().toInstant(OffsetDateTime.now().getOffset()),
                        listElement.getTimestamp())) < TR_OLD_VALUE ) {
                sum = sum.add(listElement.getAmount().setScale(BigDecimal.ROUND_HALF_UP));
                count++;
                if(max.compareTo(listElement.getAmount()) == -1) {
                    max = listElement.getAmount();
                    continue;
                }
                if(min.compareTo(listElement.getAmount()) == 1)
                    min = listElement.getAmount();
            }
        }
        stat.setCount(count);
        stat.setSum(sum.setScale(2, BigDecimal.ROUND_HALF_UP));
        stat.setAvg(sum.divide(BigDecimal.valueOf(count), BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
        stat.setMax(max.setScale(2, BigDecimal.ROUND_HALF_UP));
        stat.setMin(min.setScale(2, BigDecimal.ROUND_HALF_UP));
        return stat;
    }

}
