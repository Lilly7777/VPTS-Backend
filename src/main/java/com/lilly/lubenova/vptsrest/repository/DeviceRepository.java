package com.lilly.lubenova.vptsrest.repository;

import com.lilly.lubenova.vptsrest.model.Device;
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;
import reactor.core.publisher.Flux;

public interface DeviceRepository extends FirestoreReactiveRepository<Device> {
    Flux<Device> findAllByUserId(String userId);
}
