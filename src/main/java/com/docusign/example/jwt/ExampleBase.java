package com.docusign.example.jwt;

import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This is an example base class to be extended to show functionality example.
 * its has a apiClient member as a constructor argument for later usage in API calls.
 */
public class ExampleBase {

    private static final long TOKEN_EXPIRATION_IN_SECONDS = 3600;
    private static final long TOKEN_REPLACEMENT_IN_MILLISECONDS = 10 * 60 * 1000;

    private static OAuth.Account _account;
    private static File privateKeyTempFile = null;
    private static long expiresIn;
    private static String _token = null;
    protected final ApiClient apiClient;

    protected static String getAccountId() {
        return _account.getAccountId();
    };



    public ExampleBase(ApiClient apiClient) throws IOException {
        privateKeyTempFile = DSHelper.createPrivateKeyTempFile("private-key");
        this.apiClient =  apiClient;
    }

    protected void checkToken() throws IOException, ApiException {

        if(this._token == null
                || (System.currentTimeMillis() + TOKEN_REPLACEMENT_IN_MILLISECONDS) > this.expiresIn) {
            updateToken();
        }
    }

    private void updateToken() throws IOException, ApiException {
        apiClient.configureJWTAuthorizationFlow(
                privateKeyTempFile.getAbsolutePath(),
                privateKeyTempFile.getAbsolutePath(),
                DSConfig.AUD(),
                DSConfig.CLIENT_ID,
                DSConfig.IMPERSONATED_USER_GUID,
                TOKEN_EXPIRATION_IN_SECONDS);
        //TODO: this is work around to fix incorrect set basePath account
        apiClient.setBasePath(null);

        if(_account == null)
            _account = this.getAccountInfo(apiClient);
        // default or configured account id.
        apiClient.setBasePath(_account.getBaseUri() + "/restapi");

        _token = apiClient.getAccessToken();
        //notice that expiresIn value is not exposed yet by the SDK so, we will assume it as 1 hours.
        expiresIn = System.currentTimeMillis() + (TOKEN_EXPIRATION_IN_SECONDS * 1000);
    }

    private OAuth.Account getAccountInfo(ApiClient client) throws ApiException {
        //TODO: check valid token
        OAuth.UserInfo userInfo = client.getUserInfo(client.getAccessToken());
        OAuth.Account accountInfo = null;

        //TODO: fix the order of check
        if(DSConfig.TARGET_ACCOUNT_ID == null || DSConfig.TARGET_ACCOUNT_ID.length() == 0){
            List<OAuth.Account> accounts = userInfo.getAccounts();

            OAuth.Account acct = this.find(accounts, new ICondition<OAuth.Account>() {
                public boolean test(OAuth.Account member) {
                    return (member.getIsDefault() == "true");
                }
            });

            if (acct != null) return acct;

            acct = this.find(accounts, new ICondition<OAuth.Account>() {
                public boolean test(OAuth.Account member) {
                    return (member.getAccountId() == DSConfig.TARGET_ACCOUNT_ID);
                }
            });

            if (acct != null) return acct;

        }

        return accountInfo;
    }

    private OAuth.Account find(List<OAuth.Account> accounts, ICondition<OAuth.Account> criteria) {
        for (OAuth.Account acct: accounts) {
            if(criteria.test(acct)){
                return acct;
            }
        }
        return null;
    }

    interface ICondition<T> {
        boolean test(T member);
    }
}
