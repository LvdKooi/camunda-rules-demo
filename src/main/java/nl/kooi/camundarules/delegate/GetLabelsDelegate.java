package nl.kooi.camundarules.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.camundarules.domain.CustomerLabelService;
import nl.kooi.camundarules.enums.Label;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class GetLabelsDelegate implements JavaDelegate {

    private final CustomerLabelService service;
    private final RuntimeService camundaRuntime;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        var customerId = delegateExecution.getVariable("customerId");
        var labels = service.getLabelsForCustomer((Long) customerId);
        var labelsAsString = labels.stream().map(Label::toString).toList();

        log.info("Received labels: {} for customer: {}", labels, customerId);

        delegateExecution.setVariable("labels", labelsAsString);

        var procesVariables = Map.of("variable1", (Object) UUID.randomUUID(), "variable2", (Object) " HELLO!");

        sendMessage(procesVariables);
    }

    private void sendMessage(Map<String, Object> processVariables) {
        camundaRuntime.startProcessInstanceByMessage("Message_start", processVariables);
    }
}
