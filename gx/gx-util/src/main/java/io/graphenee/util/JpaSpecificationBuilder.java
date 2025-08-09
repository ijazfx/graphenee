package io.graphenee.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

/**
 * A builder for creating JPA specifications.
 *
 * @param <T> The entity type.
 */
public class JpaSpecificationBuilder<T> {

	private LinkedList<Specification<T>> specsQueue;

	private JpaSpecificationBuilder() {
		specsQueue = new LinkedList<>();
	}

	/**
	 * Gets a new instance of this builder.
	 * @param <I> The entity type.
	 * @return The new instance.
	 */
	public static <I> JpaSpecificationBuilder<I> get() {
		return new JpaSpecificationBuilder<I>();
	}

	/**
	 * Adds an AND operator to the specification.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> and() {
		if (specsQueue.isEmpty())
			return this;
		Specification<T> spec = specsQueue.removeFirst();
		while (!specsQueue.isEmpty()) {
			spec = spec.and(specsQueue.removeFirst());
		}
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Builds the specification.
	 * @param <S> The entity type.
	 * @return The specification.
	 */
	public <S extends T> Specification<T> build() {
		and();
		if (specsQueue.isEmpty())
			return null;
		return specsQueue.removeFirst();
	}

	/**
	 * Adds an OR operator to the specification.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> or() {
		if (specsQueue.isEmpty())
			return this;
		Specification<T> spec = specsQueue.removeFirst();
		while (!specsQueue.isEmpty()) {
			spec = spec.or(specsQueue.removeFirst());
		}
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds an equals operator to the specification.
	 * @param key The key.
	 * @param value The value.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> eq(String key, Object value) {
		if (value == null || (value instanceof String && value.toString().trim().length() == 0))
			return this;
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return cb.equal(join.get(parts[parts.length - 1]), value);
			}
			return cb.equal(root.get(key), value);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds a not equals operator to the specification.
	 * @param key The key.
	 * @param value The value.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> ne(String key, Object value) {
		if (value == null || (value instanceof String && value.toString().trim().length() == 0))
			return this;
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return cb.notEqual(join.get(parts[parts.length - 1]), value);
			}
			return cb.notEqual(root.get(key), value);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds a like operator to the specification.
	 * @param key The key.
	 * @param pattern The pattern.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> like(String key, String pattern) {
		if (pattern == null || pattern.trim().length() == 0)
			return this;
		final String likePattern = toLowerCase(pattern);
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return cb.like(cb.lower(join.get(parts[parts.length - 1])), likePattern);
			}
			return cb.like(cb.lower(root.get(key)), likePattern);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds a join to the specification.
	 * @param <J> The join type.
	 * @param on The join on.
	 * @param key The key.
	 * @param pattern The pattern.
	 * @return This instance.
	 */
	public <J> JpaSpecificationBuilder<T> join(String on, String key, Object pattern) {
		if (key == null || on == null || pattern == null)
			return this;
		Specification<T> spec = null;
		if (pattern.getClass().equals(String.class)) {
			final String likePattern = toLowerCase(pattern.toString());
			spec = (root, cq, cb) -> {
				Join<T, J> jn = root.join(on);
				return cb.like(cb.lower(jn.get(key)), likePattern);
			};
		} else if (pattern.getClass().getSuperclass().equals(Number.class)) {
			final Number number = (Number) pattern;
			spec = (root, cq, cb) -> {
				Join<T, J> jn = root.join(on);
				return cb.equal(jn.get(key), number);
			};
		}
		specsQueue.add(spec);
		return this;
	}

	private String toLowerCase(String pattern) {
		pattern = pattern.trim();
		if (!pattern.contains("%")) {
			pattern = "%" + pattern + "%";
		}
		return pattern.toLowerCase();
	}

