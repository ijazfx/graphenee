package io.graphenee.security.controller;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxAccessKey;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.security.GxJwtService;
import io.graphenee.security.controller.dto.GxAuthenticationRequest;
import io.graphenee.security.controller.dto.GxAuthenticationResponse;
import io.graphenee.util.ServletUtil;
import io.graphenee.util.hash.TRHashFactory;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/gx")
public class GxAuthenticationController {

    @Autowired
    private GxJwtService jwtService;

    @Autowired
    private GxDataService gxDataService;

    @PostMapping("/authenticate")
    public ResponseEntity<GxAuthenticationResponse> authenticate(@RequestBody GxAuthenticationRequest request,
            HttpServletRequest httpRequest) {

        GxAccessKey accessKey = gxDataService.findAccessKey(UUID.fromString(request.getAccessKey()));
        String secret = request.getSecret();
        Set<String> secretSet = TRHashFactory.getInstance().generateHashForAllProviders(secret);

        /*
         * 1. Validate access key
         * 2. Validate secret key
         * 3. Check if host namespace is available and also equals to user's namespace
         */
        if (accessKey != null && accessKey.getIsActive() && !accessKey.getUserAccount().getIsLocked()
                && (accessKey.getSecret().equals(secret) || secretSet.contains(accessKey.getSecret()))) {

            String username = accessKey.getUserAccount().getUsername() + "@"
                    + accessKey.getUserAccount().getNamespace().getNamespace();
            String token = jwtService.generateToken(username);
            return ResponseEntity.ok().body(new GxAuthenticationResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
