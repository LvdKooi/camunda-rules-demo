package nl.kooi.camundarules.api;

import lombok.experimental.UtilityClass;
import nl.kooi.camundarules.api.dto.CustomerLabelDto;
import nl.kooi.camundarules.api.dto.CustomerTopLabelDto;
import nl.kooi.camundarules.persistence.CustomerLabel;
import nl.kooi.camundarules.persistence.CustomerTopLabel;

@UtilityClass
public class Mapper {

    public static CustomerLabelDto map(CustomerLabel model) {
        var dto = new CustomerLabelDto();
        dto.setCustomerId(model.getCustomerId());
        dto.setId(model.getId());
        dto.setLabel(model.getLabel());
        dto.setThrottled(model.isThrottled());
        return dto;
    }

    public static CustomerTopLabelDto map(CustomerTopLabel model) {
        var dto = new CustomerTopLabelDto();
        dto.setCustomerId(model.getCustomerId());
        dto.setId(model.getId());
        dto.setLabel(model.getLabel());
        dto.setOutputStrategy(model.getOutputStrategy());
        return dto;
    }
}