	/**
	 * Adds a greater than operator to the specification.
	 * @param <VT> The value type.
	 * @param key The key.
	 * @param value The value.
	 * @return This instance.
	 */
	public <VT extends Number> JpaSpecificationBuilder<T> gt(String key, VT value) {
		if (value == null)
			return this;
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return cb.gt(join.get(parts[parts.length - 1]), value);
			}
			return cb.gt(root.get(key), value);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds a greater than or equal to operator to the specification.
	 * @param <VT> The value type.
	 * @param key The key.
	 * @param value The value.
	 * @return This instance.
	 */
	public <VT extends Number> JpaSpecificationBuilder<T> ge(String key, VT value) {
		if (value == null)
			return this;
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return cb.ge(join.get(parts[parts.length - 1]), value);
			}
			return cb.ge(root.get(key), value);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds a less than operator to the specification.
	 * @param <VT> The value type.
	 * @param key The key.
	 * @param value The value.
	 * @return This instance.
	 */
	public <VT extends Number> JpaSpecificationBuilder<T> lt(String key, VT value) {
		if (value == null)
			return this;
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return cb.lt(join.get(parts[parts.length - 1]), value);
			}
			return cb.lt(root.get(key), value);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds a less than or equal to operator to the specification.
	 * @param <VT> The value type.
	 * @param key The key.
	 * @param value The value.
	 * @return This instance.
	 */
	public <VT extends Number> JpaSpecificationBuilder<T> le(String key, VT value) {
		if (value == null)
			return this.and();
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return cb.le(join.get(parts[parts.length - 1]), value);
			}
			return cb.le(root.get(key), value);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds a between operator to the specification.
	 * @param key The key.
	 * @param value1 The first value.
	 * @param value2 The second value.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> between(String key, Integer value1, Integer value2) {
		if (value1 == null || value2 == null)
			return this;
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return cb.between(join.get(parts[parts.length - 1]), value1, value2);
			}
			return cb.between(root.get(key), value1, value2);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds a between operator to the specification.
	 * @param key The key.
	 * @param value1 The first value.
	 * @param value2 The second value.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> between(String key, Long value1, Long value2) {
		if (value1 == null || value2 == null)
			return this;
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return cb.between(join.get(parts[parts.length - 1]), value1, value2);
			}
			return cb.between(root.get(key), value1, value2);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds a between operator to the specification.
	 * @param key The key.
	 * @param value1 The first value.
	 * @param value2 The second value.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> between(String key, Date value1, Date value2) {
		if (value1 == null || value2 == null)
			return this;
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return cb.between(join.get(parts[parts.length - 1]), value1, value2);
			}
			return cb.between(root.get(key), value1, value2);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds a before operator to the specification.
	 * @param key The key.
	 * @param value The value.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> before(String key, Date value) {
		if (value == null)
			return this;
		Timestamp fromValue = new Timestamp(0);
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return cb.between(join.get(parts[parts.length - 1]), fromValue, value);
			}
			return cb.between(root.get(key), fromValue, value);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds a day operator to the specification.
	 * @param key The key.
	 * @param value The value.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> day(String key, Date value) {
		if (value == null)
			return this;
		Date startOfDay = TRCalendarUtil.startOfDay(value);
		Date endOfDay = TRCalendarUtil.endOfDay(value);
		Specification<T> spec = between(key, startOfDay, endOfDay).build();
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds an after operator to the specification.
	 * @param key The key.
	 * @param value The value.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> after(String key, Date value) {
		if (value == null)
			return this;
		Timestamp toValue = Timestamp.valueOf(LocalDateTime.now().plusYears(1000));
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return cb.between(join.get(parts[parts.length - 1]), value, toValue);
			}
			return cb.between(root.get(key), value, toValue);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds an in operator to the specification.
	 * @param <VT> The value type.
	 * @param key The key.
	 * @param values The values.
	 * @return This instance.
	 */
	public <VT> JpaSpecificationBuilder<T> in(String key, VT[] values) {
		if (values == null || values.length == 0)
			return this;
		List<Object> filtered = new ArrayList<>();
		for (VT value : values) {
			if (value instanceof String) {
				String trimmed = value.toString().trim();
				if (trimmed.length() > 0)
					filtered.add(trimmed);
			} else if (value != null) {
				filtered.add(value);
			}
		}
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return join.get(parts[parts.length - 1]).in(filtered);
			}
			return root.get(key).in(filtered);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds an in operator to the specification.
	 * @param <VT> The value type.
	 * @param key The key.
	 * @param values The values.
	 * @return This instance.
	 */
	public <VT> JpaSpecificationBuilder<T> in(String key, Iterable<VT> values) {
		if (values == null)
			return this;
		List<Object> filtered = new ArrayList<>();
		Iterator<VT> iter = values.iterator();
		VT value = null;
		while (iter.hasNext()) {
			value = iter.next();
			if (value instanceof String) {
				String trimmed = value.toString().trim();
				if (trimmed.length() > 0)
					filtered.add(trimmed);
			} else if (value != null) {
				filtered.add(value);
			}
		}
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return join.get(parts[parts.length - 1]).in(filtered);
			}
			return root.get(key).in(filtered);
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds a not in operator to the specification.
	 * @param <VT> The value type.
	 * @param key The key.
	 * @param values The values.
	 * @return This instance.
	 */
	public <VT> JpaSpecificationBuilder<T> notIn(String key, Iterable<VT> values) {
		if (values == null)
			return this;
		List<Object> filtered = new ArrayList<>();
		Iterator<VT> iter = values.iterator();
		VT value = null;
		while (iter.hasNext()) {
			value = iter.next();
			if (value instanceof String) {
				String trimmed = value.toString().trim();
				if (trimmed.length() > 0)
					filtered.add(trimmed);
			} else if (value != null) {
				filtered.add(value);
			}
		}
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return join.get(parts[parts.length - 1]).in(filtered).not();
			}
			return root.get(key).in(filtered).not();
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds a not in operator to the specification.
	 * @param <VT> The value type.
	 * @param key The key.
	 * @param values The values.
	 * @return This instance.
	 */
	public <VT> JpaSpecificationBuilder<T> notIn(String key, VT[] values) {
		if (values == null || values.length == 0)
			return this;
		List<Object> filtered = new ArrayList<>();
		for (VT value : values) {
			if (value instanceof String) {
				String trimmed = value.toString().trim();
				if (trimmed.length() > 0)
					filtered.add(trimmed);
			} else if (value != null) {
				filtered.add(value);
			}
		}
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return join.get(parts[parts.length - 1]).in(filtered).not();
			}
			return root.get(key).in(filtered).not();
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds an is empty operator to the specification.
	 * @param key The key.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> isEmpty(String key) {
		if (key == null)
			return this;
		Specification<T> spec = (root, cq, cb) -> {
			return cb.isEmpty(root.get(key));
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds an is not empty operator to the specification.
	 * @param key The key.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> isNotEmpty(String key) {
		if (key == null)
			return this;
		Specification<T> spec = (root, cq, cb) -> {
			return cb.isNotEmpty(root.get(key));
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds an is null operator to the specification.
	 * @param key The key.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> isNull(String key) {
		if (key == null)
			return this;
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return join.get(parts[parts.length - 1]).isNull();
			}
			return cb.isNull(root.get(key));
		};
		specsQueue.add(spec);
		return this;
	}

	/**
	 * Adds an is not null operator to the specification.
	 * @param key The key.
	 * @return This instance.
	 */
	public JpaSpecificationBuilder<T> isNotNull(String key) {
		if (key == null)
			return this;
		Specification<T> spec = (root, cq, cb) -> {
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				Join<Object, Object> join = root.join(parts[0]);
				for (int i = 1; i < parts.length - 1; i++) {
					join = join.join(parts[i]);
				}
				return join.get(parts[parts.length - 1]).isNotNull();
			}
			return cb.isNotNull(root.get(key));
		};
		specsQueue.add(spec);
		return this;
	}
}