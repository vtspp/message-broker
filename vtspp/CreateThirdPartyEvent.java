package br.vtspp;

import br.vtspp.ThirdPartyAdvanced;
import br.vtspp.RiskDto;

public record CreateThirdPartyEvent (ThirdPartyAdvanced thirdPartyCreateRequest, RiskDto risk) implements Event {
}
