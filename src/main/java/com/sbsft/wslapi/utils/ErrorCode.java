package com.sbsft.wslapi.utils;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCode {
	SUCCESS			    			(0, ""),
    UNKNOWN_ERROR					(999, "unknown.error"),
    SESSION_TIMEOUT					(408, "session.timeout"),
    NO_TOKEN					(200, "token.required"),
    UNVALID_TOKEN					(201, "token.unvalid"),
    UNVALID_PKG_NAME					(408, "unvalid.pkg.name");

    private int code;
    private String message;
    
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
    	return code;
    }
    
    public String getMessage() {
    	return message;
    }
    
    public String getMessage(String paramName) {
    	return message +" : " + paramName;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public static ErrorCode get(int code) {
        ErrorCode result = null;
		for (ErrorCode each : values()) {
			if (code == each.getCode()) {
                result = each;
                break;
			}
		}
		return result;
	}
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Map toMap() {
    	Map error = new HashMap();
    	
    	error.put("code", code);
    	error.put("message", message);
    	
    	Map result = new HashMap();
    	result.put("error", error);
    	
    	return result;
    }
}
