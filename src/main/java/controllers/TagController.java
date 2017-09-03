package controllers;

import dao.ReceiptDao;
import dao.TagDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {

    private ReceiptDao receiptDao;
    private TagDao tagDao;

    public TagController(ReceiptDao r, TagDao t) {
        receiptDao = r;
        tagDao = t;
    }
    @PUT
    @Path("/tags/{tag}")
    public void toggleTag(@PathParam("tag") String tagName, Integer receiptId) {

        if (!receiptDao.exists(receiptId)) {
            return;
        }

        tagDao.toggleTag(receiptId, tagName);


        //return Response.status(Response.Status.CONFLICT).build();
    }
}
