package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * Represents the result of an OCR parse
 */
public class ReceiptSuggestionResponse {
    @JsonProperty
    public final String merchantName;

    @JsonProperty
    public final BigDecimal amount;

    @JsonProperty
    public final Integer xmin;

    @JsonProperty
    public final Integer xmax;

    @JsonProperty
    public final Integer ymin;

    @JsonProperty
    public final Integer ymax;


    public ReceiptSuggestionResponse(String merchantName, BigDecimal amount, Integer xmin, Integer ymin, Integer xmax, Integer ymax) {
        this.merchantName = merchantName;
        this.amount = amount;
        this.xmax = xmax;
        this.ymax = ymax;
        this.xmin = xmin;
        this.ymin = ymin;
    }
}
