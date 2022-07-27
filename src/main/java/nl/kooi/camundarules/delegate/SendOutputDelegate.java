package nl.kooi.camundarules.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class SendOutputDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        var outputOutcome = delegateExecution.getVariable("output_strategy");

        System.out.println(outputOutcome);
    }
}
