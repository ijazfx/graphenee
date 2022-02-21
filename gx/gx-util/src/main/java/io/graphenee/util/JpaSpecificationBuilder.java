package io.graphenee.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

public class JpaSpecificationBuilder<T> {

    private LinkedList<Specification<T>> specsQueue;

    private JpaSpecificationBuilder() {
        specsQueue = new LinkedList<>();
    }

    public static <I> JpaSpecificationBuilder<I> get() {
        return new JpaSpecificationBuilder<I>();
    }

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

    public <S extends T> Specification<T> build() {
        and();
        if (specsQueue.isEmpty())
            return null;
        return specsQueue.removeFirst();
    }

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

    public <VT extends Number> JpaSpecificationBuilder<T> le(String key, VT value) {
        if (value == null)
            return this;
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

    public JpaSpecificationBuilder<T> between(String key, Timestamp value1, Timestamp value2) {
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

    public JpaSpecificationBuilder<T> before(String key, Timestamp value) {
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

    public JpaSpecificationBuilder<T> after(String key, Timestamp value) {
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

    public JpaSpecificationBuilder<T> isEmpty(String key) {
        if (key == null)
            return this;
        Specification<T> spec = (root, cq, cb) -> {
            return cb.isEmpty(root.get(key));
        };
        specsQueue.add(spec);
        return this;
    }

    public JpaSpecificationBuilder<T> isNotEmpty(String key) {
        if (key == null)
            return this;
        Specification<T> spec = (root, cq, cb) -> {
            return cb.isNotEmpty(root.get(key));
        };
        specsQueue.add(spec);
        return this;
    }

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