package sept.major.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;

@Converter
public class StringToDateConverter implements AttributeConverter<String, Date>{

    @Override
    public Date convertToDatabaseColumn(String s) {
        return Date.valueOf(s);
    }

    @Override
    public String convertToEntityAttribute(Date date) {
        return date.toString();
    }
}
