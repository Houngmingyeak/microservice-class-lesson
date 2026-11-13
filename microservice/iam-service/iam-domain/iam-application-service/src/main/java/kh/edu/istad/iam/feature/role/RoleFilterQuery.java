package kh.edu.istad.iam.feature.role;

public record RoleFilterQuery(
        int page,
        int size
) {
    public RoleFilterQuery {
        if (page < 0) page = 0;
        if (size <= 0) size = 20;
    }
}

