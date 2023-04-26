package dev.mateusneres.pixpayview.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SettingsRequest {

    @NotBlank
    private String paymentToken;

}
