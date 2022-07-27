package nl.kooi.camundarules.api;

import lombok.RequiredArgsConstructor;
import nl.kooi.camundarules.api.dto.CustomerLabelDto;
import nl.kooi.camundarules.domain.CustomerLabelService;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static nl.kooi.camundarules.api.Mapper.map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customer-labels")
public class CustomerLabelController {

    private final CustomerLabelService service;

    @PostMapping
    public CustomerLabelDto saveCustomerLabel(@RequestBody CustomerLabelDto dto) {
        var customer = service.saveCustomerLabel(dto.getCustomerId(), dto.getLabel(), dto.isThrottled());
        return map(customer);
    }

    @GetMapping
    public List<CustomerLabelDto> getCustomerLabels() {
        return service.getCustomerLabels().stream().map(Mapper::map).sorted(Comparator.comparingLong(CustomerLabelDto::getCustomerId)).collect(Collectors.toList());
    }
}
