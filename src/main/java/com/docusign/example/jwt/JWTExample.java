package com.docusign.example.jwt;

import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Envelope;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EnvelopesInformation;
import com.docusign.esign.model.Recipients;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.docusign.example.jwt.DSHelper.printPrettyJSON;

public class JWTExample {

    private static final ApiClient apiClient = new ApiClient();

    public static void main(String args[]) {
        try {
            System.setProperty("https.protocols","TLSv1.2");
            System.out.println("\nSending and envelope...");
            EnvelopeSummary result = new SendEnvelope(apiClient).sendEnvelope();
            System.out.println(
                    String.format("Envelope status: %s. Envelope ID: %s",
                    result.getStatus(),
                    result.getEnvelopeId()));

            System.out.println("\nList envelopes in the account...");
            EnvelopesInformation envelopesList = new ListEnvelopes(apiClient).list();

            List<Envelope> envelopes = envelopesList.getEnvelopes();

            if(envelopesList != null && envelopes.size() > 2) {
                System.out.println(
                        String.format("Results for %d envelopes were returned. Showing the first two:",
                        envelopesList.getEnvelopes().size()));

                envelopesList.setEnvelopes(Arrays.asList(envelopes.get(0), envelopes.get(1)));
            }

            String firstEnvelopeId= null;
            // Save an envelopeId for later use if an envelope list was returned (result set could be empty)
            if(envelopes != null && envelopes.get(0) != null){
                firstEnvelopeId = envelopes.get(0).getEnvelopeId();
            }

            printPrettyJSON(envelopesList);

            System.out.println("\nGet an envelope's status...");
            Envelope envelope = new GetEnvelopeStatus(apiClient).getEnvelope(firstEnvelopeId);
            printPrettyJSON(envelope);

            System.out.println ("\nList an envelope's recipients and their status...");
            Recipients recipients = new ListEnvelopeRecipients(apiClient).list(firstEnvelopeId);
            printPrettyJSON(recipients);

            System.out.println("\nDownload an envelope's document(s)...");
            new GetEnvelopeDocuments(apiClient).download(firstEnvelopeId);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
