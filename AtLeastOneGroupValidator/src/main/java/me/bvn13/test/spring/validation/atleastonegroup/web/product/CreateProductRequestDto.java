package me.bvn13.test.spring.validation.atleastonegroup.web.product;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import me.bvn13.test.spring.validation.atleastonegroup.web.validation.AtLeastOneGroupValidated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@AtLeastOneGroupValidated(
        checkingGroups = {
                CreateProductRequestDto.FirstOption.class,
                CreateProductRequestDto.SecondOption.class,
                CreateProductRequestDto.ThirdOption.class
        }, message = "One of 'productId', 'productCode' or 'productIdHash' must be specified!"
)
@Value
@Builder
@Jacksonized
public class CreateProductRequestDto {

    @NotNull(groups = {
            FirstOption.class
    })
    @Null(groups = {
            SecondOption.class,
            ThirdOption.class
    })
    Long productId;
    @NotNull(groups = {
            SecondOption.class
    })
    @Null(groups = {
            FirstOption.class,
            ThirdOption.class
    })
    String productCode;
    @NotNull(groups = {
            ThirdOption.class
    })
    @Null(groups = {
            FirstOption.class,
            SecondOption.class
    })
    String productIdHash;


    public interface FirstOption {
    }

    public interface SecondOption {
    }

    public interface ThirdOption {
    }
}
