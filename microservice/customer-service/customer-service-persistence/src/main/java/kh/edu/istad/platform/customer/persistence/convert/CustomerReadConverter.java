package kh.edu.istad.platform.customer.persistence.convert;

import kh.edu.istad.common.domain.valueobject.Customerid;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;


import java.util.UUID;

@ReadingConverter
public class CustomerReadConverter implements Converter<Document, Customerid> {

    @Override
    public Customerid convert(Document source) {
        return new Customerid(UUID.fromString(source.getString("_id")));
    }
}
