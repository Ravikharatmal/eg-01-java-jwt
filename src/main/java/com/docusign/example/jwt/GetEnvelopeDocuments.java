package com.docusign.example.jwt;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDocument;
import com.docusign.esign.model.EnvelopeDocumentsResult;

import java.io.File;
import java.io.IOException;


import java.util.List;

/**
 * This class demonstrate list and download documents API call and save the bytes into given directory.
 */
public class GetEnvelopeDocuments extends ExampleBase {

    public GetEnvelopeDocuments(ApiClient apiClient) throws IOException {
        super(apiClient);
    }

    /**
     * This method list document and then download them one by one
     *
     * @param envelopeId - envelope id returned by SendEnvelope.sendEnvelope(...) class
     * @throws ApiException
     * @throws IOException
     */
    public void download(String  envelopeId) throws ApiException, IOException {
        if(envelopeId == null)
            throw new IllegalArgumentException(
                    "PROBLEM: This example software doesn't know which envelope's " +
                    "information should be looked up.\n" +
                    "SOLUTION: First run the <b>Send Envelope via email</b> example to create an envelope.");

        this.validateToken();

        // The workflow will be multiple API requests:
        //  1) list the envelope's documents
        //  2) Loop to get each document
        EnvelopesApi envelopeApi = new EnvelopesApi(this.apiClient);
        String docDownloadDirPath = DSHelper.ensureDirExistance("downloaded_documents");
        EnvelopeDocumentsResult documents = envelopeApi.listDocuments(this.accountID, envelopeId);
        DSHelper.printPrettyJSON(documents);

        List<EnvelopeDocument> envelopeDocuments = documents.getEnvelopeDocuments();
        System.out.println("Download files path: " + docDownloadDirPath);
        for (EnvelopeDocument doc: envelopeDocuments) {
            String docName = String.format("%s__%s", envelopeId, doc.getName());
            boolean hasPDFsuffix = docName.toUpperCase().endsWith(".PDF");

            if(("content".equals(doc.getType()) || "summary".equals(doc.getType())) && !hasPDFsuffix) {
                docName +=".pdf";
            }

            byte[] docBytes = envelopeApi.getDocument(this.accountID, envelopeId, doc.getDocumentId(), null);
            String filePath = String.format("%s%c%s", docDownloadDirPath, File.separatorChar, docName);
            DSHelper.writeByteArrayToFile(filePath, docBytes);

            System.out.println(String.format("Wrote document id %s to %s", doc.getDocumentId(), docName));
        }
    }
}
