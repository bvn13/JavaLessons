package me.bvn13.test.spring.validation.atleastonegroup.web;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ApiResponseDto<T> {

    T response;
    String status;

}
