package nl.kooi.camundarules.api.dto;

import lombok.Data;
import nl.kooi.camundarules.enums.Label;

@Data
public class CustomerLabelDto {
    private Long id;
    private Long customerId;
    private Label label;
}
