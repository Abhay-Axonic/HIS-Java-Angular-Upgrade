package com.param.global.common;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;

@Converter(autoApply = true)
public class InstantConverterAppoVital  implements AttributeConverter<Instant, Timestamp>{
    @Override
    public Timestamp convertToDatabaseColumn(Instant attribute) {
        if (attribute == null) {
            return null;
        }
        // Convert Instant to Timestamp
        return Timestamp.from(attribute);
    }

    @Override
    public Instant convertToEntityAttribute(Timestamp dbData) {
        if (dbData == null) {
            return null;
        }
        // Convert Timestamp back to Instant
        return dbData.toInstant();
    }
}
