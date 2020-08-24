package sept.major.hours;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringToIntegerConverter implements AttributeConverter<String, Integer> {

    @Override
    public Integer convertToDatabaseColumn(String s) {
        return Integer.parseInt(s);
    }

    @Override
    public String convertToEntityAttribute(Integer integer) {
        return integer.toString();
    }
}
