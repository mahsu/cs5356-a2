package dao;

import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.TAGS;

public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public boolean exists(Integer receiptId, String tag) {
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(TAGS)
                        .where(TAGS.RECEIPT_ID.eq(receiptId), TAGS.TAG_NAME.eq(tag))
        );
    }

    public void toggleTag(Integer receiptId, String tagName) {
        if (exists(receiptId, tagName)) {
            dsl.deleteFrom(TAGS)
                    .where(TAGS.RECEIPT_ID.eq(receiptId), TAGS.TAG_NAME.eq(tagName))
                    .execute();
        } else {
            dsl
                    .insertInto(TAGS, TAGS.RECEIPT_ID, TAGS.TAG_NAME)
                    .values(receiptId, tagName)
                    .execute();
        }
    }

}
