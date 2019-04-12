package io.graphenee.core.model.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GxJpaRepository<ENTITY, ID> extends JpaRepository<ENTITY, ID> {

	default ENTITY findOne(ID id) {
		Optional<ENTITY> found = findById(id);
		return found.isPresent() ? found.get() : null;
	}

}
