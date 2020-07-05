package com.lim.bookopenapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class BookSearchForm {

    @NotBlank
    private String searchBy;
}
