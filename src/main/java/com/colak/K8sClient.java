package com.colak;


import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class K8sClient {

    private final ApiClient apiClient;

    public K8sClient() {
        try {
            this.apiClient = Config.defaultClient();
        } catch (IOException e) {
            log.error("build K8s-Client error", e);
            throw new RuntimeException("build K8s-Client error");
        }
    }

    public V1PodList getAllPodList() {
        CoreV1Api api = new CoreV1Api(apiClient);
        // invokes the CoreV1Api client
        try {
            return api.listPodForAllNamespaces().execute();
        } catch (ApiException exception) {
            log.error("get podlist error:" + exception.getResponseBody(), exception);
        }
        return null;
    }

    public V1PodList getAllPodList(String namespace) {
        CoreV1Api api = new CoreV1Api(apiClient);
        // invokes the CoreV1Api client
        try {
            return api.listNamespacedPod(namespace).execute();
        } catch (ApiException exception) {
            log.error("get podlist error:" + exception.getResponseBody(), exception);
        }
        return null;
    }

}
