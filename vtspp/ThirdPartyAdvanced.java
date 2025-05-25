package br.vtspp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Data
public class ThirdPartyAdvanced implements Serializable {
    private Long id;
    private Map<String, Object> basicData;
    private Map<String, Object> bankAccountData;
    private Map<String, Object> addressData;
    private List<?> roleData;
    private List<?> contactBook;
    private List<?> partner;
}
