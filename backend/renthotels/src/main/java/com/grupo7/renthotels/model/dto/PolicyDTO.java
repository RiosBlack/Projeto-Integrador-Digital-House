package com.grupo7.renthotels.model.dto;

import com.grupo7.renthotels.model.Policy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDTO {
    private Long policySku;
    private String policyHotel1;
    private String policyHotel2;
    private String policyHotel3;

    public static PolicyDTO from(Policy policy){
        return new PolicyDTO(
                policy.getPolicySku(),
                policy.getPolicyHotel1(),
                policy.getPolicyHotel2(),
                policy.getPolicyHotel3()
        );
    }

    public Policy toEntity(){
        Policy policy = new Policy();
        policy.setPolicySku(this.policySku);
        policy.setPolicyHotel1(this.policyHotel1);
        policy.setPolicyHotel2(this.policyHotel2);
        policy.setPolicyHotel3(this.policyHotel3);
        return policy;
    }
}
