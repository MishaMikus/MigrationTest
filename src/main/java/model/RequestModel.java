package model;

import java.util.HashMap;
import java.util.Map;

public class RequestModel {
    private String path;
    private String contentType;
    private String protocol;
    private String host;
    private Object body;
    private Map<String, Object> headers = new HashMap<>();
    private Map<String, Object> params = new HashMap<>();
    private String baseUserName;
    private String baseUserPassword;
    private String method;
    private Boolean requestLog;
    private Boolean responseLog;
    private Boolean useCookie;
    private Boolean responseIfErrorLog;


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public void setBaseUserName(String baseUserName) {
        this.baseUserName = baseUserName;
    }

    public void setBaseUserPassword(String baseUserPassword) {
        this.baseUserPassword = baseUserPassword;
    }

    public void setRequestLog(boolean requestLog) {
        this.requestLog = requestLog;
    }

    public void setResponseLog(Boolean responseLog) {
        this.responseLog = responseLog;
    }

    public String getPath() {
        return path;
    }

    public String getContentType() {
        return contentType;
    }

    public Object getBody() {
        return body;
    }

    public String getBaseUserName() {
        return baseUserName;
    }

    public String getBaseUserPassword() {
        return baseUserPassword;
    }

    public Boolean getRequestLog() {
        return requestLog;
    }

    public Boolean getResponseLog() {
        return responseLog;
    }

    public void setUseCookie(Boolean useCookie) {
        this.useCookie = useCookie;
    }

    public Boolean getUseCookie() {
        return useCookie;
    }

    public void putHeader(String key, Object value) {
        headers.put(key, value);
    }

    public void putParam(String key, Object value) {
        params.put(key, value);
    }

    public void setResponseIfErrorLog(Boolean responseIfErrorLog) {
        this.responseIfErrorLog = responseIfErrorLog;
    }

    public Boolean getResponseIfErrorLog() {
        return responseIfErrorLog;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setRequestLog(Boolean requestLog) {
        this.requestLog = requestLog;
    }

    public String getURL() {
        return protocol+host+path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "path='" + path + '\'' +
                ", contentType='" + contentType + '\'' +
                ", protocol='" + protocol + '\'' +
                ", host='" + host + '\'' +
                ", body=" + body +
                ", headers=" + headers +
                ", params=" + params +
                ", baseUserName='" + baseUserName + '\'' +
                ", baseUserPassword='" + baseUserPassword + '\'' +
                ", method='" + method + '\'' +
                ", requestLog=" + requestLog +
                ", responseLog=" + responseLog +
                ", useCookie=" + useCookie +
                ", responseIfErrorLog=" + responseIfErrorLog +
                '}';
    }
}
