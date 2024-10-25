package com.colak;


import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentSpec;
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

    public int getReplicas(String deploymentName, String namespace) {
        int result = -1;
        AppsV1Api api = new AppsV1Api(apiClient);

        try {
            V1Deployment deployment = api.readNamespacedDeployment(deploymentName, namespace).execute();
            V1DeploymentSpec spec = deployment.getSpec();

            assert spec != null;
            result = spec.getReplicas();
        } catch (ApiException exception) {
            log.error("set replicas error:" + exception.getResponseBody(), exception);
        }
        return result;
    }

    public void setReplicas(String deploymentName, String namespace, int replicas) {
        AppsV1Api api = new AppsV1Api();

        try {
            V1Deployment deployment = api.readNamespacedDeployment(deploymentName, namespace).execute();
            V1DeploymentSpec spec = deployment.getSpec();

            assert spec != null;
            spec.setReplicas(replicas);
            api.replaceNamespacedDeployment(deploymentName, namespace, deployment).execute();
        } catch (ApiException exception) {
            log.error("set replicas error:" + exception.getResponseBody(), exception);
        }
    }
}
