package com.n26.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Setter
@Getter
public class StatisticsDto {

    public StatisticsDto () {
        sum = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_UP);
        avg = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_UP);
        max = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_UP);
        min = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_UP);
        count = 0;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal sum;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal avg;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal max;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal min;

    private long count;
}
