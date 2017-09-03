package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.tables.records.ReceiptsRecord;

import java.math.BigDecimal;

public class TagResponse {

    @JsonProperty
    Integer id;

    @JsonProperty
    String merchant;

    @JsonProperty
    BigDecimal amount;

    public TagResponse(ReceiptsRecord dbRecord) {
        this.merchant = dbRecord.getMerchant();
        this.amount = dbRecord.getAmount();
        this.id = dbRecord.getId();
    }
}
