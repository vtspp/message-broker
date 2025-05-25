package br.vtspp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreatePolicyEventConsumer implements MessageConsumer<CreatePolicyEvent> {
    
    private final CreatePolicyRecord policyRecord;

    @Override
    public void accept(CreatePolicyEvent createPolicyEvent) {
        log.info("{} - Process create policy event {}", Thread.currentThread().getName(), createPolicyEvent);
        policyRecord.createPolicy(createPolicyEvent.risk());
    }

    @Override
    public Event onError(Throwable throwable) {
        return new ErrorEvent(new CreatePolicyErrorEventException(throwable.getMessage()));
    }
}
