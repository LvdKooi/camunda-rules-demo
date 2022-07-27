package nl.kooi.camundarules.api;

import lombok.RequiredArgsConstructor;
import nl.kooi.camundarules.api.dto.ClassificationOutcome;
import nl.kooi.camundarules.api.dto.CustomerLabelDto;
import nl.kooi.camundarules.domain.CustomerLabelService;
import nl.kooi.camundarules.domain.CustomerTopLabelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/classification-outcome")
public class CustomerClassificationController {

    private final CustomerLabelService customerLabelService;
    private final CustomerTopLabelService customerTopLabelService;


    @GetMapping
    public List<ClassificationOutcome> getClassificationOutcomes() {
        var labelsPerCustomerId = customerLabelService.getCustomerLabels().stream().map(Mapper::map).sorted(Comparator.comparingLong(CustomerLabelDto::getCustomerId)).collect(Collectors.groupingBy(CustomerLabelDto::getCustomerId));


        return labelsPerCustomerId.keySet()
                .stream()
                .map(customerId -> new ClassificationOutcome(labelsPerCustomerId.get(customerId), Mapper.map(customerTopLabelService.getCustomerTopLabel(customerId))))
                .collect(Collectors.toList());
    }

    @PostMapping("/clear-all")
    public void clearAllClassifications() {
        customerLabelService.clearAll();
        customerTopLabelService.clearAll();
    }
}
