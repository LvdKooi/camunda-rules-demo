package nl.kooi.camundarules.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationOutcome {
    private List<CustomerLabelDto> input;
    private CustomerTopLabelDto outcome;
}
