package br.com.vtspp;

public record ErrorEvent (Throwable cause) implements Event {
}
