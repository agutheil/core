package com.mightymerce.core.web.rest.util;

import com.mightymerce.core.domain.enumeration.PublicationStatus;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for http header creation.
 *
 */
public class HeaderUtil {

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-coreApp-alert", message);
        headers.add("X-coreApp-params", param);
        return headers;
    }

    public static HttpHeaders createWarningAlert(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-coreApp-alert", message);
        headers.add("X-coreApp-alert-type", "warning");
        return headers;
    }

    public static HttpHeaders createErrorAlert(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-coreApp-alert", message);
        headers.add("X-coreApp-alert-type", "error");
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("coreApp." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("coreApp." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("coreApp." + entityName + ".deleted", param);
    }

}
