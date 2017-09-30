package controllers;

import api.ReceiptSuggestionResponse;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.math.BigDecimal;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotEmpty;

import static java.lang.System.out;

@Path("/images")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptImageController {
    private final AnnotateImageRequest.Builder requestBuilder;

    public ReceiptImageController() {
        // DOCUMENT_TEXT_DETECTION is not the best or only OCR method available
        Feature ocrFeature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        this.requestBuilder = AnnotateImageRequest.newBuilder().addFeatures(ocrFeature);

    }

    /**
     * This borrows heavily from the Google Vision API Docs.  See:
     * https://cloud.google.com/vision/docs/detecting-fulltext
     * <p>
     * YOU SHOULD MODIFY THIS METHOD TO RETURN A ReceiptSuggestionResponse:
     * <p>
     * public class ReceiptSuggestionResponse {
     * String merchantName;
     * String amount;
     * }
     */
    @POST
    public ReceiptSuggestionResponse parseReceipt(@NotEmpty String base64EncodedImage) throws Exception {
        Image img = Image.newBuilder().setContent(ByteString.copyFrom(Base64.getDecoder().decode(base64EncodedImage))).build();
        AnnotateImageRequest request = this.requestBuilder.setImage(img).build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse responses = client.batchAnnotateImages(Collections.singletonList(request));
            AnnotateImageResponse res = responses.getResponses(0);

            String merchantName = null;
            BigDecimal amount = null;

            // Sort text annotations by bounding polygon.  Top-most non-decimal text is the merchant
            // bottom-most decimal text is the total amount
            List<EntityAnnotation> textAnnotations = new ArrayList<>(res.getTextAnnotationsList());

            Collections.sort(textAnnotations, new TopBottomComparator());

            for (EntityAnnotation annotation : textAnnotations) {
                out.printf("Position : %s\n", annotation.getBoundingPoly());
                out.printf("Text: %s\n", annotation.getDescription());
            }

            EntityAnnotation fullReceipt = null;

            //get the top-most non-decimal string
            for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                String merchant = annotation.getDescription();
                //ignore the full text summary, which contains line breaks
                if (merchant.contains("\n")) {
                    fullReceipt = annotation;
                    continue;
                }
                try {
                    new BigDecimal(merchant);
                } catch (NumberFormatException e) {
                    merchantName = merchant;
                    break;
                }
            }

            if (fullReceipt != null) {
                //use captured as the full receipt bounds
            }

            //get the bottom-most decimal text
            for (int i = textAnnotations.size() - 1; i >= 0; i--) {
                try {
                    String amt = textAnnotations.get(i).getDescription();
                    if (amt.charAt(0) == '$') {
                        amt = amt.substring(1);
                    }

                    amount = new BigDecimal(amt);
                    break;
                } catch (NumberFormatException e) {
                }
            }

            //TextAnnotation fullTextAnnotation = res.getFullTextAnnotation();
            return new ReceiptSuggestionResponse(merchantName, amount);
        }
    }

    public static class TopBottomComparator implements Comparator<EntityAnnotation> {

        @Override
        public int compare(EntityAnnotation a, EntityAnnotation b) {
            Vertex va = a.getBoundingPoly().getVertices(0);
            Vertex vb = b.getBoundingPoly().getVertices(0);
            return va.getY() < vb.getY() ? -1 : va.getY() == vb.getY() ? 0 : 1;
        }
    }
}
