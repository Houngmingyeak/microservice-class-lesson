package kh.edu.istad.platform.customer.persistence.query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public final class DynamicQuery {

    private final List<Criteria> conditions = new ArrayList<>();

    private DynamicQuery() {}

    public static DynamicQuery create() {
        return new DynamicQuery();
    }

    /** field == value (ignored if value is null) */
    public DynamicQuery eq(String field, Object value) {
        if (value != null) {
            conditions.add(Criteria.where(field).is(value));
        }
        return this;
    }

    /** field contains text, case-insensitive (ignored if blank) */
    public DynamicQuery contains(String field, String value) {
        if (hasText(value)) {
            conditions.add(Criteria.where(field).regex(Pattern.quote(value.trim()), "i"));
        }
        return this;
    }

    /** any of the fields contains text (keyword search) */
    public DynamicQuery anyContains(String value, String... fields) {
        if (hasText(value) && fields.length > 0) {
            String regex = Pattern.quote(value.trim());
            Criteria[] ors = new Criteria[fields.length];
            for (int i = 0; i < fields.length; i++) {
                ors[i] = Criteria.where(fields[i]).regex(regex, "i");
            }
            conditions.add(new Criteria().orOperator(ors));
        }
        return this;
    }

    /** field in (values) (ignored if null or empty) */
    public DynamicQuery in(String field, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            conditions.add(Criteria.where(field).in(values));
        }
        return this;
    }

    /** field >= value */
    public DynamicQuery from(String field, Object value) {
        if (value != null) {
            conditions.add(Criteria.where(field).gte(value));
        }
        return this;
    }

    /** field <= value */
    public DynamicQuery to(String field, Object value) {
        if (value != null) {
            conditions.add(Criteria.where(field).lte(value));
        }
        return this;
    }

    /** Query without paging (use for count) */
    public Query build() {
        Query query = new Query();
        if (!conditions.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(conditions.toArray(new Criteria[0])));
        }
        return query;
    }

    /** Query with skip, limit and sort */
    public Query build(Pageable pageable) {
        return build().with(pageable);
    }

    private static boolean hasText(String s) {
        return s != null && !s.isBlank();
    }
}