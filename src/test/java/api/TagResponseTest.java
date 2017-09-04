package api;


import generated.tables.records.ReceiptsRecord;
import io.dropwizard.jersey.validation.Validators;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Validator;
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class TagResponseTest {

    private final Validator validator = Validators.newValidator();
    private ReceiptsRecord record;

    @Before
    public void before() {
        record = new ReceiptsRecord();
        record.setId(0);
        record.setMerchant("asd");
        record.setAmount(new BigDecimal(23.14));
    }

    @Test
    public void testValid() {
        TagResponse tr = new TagResponse(record);
        assertThat(validator.validate(tr), empty());
    }

    @Test
    public void testMissingId() {
        record.setId(null);
        TagResponse tr = new TagResponse(record);
        assertThat(validator.validate(tr), hasSize(1));
    }

    @Test
    public void testMissingIdMerchant() {
        record.setId(null);
        record.setMerchant(null);
        TagResponse tr = new TagResponse(record);

        assertThat(validator.validate(tr), hasSize(2));
    }

}