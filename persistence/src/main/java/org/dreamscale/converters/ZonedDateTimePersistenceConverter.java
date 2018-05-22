package org.dreamscale.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class ZonedDateTimePersistenceConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime entityValue) {
        if (entityValue != null) {
            return Timestamp.from(entityValue.toInstant());
        }
        return null;
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp databaseValue) {
        if (databaseValue != null) {
            return ZonedDateTime.ofInstant(databaseValue.toInstant(), ZoneId.of("UTC"));
        }
        return null;
    }

}
