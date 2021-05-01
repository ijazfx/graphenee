package io.graphenee.vaadin.flow.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

    public <T> Specification<T> build() {
        and();
        if (specsQueue.isEmpty())
            return null;
        return (Specification<T>) specsQueue.removeFirst();
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
            return cb.equal(root.get(key), value);
        };
        specsQueue.add(spec);
        return this;
    }

    public JpaSpecificationBuilder<T> like(String key, String pattern) {
        if (pattern == null || pattern.trim().length() == 0)
            return this;
        pattern = pattern.trim();
        if (!pattern.contains("%")) {
            pattern = "%" + pattern + "%";
        }
        final String likePattern = pattern;
        Specification<T> spec = (root, cq, cb) -> {
            return cb.like(root.get(key), likePattern);
        };
        specsQueue.add(spec);
        return this;
    }

    public <VT extends Number> JpaSpecificationBuilder<T> gt(String key, VT value) {
        if (value == null)
            return this;
        Specification<T> spec = (root, cq, cb) -> {
            return cb.gt(root.get(key), value);
        };
        specsQueue.add(spec);
        return this;
    }

    public <VT extends Number> JpaSpecificationBuilder<T> ge(String key, VT value) {
        if (value == null)
            return this;
        Specification<T> spec = (root, cq, cb) -> {
            return cb.ge(root.get(key), value);
        };
        specsQueue.add(spec);
        return this;
    }

    public <VT extends Number> JpaSpecificationBuilder<T> lt(String key, VT value) {
        if (value == null)
            return this;
        Specification<T> spec = (root, cq, cb) -> {
            return cb.lt(root.get(key), value);
        };
        specsQueue.add(spec);
        return this;
    }

    public <VT extends Number> JpaSpecificationBuilder<T> le(String key, VT value) {
        if (value == null)
            return this;
        Specification<T> spec = (root, cq, cb) -> {
            return cb.le(root.get(key), value);
        };
        specsQueue.add(spec);
        return this;
    }

    public JpaSpecificationBuilder<T> between(String key, Integer value1, Integer value2) {
        if (value1 == null || value2 == null)
            return this;
        Specification<T> spec = (root, cq, cb) -> {
            return cb.between(root.get(key), value1, value2);
        };
        specsQueue.add(spec);
        return this;
    }

    public JpaSpecificationBuilder<T> between(String key, Long value1, Long value2) {
        if (value1 == null || value2 == null)
            return this;
        Specification<T> spec = (root, cq, cb) -> {
            return cb.between(root.get(key), value1, value2);
        };
        specsQueue.add(spec);
        return this;
    }

    public JpaSpecificationBuilder<T> between(String key, Timestamp value1, Timestamp value2) {
        if (value1 == null || value2 == null)
            return this;
        Specification<T> spec = (root, cq, cb) -> {
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
            return cb.in(root.get(key)).in(filtered);
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
            return root.get(key).in(filtered);
        };
        specsQueue.add(spec);
        return this;
    }

}
