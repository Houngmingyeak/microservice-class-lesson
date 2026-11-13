package kh.edu.istad.iam.feature.permission;

public record PermissionFilterQuery(
        int page,
        int size
) {
    public PermissionFilterQuery {
        if (page < 0) page = 0;
        if (size <= 0) size = 20;
    }
}

