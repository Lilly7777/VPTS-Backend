package com.lilly.lubenova.vptsrest.repository;

import com.lilly.lubenova.vptsrest.model.User;
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends FirestoreReactiveRepository<User> {
    Flux<User> findAllByLocation(String location);
}
