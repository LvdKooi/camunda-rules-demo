package nl.kooi.camundarules.api;

import lombok.RequiredArgsConstructor;
import nl.kooi.camundarules.persistence.CustomerLabel;
import nl.kooi.camundarules.api.dto.CustomerLabelDto;
import nl.kooi.camundarules.domain.CustomerLabelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customer-labels")
public class CustomerLabelController {

    private final CustomerLabelService service;

    @PostMapping
    public CustomerLabelDto saveCustomerLabel(@RequestBody CustomerLabelDto dto) {
        var customer = service.saveCustomerLabel(dto.getCustomerId(), dto.getLabel());
        return map(customer);
    }

    @GetMapping
    public List<CustomerLabelDto> getCustomerLabels() {
        return service.getCustomerLabels().stream().map(CustomerLabelController::map).collect(Collectors.toList());
    }

    private static CustomerLabelDto map(CustomerLabel customer) {
        var dto = new CustomerLabelDto();
        dto.setCustomerId(customer.getCustomerId());
        dto.setId(customer.getId());
        dto.setLabel(customer.getLabel());
        return dto;
    }
}
