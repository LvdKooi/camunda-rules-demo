package nl.kooi.camundarules.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClassificationOutcome {
    private final List<CustomerLabelDto> input;
    private final CustomerTopLabelDto outcome;
}
