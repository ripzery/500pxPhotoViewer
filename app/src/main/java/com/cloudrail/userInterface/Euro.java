package com.cloudrail.userInterface;

import android.content.Context;
import com.cloudrail.auth.UriRedirectListener;
import com.cloudrail.auth.UriView;
import com.cloudrail.exception.CloudRailException;
import com.cloudrail.userInterface.Endpoint.WebAuthorization;
import java.util.HashMap;
import java.util.Map;

public class Euro {

    private Endpoint endpoint;

    /**
     * @param facebookgraphapi_clientidentifier Tags: ClientIdentifier
     * @param facebookgraphapi_uri Tags: RedirectURI, URI
     * @param facebookgraphapi_scope Tags: Scope
     * @param facebookgraphapi_clientsecret Tags: ClientSecret
     * @param facebookgraphapi_csrftoken Tags: CSRFToken
     */
    public Euro(Context context, String facebookgraphapi_clientidentifier, String facebookgraphapi_uri, String facebookgraphapi_scope,
        String facebookgraphapi_clientsecret, String facebookgraphapi_csrftoken) {
        HashMap<String, Object> globals = new HashMap<String, Object>();
        globals.put("facebookgraphapi_clientidentifier", facebookgraphapi_clientidentifier);
        globals.put("facebookgraphapi_uri", facebookgraphapi_uri);
        globals.put("facebookgraphapi_scope", facebookgraphapi_scope);
        globals.put("facebookgraphapi_clientsecret", facebookgraphapi_clientsecret);
        globals.put("facebookgraphapi_csrftoken", facebookgraphapi_csrftoken);
        try {
            endpoint = new Endpoint(context.getAssets().open("euro.cloudrail"), globals);
        } catch (Exception e) {
            throw new CloudRailException(e);
        }
    }

    /**
     * @param locationidentifier Tags: LocationIdentifier
     * @param apikey Tags: APIKey
     * @param answer The ResponseListener for this asynchronous call
     */
    public void getCurrentConditions(Long locationidentifier, String apikey, final GetCurrentConditionsResponseListener answer) {
        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("locationidentifier", locationidentifier);
        inputs.put("apikey", apikey);
        endpoint.call("GetCurrentConditions", inputs, new EndpointCallResponseListener() {
            public void onSuccess(Map<String, Object> result) {
                answer.onSuccess();
            }

            public void onError(CloudRailException error) {
                answer.onError(error);
            }

            public void onProgress(double percentFinished) {
                answer.onProgress(percentFinished);
            }
        });
    }

    /**
     * @param accesstoken Tags: AccessToken
     * @param answer The ResponseListener for this asynchronous call
     */
    public void getUserFeed(String accesstoken, final GetUserFeedResponseListener answer) {
        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("accesstoken", accesstoken);
        endpoint.call("Get User Feed", inputs, new EndpointCallResponseListener() {
            public void onSuccess(Map<String, Object> result) {
                answer.onSuccess();
            }

            public void onError(CloudRailException error) {
                answer.onError(error);
            }

            public void onProgress(double percentFinished) {
                answer.onProgress(percentFinished);
            }
        });
    }

    private WebAuthorization FacebookGraphAPI_Auth;

    /**
     * Set up authorization for Facebook Graph API
     *
     * @param uriOpener An implementation of UriView that opens URIs at the appropriate place
     * @param uriRedirectListener An implementation of UriRedirectListener which receives authorization redirects
     * @param tokenCallback An implementation of AccessTokenCallback which receives the access token after successful authorization
     */
    public void init_FacebookGraphAPI_Auth(UriView uriOpener, UriRedirectListener uriRedirectListener, AccessTokenCallback tokenCallback) {
        FacebookGraphAPI_Auth = endpoint.createWebAuthorization("Facebook Graph API", uriOpener, uriRedirectListener, tokenCallback);
    }

    /**
     * Start the authorization process for Facebook Graph API
     */
    public void start_FacebookGraphAPI_Auth() {
        FacebookGraphAPI_Auth.openLogin();
    }

    public interface GetCurrentConditionsResponseListener {
        /**
         *
         */
        public void onSuccess();

        /**
         * @param error This parameter contains the Exception in case one is thrown
         */
        public void onError(CloudRailException error);

        /**
         * @param percentFinished This parameter contains the progress of the current operation in percent
         */
        public void onProgress(double percentFinished);
    }

    public interface GetUserFeedResponseListener {
        /**
         *
         */
        public void onSuccess();

        /**
         * @param error This parameter contains the Exception in case one is thrown
         */
        public void onError(CloudRailException error);

        /**
         * @param percentFinished This parameter contains the progress of the current operation in percent
         */
        public void onProgress(double percentFinished);
    }
}