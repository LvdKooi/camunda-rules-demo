package nl.kooi.camundarules.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.camundarules.api.dto.ClassificationOutcome;
import nl.kooi.camundarules.api.dto.CustomerLabelDto;
import nl.kooi.camundarules.domain.CustomerLabelService;
import nl.kooi.camundarules.domain.CustomerTopLabelService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Slf4j
public class CustomerClassificationController {

    private final CustomerLabelService customerLabelService;
    private final CustomerTopLabelService customerTopLabelService;
    private final RuntimeService camundaRuntime;


    @GetMapping("/classification-outcome")
    public List<ClassificationOutcome> getClassificationOutcomes() {
        var labelsPerCustomerId = customerLabelService
                .getCustomerLabels()
                .stream()
                .map(Mapper::map)
                .collect(Collectors.groupingBy(CustomerLabelDto::getCustomerId));

        return labelsPerCustomerId.keySet()
                .stream()
                .map(customerId ->
                        new ClassificationOutcome(labelsPerCustomerId.get(customerId), Mapper.map(customerTopLabelService.getCustomerTopLabel(customerId))))
                .collect(Collectors.toList());
    }

    @PostMapping("/classification-outcome/clear-all")
    public void clearAllClassifications() {
        customerLabelService.clearAll();
        customerTopLabelService.clearAll();
    }

    @PostMapping("/start-classification")
    public void startClassificationProcess() {
        log.info("Clearing previous outcome.");
        customerTopLabelService.clearAll();
        var process = camundaRuntime.startProcessInstanceByKey("classification_process");
        log.info("Process started with id: {}", process.getId());
    }
}
