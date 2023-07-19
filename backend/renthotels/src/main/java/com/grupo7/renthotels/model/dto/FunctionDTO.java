package com.grupo7.renthotels.model.dto;

import com.grupo7.renthotels.model.Function;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FunctionDTO {

    private Long functionSku;

    private String functionName;

    public static FunctionDTO from(Function function) {
        FunctionDTO functionDTO = new FunctionDTO();
        functionDTO.setFunctionSku(function.getFunctionSku());
        functionDTO.setFunctionName(function.getName());
        return functionDTO;
    }

    public Function toEntity() {
        Function function = new Function();
        function.setFunctionSku(this.functionSku);
        function.setName(this.functionName);
        return function;
    }

}
