package br.vtspp;

import org.springframework.util.SerializationUtils;

import java.io.Serializable;

public interface Event extends Serializable {

    default byte[] serialize() {
        return SerializationUtils.serialize(this);
    }

    static Event deserialize(byte[] data) {
        return (Event) SerializationUtils.deserialize(data);
    }
}
