package nl.kooi.camundarules.api.dto;

import lombok.Data;
import nl.kooi.camundarules.enums.Label;

@Data
public class CustomerTopLabelDto {
    private Long id;
    private Long customerId;
    private Label label;
    private String outputStrategy;
}
