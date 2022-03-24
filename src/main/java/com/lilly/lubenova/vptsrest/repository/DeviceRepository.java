package com.lilly.lubenova.vptsrest.repository;

import com.lilly.lubenova.vptsrest.model.Device;
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

public interface DeviceRepository extends FirestoreReactiveRepository<Device> {
}
