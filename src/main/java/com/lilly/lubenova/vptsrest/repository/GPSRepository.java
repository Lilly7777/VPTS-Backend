package com.lilly.lubenova.vptsrest.repository;

import com.lilly.lubenova.vptsrest.model.GPSRecord;
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

public interface GPSRepository extends FirestoreReactiveRepository<GPSRecord> {
}
