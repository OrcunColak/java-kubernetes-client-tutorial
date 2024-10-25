package com.colak;


import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class K8sClientTest {

    @Test
    void getAllPodList() {
        K8sClient k8sClient = new K8sClient();
        V1PodList podList = k8sClient.getAllPodList("test-scale-ns");
        for (V1Pod pod : podList.getItems()) {
            V1ObjectMeta metadata = pod.getMetadata();
            assert metadata != null;
            log.info("{} : {}", metadata.getNamespace(), metadata.getName());
        }
    }

    @Test
    void getDefaultNamespacePodList() {
        K8sClient k8sClient = new K8sClient();
        V1PodList podList = k8sClient.getAllPodList("default");
        for (V1Pod pod : podList.getItems()) {
            V1ObjectMeta metadata = pod.getMetadata();
            assert metadata != null;
            log.info("{} : {}", metadata.getNamespace(), metadata.getName());
        }
    }

    @Test
    void getReplicas() {
        K8sClient k8sClient = new K8sClient();
        int replicas = k8sClient.getReplicas("nginx", "test-scale-ns");
        log.info("nginx replicas {} : ", replicas);
    }
}