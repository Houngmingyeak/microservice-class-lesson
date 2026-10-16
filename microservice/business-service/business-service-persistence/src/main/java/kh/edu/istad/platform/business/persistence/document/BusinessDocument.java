package kh.edu.istad.platform.business.persistence.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "businesses")
public class BusinessDocument {

    // ids are stored as text, so no UUID representation setting is needed in MongoDB
    @Id
    private String businessId;

    private String legalName;
    private String displayName;
    private String email;
    private String phoneNumber;
    private boolean active;
    private List<ProductDocument> products = new ArrayList<>();
}
