package nl.kooi.camundarules.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetLabelsPerCustomerGroupDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("Obtaining customer groups.");
        delegateExecution.setVariable("labels", List.of("label_1", "label_3"));
        delegateExecution.setVariable("isThrottled", false);
    }
}
