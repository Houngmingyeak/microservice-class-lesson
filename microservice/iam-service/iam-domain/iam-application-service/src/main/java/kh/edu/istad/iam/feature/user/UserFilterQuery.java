package kh.edu.istad.iam.feature.user;

public record UserFilterQuery(
        int page,
        int size,
        String sortBy,
        String direction
) {
    public UserFilterQuery {
        if (page < 0) page = 0;
        if (size <= 0) size = 20;
        if (sortBy == null || sortBy.isBlank()) sortBy = "id";
        if (direction == null || direction.isBlank()) direction = "asc";
    }
}

