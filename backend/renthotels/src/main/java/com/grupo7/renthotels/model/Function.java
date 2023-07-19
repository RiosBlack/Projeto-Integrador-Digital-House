package com.grupo7.renthotels.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "functions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Function implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_function", nullable = false, unique = true)
    private Long idFunction;

    @Column(name = "function_sku", nullable = false, unique = true)
    private Long functionSku;

    @Column(name = "function_name", length = 75, nullable = false, unique = true)
    private String name;

    public Long createFunctionSku() {
        long generated = (long) (Math.random() * 9999999L);
        return functionSku = (generated <0 ? generated * -1 : generated);
    }


}

