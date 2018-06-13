package com.docusign.example.jwt;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Envelope;

import java.io.IOException;

/**
 * This class demonstrate get envelope API call by providing accountId and envelopeId.
 */
public class GetEnvelopeStatus extends ExampleBase {

    public GetEnvelopeStatus(ApiClient apiClient) throws IOException {
        super(apiClient);
    }

    /**
     *
     * @param envelopeId - envelope id returned by SendEnvelope.sendEnvelope(...) class
     * @return - envelope
     * @throws ApiException
     */
    public Envelope getEnvelope(String envelopeId) throws ApiException, IOException {

        if(envelopeId == null)
            throw new IllegalArgumentException(
                    "PROBLEM: This example software doesn't know which envelope's" +
                    " information should be looked up.\n" +
                    "SOLUTION: First run the <b>Send Envelope via email</b> example to create an envelope.");

        this.validateToken();

        // call the getEnvelope() API
        EnvelopesApi envelopeApi = new EnvelopesApi(this.apiClient);

        return envelopeApi.getEnvelope(this.accountID, envelopeId);
    }
}
