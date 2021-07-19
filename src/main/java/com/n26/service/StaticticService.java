package com.n26.service;

import com.n26.dto.StatisticsDto;
import com.n26.dto.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
        BigDecimal sum = BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal max = BigDecimal.valueOf(Double.MIN_VALUE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal min = transactions.get(0).getAmount().setScale(2, RoundingMode.HALF_UP);
        Integer count = 0;
        for(TransactionDto listElement  : transactions){
            if (Math.abs(
                    ChronoUnit.SECONDS.between(
                        LocalDateTime.now().toInstant(OffsetDateTime.now().getOffset()),
                        listElement.getTimestamp())) < TR_OLD_VALUE ) {
                sum = sum.add(listElement.getAmount());
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
        stat.setSum(sum);
        stat.setAvg(sum.divide(BigDecimal.valueOf(count),2, RoundingMode.HALF_UP));
        stat.setMax(max.round(new MathContext(MathContext.UNLIMITED.getPrecision(), RoundingMode.HALF_UP)).setScale(2));
        stat.setMin(min.round(new MathContext(MathContext.UNLIMITED.getPrecision(), RoundingMode.HALF_UP)).setScale(2));
        return stat;
    }

}
