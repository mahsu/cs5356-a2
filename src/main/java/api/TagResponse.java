package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.tables.records.ReceiptsRecord;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TagResponse {

    @NotNull
    @JsonProperty
    Integer id;

    @NotEmpty
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
