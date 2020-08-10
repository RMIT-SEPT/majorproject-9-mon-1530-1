package sept.major.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;

@Converter
public class StringToTimestampConverter implements AttributeConverter<String, Timestamp>{
    @Override
    public Timestamp convertToDatabaseColumn(String s) {
        return Timestamp.valueOf(s);
    }

    @Override
    public String convertToEntityAttribute(Timestamp timestamp) {
        return timestamp.toString();
    }
}
