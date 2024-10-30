package io.graphenee.vaadin.flow.security;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazelcast.core.IMap;

@Service
public class GxHazelCastSessionService {

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    private IMap<String, Boolean> sessionMap;

    public void saveNewSessionForUser(String identifier) {
        // sessionMap.putIfAbsent(identifier, true);
        sessionMap.put(identifier, true, 10, TimeUnit.MINUTES);
    }

    public void removeAllSessionsForUser(String name) {
        List<String> keys = findAllKeysByUsername(name);
        for (String k : keys) {
            if (sessionMap.containsKey(k)) {
                sessionMap.remove(k);
            }
        }
    }

    // Get all keys containing a specific username
    public List<String> findAllKeysByUsername(String username) {
        return sessionMap.keySet().stream()
                .filter(key -> key.contains(username))
                .collect(Collectors.toList());
    }
}
