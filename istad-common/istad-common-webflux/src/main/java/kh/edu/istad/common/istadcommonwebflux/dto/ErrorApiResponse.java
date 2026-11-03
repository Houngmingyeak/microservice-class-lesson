package kh.edu.istad.common.istadcommonwebflux.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
public record ErrorApiResponse(String message, Integer code, Map<String, Object> details) {
//    private String message;
//    private Integer code;
//    private Object details;
}