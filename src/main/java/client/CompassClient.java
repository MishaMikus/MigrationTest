package client;

import model.RequestModel;
import model.ResponseModel;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class CompassClient {

    private BaseClient baseClient;
    private String protocol;
    private String host;

    public CompassClient(BaseClient client, String protocol, String host) {
        baseClient = client;
        this.protocol = protocol;
        this.host = host;
    }

    public ResponseModel scvCreateAccount(String email) throws UnsupportedEncodingException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(protocol);
        requestModel.setHost("stg.srs.adidas.com");
        requestModel.setPath("/scvRESTServices/account/createAccount");
        requestModel.setBody("{\"access_token_manager_id\":\"jwt\"," +
                "\"actionType\":\"REGISTRATION\"," +
                "\"password\":\"1111111q\"," +
                "\"scope\":\"pii\"," +
                "\"version\":\"11.0\"," +
                "\"email\":\"" + email + "\"," +
                "\"firstName\":\"\"," +
                "\"lastName\":\"\"," +
                "\"minAgeConfirmation\":\"Y\"," +
                "\"countryOfSite\":\"US\"," +
                "\"clientId\":\"97C263EA7D1C2EB2EACE0E16D6902ADC\"," +
                "\"dateOfBirth\":\"1988-12-30\"}");
        requestModel.setMethod("POST");
        requestModel.setUseCookie(true);
        return baseClient.call(requestModel);
    }

    public ResponseModel update(String token) throws UnsupportedEncodingException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(protocol);
        requestModel.setHost("stg.srs.adidas.com");
        requestModel.setPath("/scvRESTServices/account/updateAccount");
        requestModel.setBody("{\"oauthToken\":\"" + token + "\"," +
                "\"countryOfSite\":\"US\"," +
                "\"lastName\":\"Basta\"," +
                "\"version\":\"11.0\"," +
                "\"firstName\":\"Basta\"," +
                "\"dateOfBirth\":\"1989-10-18\"," +
                "\"actionType\":\"UPDATEPROFILE\"}");
        requestModel.setMethod("POST");
        requestModel.setUseCookie(true);
        return baseClient.call(requestModel);
    }

    public ResponseModel patchProfile(String token) throws UnsupportedEncodingException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(protocol);
        requestModel.setHost("api.qa.compass.aws.ds.3stripes.net");
        requestModel.putHeader("Authorization", "Bearer " + token);
        requestModel.setPath("/v1/user/profile");
        requestModel.setBody("{\"genderIdentification\":\"Female\"," +
                "\"country\":\"DE\"," +
                "\"height\":183," +
                "\"weight\":70," +
                "\"birthdate\":\"19891018\"}");
        requestModel.setMethod("PATCH");
        requestModel.setUseCookie(true);
        //requestModel.setResponseIfErrorLog(true);
        return baseClient.call(requestModel);
    }

    public ResponseModel patchSettings(String token, Map<String, String> sourcesMap) throws UnsupportedEncodingException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(protocol);
        requestModel.setHost("api.qa.compass.aws.ds.3stripes.net");
        requestModel.putHeader("Authorization", "Bearer " + token);
        requestModel.setPath("/v1/user/settings");
        requestModel.setBody("{\"locale\":\"en-GB\"," +
                "\"downloadUsingCellData\":false," +
                "\"dataSources\":{\"activeMinutes\":\"" + sourcesMap.get("data_sources_ActiveMinutes") + "\"," +
                "\"workouts\":\"" + sourcesMap.get("data_sources_Workouts") + "\"," +
                "\"steps\":\"" + sourcesMap.get("data_sources_Steps") + "\"," +
                "\"weight\":\"" + sourcesMap.get("data_sources_Weight") + "\"," +
                "\"sleep\":\"" + sourcesMap.get("data_sources_Sleep") + "\"}," +
                "\"language\":\"English\"," +
                "\"downloadOnlyWhenCharging\":false," +
                "\"trackCalories\":false," +
                "\"units\":{\"height\":\"cm\"," +
                "\"weight\":\"kg\"}," +
                "\"notifications\":{\"newDiscoveryRecommendation\":false," +
                "\"tips\":false," +
                "\"insights\":false," +
                "\"workoutSummary\":false}}");
        requestModel.setMethod("PATCH");
        requestModel.setUseCookie(true);
       // requestModel.setResponseIfErrorLog(true);
        return baseClient.call(requestModel);
    }

    public ResponseModel getDataSources(String token) throws UnsupportedEncodingException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(protocol);
        requestModel.setHost("api.qa.compass.aws.ds.3stripes.net");
        requestModel.putHeader("Authorization", "Bearer " + token);
        requestModel.setPath("v1/user/settings/dataSources");
        requestModel.setMethod("GET");
        requestModel.setUseCookie(true);
        //requestModel.setResponseIfErrorLog(true);
        return baseClient.call(requestModel);
    }

    public ResponseModel login(String email) throws UnsupportedEncodingException {
        RequestModel requestModel = new RequestModel();
        requestModel.setProtocol(protocol);
        requestModel.setHost("stg.pf.adidas.com");
        requestModel.setPath("/as/token.oauth2");
        requestModel.putParam("validator_id", "confirmed4oauthUS");
        requestModel.putParam("username", email);
        requestModel.putParam("scope", "pii");
        requestModel.putParam("password", "1111111q");
        requestModel.putParam("grant_type", "password");
        requestModel.putParam("client_id", "97C263EA7D1C2EB2EACE0E16D6902ADC");
        requestModel.putParam("access_token_manager_id", "jwt");
        requestModel.setMethod("POST");
        requestModel.setUseCookie(true);
        return baseClient.call(requestModel);
    }
}