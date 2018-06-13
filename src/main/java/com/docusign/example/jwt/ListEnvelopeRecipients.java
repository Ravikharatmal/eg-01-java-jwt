package com.docusign.example.jwt;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Recipients;

import java.io.IOException;

/**
 * This class demonstrate list recipients API call with accountId and envelopeId
 */
public class ListEnvelopeRecipients extends ExampleBase {

    public ListEnvelopeRecipients(ApiClient apiClient) throws IOException {
        super(apiClient);
    }

    /**
     * This method demonstrate list recipients api call.
     *
     * @param envelopeId - envelope id returned by SendEnvelope.sendEnvelope(...) class
     * @return - envelope recipient(s)
     * @throws ApiException
     */
    public Recipients list(String envelopeId) throws ApiException, IOException {
        if(envelopeId == null)
            throw new IllegalArgumentException("PROBLEM: This example software doesn't know which envelope's " +
                    "information should be looked up.\n" +
                    "SOLUTION: First run the <b>Send Envelope via email</b> example to create an envelope.");

        this.validateToken();

        EnvelopesApi envelopeApi = new EnvelopesApi(this.apiClient);

        return envelopeApi.listRecipients(this.accountID, envelopeId);
    }
}
