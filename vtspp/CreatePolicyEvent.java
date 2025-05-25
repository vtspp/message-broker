package br.vtspp;

public record CreatePolicyEvent (RiskDto risk) implements Event {
}
