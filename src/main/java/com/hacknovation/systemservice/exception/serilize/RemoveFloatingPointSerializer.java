package com.hacknovation.systemservice.exception.serilize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author Sombath
 * create at 3/10/22 10:22 AM
 */

public class RemoveFloatingPointSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator generator, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        // put your desired money style here
        generator.writeString(value.toBigInteger().toString());
    }
}