package com.n26.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeParseException;

/**
 * TransactionDto for controllers.
 */
@Getter
@Setter
@Component
@NoArgsConstructor
public class TransactionDto {

    public TransactionDto(JsonNode body) throws DateTimeParseException {
        this.amount = new BigDecimal(body.get("amount").asText());
        this.timestamp = Instant.parse(body.get("timestamp").asText());
    }

    /**
     * Timestamp of a transaction
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",timezone = "UTC")
    @JsonProperty("timestamp")
    private Instant timestamp;


    /**
     * Amount of transactions
     */
    @JsonSerialize(using = BigDecimalSerialize.class)
    @JsonProperty("amount")
    private BigDecimal amount;


    /**
     * Serializer for BigDecimal
     */
    public class BigDecimalSerialize extends JsonSerializer<BigDecimal> {
        @Override
        public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
            gen.writeString(value.setScale(2, BigDecimal.ROUND_HALF_DOWN) + "");
        }
    }

}
