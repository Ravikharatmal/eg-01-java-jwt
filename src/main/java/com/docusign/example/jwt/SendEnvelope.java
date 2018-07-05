package com.docusign.example.jwt;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import com.sun.jersey.core.util.Base64;

import java.io.IOException;
import java.util.Arrays;

class SendEnvelope extends ExampleBase {

    private static final String DOC_2_DOCX = "World_Wide_Corp_Battle_Plan_Trafalgar.docx";
    private static final String DOC_3_PDF = "World_Wide_Corp_lorem.pdf";
    private static final String ENVELOPE_1_DOCUMENT_1 = "<!DOCTYPE html>" +
            "<html>" +
            "    <head>" +
            "      <meta charset=\"UTF-8\">" +
            "    </head>" +
            "    <body style=\"font-family:sans-serif;margin-left:2em;\">" +
            "    <h1 style=\"font-family: 'Trebuchet MS', Helvetica, sans-serif;" +
            "         color: darkblue;margin-bottom: 0;\">World Wide Corp</h1>" +
            "    <h2 style=\"font-family: 'Trebuchet MS', Helvetica, sans-serif;" +
            "         margin-top: 0px;margin-bottom: 3.5em;font-size: 1em;" +
            "         color: darkblue;\">Order Processing Division</h2>" +
            "  <h4>Ordered by " + DSConfig.SIGNER_NAME + "</h4>" +
            "    <p style=\"margin-top:0em; margin-bottom:0em;\">Email: " + DSConfig.SIGNER_EMAIL + "</p>" +
            "    <p style=\"margin-top:0em; margin-bottom:0em;\">Copy to: " + DSConfig.CC_NAME + ", " + DSConfig.SIGNER_EMAIL + "</p>" +
            "    <p style=\"margin-top:3em;\">" +
            "  Candy bonbon pastry jujubes lollipop wafer biscuit biscuit. Topping brownie sesame snaps" +
            " sweet roll pie. Croissant danish biscuit soufflé caramels jujubes jelly. Dragée danish caramels lemon" +
            " drops dragée. Gummi bears cupcake biscuit tiramisu sugar plum pastry." +
            " Dragée gummies applicake pudding liquorice. Donut jujubes oat cake jelly-o. Dessert bear claw chocolate" +
            " cake gummies lollipop sugar plum ice cream gummies cheesecake." +
            "    </p>" +
            "    <!-- Note the anchor tag for the signature field is in white. -->" +
            "    <h3 style=\"margin-top:3em;\">Agreed: <span style=\"color:white;\">**signature_1**/</span></h3>" +
            "    </body>" +
            "</html>";

    public SendEnvelope(ApiClient apiClient) throws IOException {
        super(apiClient);
    }

    /**
     * method show the usage of
     * @return
     * @throws ApiException
     * @throws IOException
     */
    public EnvelopeSummary sendEnvelope() throws ApiException, IOException {

        this.checkToken();

        EnvelopeDefinition envelope = this.createEvelope();

        EnvelopesApi envelopeApi = new EnvelopesApi(this.apiClient);

        EnvelopeSummary results = envelopeApi.createEnvelope(this.getAccountId(), envelope);

        return results;
    }
    /**
     * This method creates envelope from template
     *
     * @return instance of EnvelopeDefinition
     */
    public EnvelopeDefinition createEvelope() throws IOException {
        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign this document sent from Node SDK");

        Document doc1 = createDocumentFromTemplate("1","Order acknowledgement","html",
                ENVELOPE_1_DOCUMENT_1.getBytes());
        Document doc2 = createDocumentFromTemplate("2","Battle Plan","docx",
                DSHelper.readContent(DOC_2_DOCX));
        Document doc3 = createDocumentFromTemplate("3","Lorem Ipsum","pdf",
                DSHelper.readContent(DOC_3_PDF));

        // The order in the docs array determines the order in the envelope
        envelopeDefinition.setDocuments(Arrays.asList(doc1, doc2, doc3));
        // create a signer recipient to sign the document, identified by name and email
        // We're setting the parameters via the object creation
        Signer signer1 = createSigner();
        // routingOrder (lower means earlier) determines the order of deliveries
        // to the recipients. Parallel routing order is supported by using the
        // same integer as the order for two or more recipients.

        // create a cc recipient to receive a copy of the documents, identified by name and email
        // We're setting the parameters via setters
        CarbonCopy cc1 = createCarbonCopy();
        // Create signHere fields (also known as tabs) on the documents,
        // We're using anchor (autoPlace) positioning
        //
        // The DocuSign platform seaches throughout your envelope's
        // documents for matching anchor strings. So the
        // sign_here_2 tab will be used in both document 2 and 3 since they
        // use the same anchor string for their "signer 1" tabs.
        SignHere signHere1 = createSignHere("**signature_1**","pixels", "20","10");
        SignHere signHere2 = createSignHere("/sn1/","pixels", "20","10");
        // Tabs are set per recipient / signer
        setSignerTabs(signer1, signHere1, signHere2);
        // Add the recipients to the envelope object
        Recipients recipients = createRecipients(signer1, cc1);
        envelopeDefinition.setRecipients(recipients);
        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        envelopeDefinition.setStatus("sent");

        return envelopeDefinition;
    }

