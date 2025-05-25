package br.vtspp;

import br.vtspp.ThirdPartyNotCreateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateThirdPartyEventConsumer implements MessageConsumer<CreateThirdPartyEvent> {

    private final BiConsumer<String, Event> producer;
    private final WebClient thirdPartyWebClient;

    @Override
    public void accept(CreateThirdPartyEvent event) {
        log.info("{} - Processing create third party event idCharge {} request {}", Thread.currentThread().getName(), event.risk().chargeFileIdcharge(), event.thirdPartyCreateRequest());
        thirdPartyWebClient.post()
                .uri("/thirdParties/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(event.thirdPartyCreateRequest())
                .retrieve()
                .bodyToMono(Map.class)
                .subscribe(
                        ok -> producer.accept("createPolicyEvent", new CreatePolicyEvent(event.risk())),
                        throwable -> producer.accept("errorEvent", new ErrorEvent(new ThirdPartyNotCreateException(throwable.getMessage())))
                );
    }

    @Override
    public Event onError(Throwable throwable) {
        log.error(throwable.getMessage());
        return new ErrorEvent(new ThirdPartyNotCreateException(throwable.getMessage()));
    }
}
