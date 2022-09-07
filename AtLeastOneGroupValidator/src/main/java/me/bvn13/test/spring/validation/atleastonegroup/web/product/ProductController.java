package me.bvn13.test.spring.validation.atleastonegroup.web.product;

import me.bvn13.test.spring.validation.atleastonegroup.web.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ProductController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/api/products")
    public ApiResponseDto<CreateProductResponseDto> createProduct(@Valid @RequestBody CreateProductRequestDto requestDto) {
        return ApiResponseDto.<CreateProductResponseDto>builder()
                .status("OK")
                .response(CreateProductResponseDto.builder()
                        .message(String.format("Product created with '%s'", detectIncomeParam(requestDto)))
                        .build())
                .build();
    }

    private String detectIncomeParam(CreateProductRequestDto requestDto) {
        if (requestDto.getProductId() != null) {
            return requestDto.getProductId().toString();
        } else if (requestDto.getProductCode() != null) {
            return requestDto.getProductCode();
        } else {
            return requestDto.getProductIdHash();
        }
    }

}