    /**
     * ﻿This method creates Recipients instance and populates its signers and carbon copies
     *
     * @param signer
     * @param cc
     * @return
     */
    private Recipients createRecipients(Signer signer, CarbonCopy cc) {

        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer));
        recipients.setCarbonCopies(Arrays.asList(cc));

        return recipients;
    }

    /**
     *
     * ﻿This method create Tabs
     *
     * @param signer - ﻿Signer instance to be set tabs
     * @param signers - ﻿SignHere array
     */
    private void setSignerTabs(Signer signer, SignHere...signers) {
        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(Arrays.asList(signers));
        signer.setTabs(tabs);
    }

    /**
     *﻿ This method create SignHere anchor
     *
     * @param anchorPattern ﻿- anchor pattern
     * @param anchorUnits -﻿ anchor units
     * @param anchorXOffset - ﻿anchor x offset
     * @param anchorYOffset - ﻿anchor y offset
     * @return
     */
    private SignHere createSignHere(String anchorPattern, String anchorUnits,
                                    String anchorXOffset, String anchorYOffset) {
        SignHere signHere = new SignHere();
        signHere.setAnchorString(anchorPattern);
        signHere.setAnchorUnits(anchorUnits);
        signHere.setAnchorXOffset(anchorXOffset);
        signHere.anchorYOffset(anchorYOffset);
        return signHere;
    }

    /**
     * This method creates CarbonCopy instance and populate its members
     *
     * @return {CarbonCopy} instance
     */
    private CarbonCopy createCarbonCopy() {
        CarbonCopy cc = new CarbonCopy();
        cc.setEmail(DSConfig.CC_EMAIL);
        cc.setName(DSConfig.CC_NAME);
        cc.setRoutingOrder("2");
        cc.setRecipientId("2");
        return cc;
    }

    /**
     * ﻿This method creates Signer instance and populates its members
     *
     * @return Signer instance
     */
    private Signer createSigner() {
        Signer signer = new Signer();
        signer.setEmail(DSConfig.SIGNER_EMAIL);
        signer.setName(DSConfig.SIGNER_NAME);
        signer.setRecipientId("1");
        signer.setRoutingOrder("1");
        return signer;
    }

    /**
     * helper method to create document from String content template
     * @param id document id
     * @param name file name
     * @param fileExtension file extension
     * @param content file content as String
     * @return Document
     */
    private Document createDocumentFromTemplate(String id,String name, String fileExtension, byte [] content){

        Document document = new Document();

        String base64Content = new String(Base64.encode(content));

        document.setDocumentBase64(base64Content);
        // can be different from actual file name
        document.setName(name);
        // Source data format. Signed docs are always pdf.
        document.setFileExtension(fileExtension);
        // a label used to reference the doc
        document.setDocumentId(id);

        return document;
    }



}
