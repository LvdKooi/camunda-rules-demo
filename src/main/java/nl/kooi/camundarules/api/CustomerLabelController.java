package nl.kooi.camundarules.api;

import lombok.RequiredArgsConstructor;
import nl.kooi.camundarules.api.dto.CustomerLabelDto;
import nl.kooi.camundarules.domain.CustomerLabelService;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customer-labels")
public class CustomerLabelController {

    private final CustomerLabelService service;

    @PostMapping
    public CustomerLabelDto saveCustomerLabel(@RequestBody CustomerLabelDto dto) {
        return Optional.of(dto)
                .map(Mapper::map)
                .map(service::saveCustomerLabel)
                .map(Mapper::map)
                .orElseThrow();
    }

    @PostMapping("/bulk")
    public List<CustomerLabelDto> saveCustomerLabels(@RequestBody List<CustomerLabelDto> dtoList) {
        return dtoList.stream()
                .map(Mapper::map)
                .map(service::saveCustomerLabel)
                .map(Mapper::map)
                .toList();
    }

    @GetMapping
    public List<CustomerLabelDto> getCustomerLabels() {
        return service
                .getCustomerLabels()
                .stream()
                .map(Mapper::map)
                .sorted(Comparator.comparingLong(CustomerLabelDto::getCustomerId))
                .toList();
    }
}
