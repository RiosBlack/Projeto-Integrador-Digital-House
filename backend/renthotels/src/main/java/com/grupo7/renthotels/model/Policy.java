package com.grupo7.renthotels.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Table(name="policies")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_policy", nullable = false, unique = true)
    private Long idPolicy;

    @Column(name = "policy_sku", nullable = false, unique = true)
    private Long policySku;

    @Column(name = "policy_hotel1", nullable = false)
    private String policyHotel1;

    @Column(name = "policy_hotel2")
    private String policyHotel2;

    @Column(name = "policy_hotel3")
    private String policyHotel3;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    public void createPolicySku() {
        long generated = (long) (Math.random() * 9999999L);
        this.policySku = (generated < 0 ? generated * -1 : generated);
    }

    @Builder
    public Policy(String policyHotel1, String policyHotel2, String policyHotel3, Product product){
        this.policyHotel1 = policyHotel1;
        this.policyHotel2 = policyHotel2;
        this.policyHotel3 = policyHotel3;
        this.product = product;
    }
}
