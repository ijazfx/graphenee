package io.graphenee.core.model.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GxJpaRepository<ENTITY, ID> extends JpaRepository<ENTITY, ID> {

	default ENTITY findOne(ID id) {
		Optional<ENTITY> found = findById(id);
		return found.isPresent() ? found.get() : null;
	}

	default List<ENTITY> findAll(Iterable<ID> ids) {
		return findAllById(ids);
	}

	default <S extends ENTITY> List<S> save(Iterable<S> entities) {
		return saveAll(entities);
	}

	default void delete(Iterable<? extends ENTITY> entities) {
		deleteAll(entities);
	}

}
