package nl.kooi.camundarules.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.camundarules.domain.CustomerLabelService;
import nl.kooi.camundarules.domain.CustomerTopLabelService;
import nl.kooi.camundarules.enums.Label;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SaveTopLevelLabelDelegate implements JavaDelegate {

    private final CustomerTopLabelService customerTopLabelService;
    private final CustomerLabelService customerLabelService;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        var label = delegateExecution.getVariable("label");
        var customerId = delegateExecution.getVariable("customerId");
        var labelAsLabel = label != null ? Label.valueOf(((String) label)) : null;

        log.info("Determine top level label {} for customer {}", label, customerId);
        customerTopLabelService.saveCustomerTopLabel((Long) customerId, labelAsLabel);

        var isThrottled = customerLabelService.isLabelThrottledForCustomer((Long) customerId, labelAsLabel);

        delegateExecution.setVariable("isThrottled", isThrottled);
    }
}
