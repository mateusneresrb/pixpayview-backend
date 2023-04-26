package dev.mateusneres.pixpayview.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PayerRequest {

    private String entity_type;
    private String type;
    private String email;

}
