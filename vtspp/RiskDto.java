package br.vtspp;

import lombok.Builder;

import java.io.Serializable;
import java.util.Date;

@Builder
public record RiskDto(
        Long idrisk,
        Long chargeFileIdcharge,
        Long tipoRegistro,
        String codRegistro,
        String codRiscoInt,
        String tipoOperacao,
        String codProduto,
        String codTitularApoliceInt,
        String codRespPagamentoInt,
        String codAcordoInt,
        String codSubAcordoInt,
        String numBilhete,
        Date datInicioVigencia,
        Date datFimVigencia,
        Date datCancelamento,
        String tipoPagamento,
        String bancoPagamento,
        String agenciaPagamento,
        String contaPagamento,
        String nroProposta,
        Date datProposta,
        String tipoPlanoFinanciamento,
        String nroApoliceMigracao,
        Integer acseleStatus,
        String emissaoSemCorretor,
        String indParceria,
        String desParceria,
        String policyNumberMigration) implements Serializable {
}
