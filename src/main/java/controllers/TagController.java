package controllers;

import api.TagResponse;
import dao.ReceiptDao;
import dao.TagDao;
import generated.tables.records.ReceiptsRecord;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {

    private ReceiptDao receipts;
    private TagDao tags;

    public TagController(ReceiptDao r, TagDao t) {
        receipts = r;
        tags = t;
    }

    @PUT
    @Path("/tags/{tag}")
    public void toggleTag(@PathParam("tag") String tagName, Integer receiptId) {

        if (!receipts.exists(receiptId)) {
            return;
        }

        tags.toggleTag(receiptId, tagName);
        //return Response.status(Response.Status.CONFLICT).build();
    }

    @GET
    @Path("/tags/{tag}")
    public List<TagResponse> getReceiptsByTag(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> receiptRecords = tags.getReceiptsByTag(tagName);
        return receiptRecords.stream().map(TagResponse::new).collect(toList());
    }
}
