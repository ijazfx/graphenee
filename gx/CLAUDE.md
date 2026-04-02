# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Graphenee** is a multi-module Java enterprise framework built on Spring Boot 3.5.4 and Vaadin Flow 24.8.4. It provides reusable modules for security, document management (DMS), i18n, jBPM workflow, blockchain integration, and a reference demo application.

- **Version:** `4.1.1-SNAPSHOT`
- **Java:** 21
- **Build:** Maven (multi-module)
- **Distribution:** Published to Maven Central via Sonatype

## Build Commands

```bash
# Build all modules (no tests)
mvn -f ./pom.xml -U clean compile -DskipTests
# or use the provided script:
./mvn-build.sh

# Run the demo application
mvn -pl gx-workshop-flow spring-boot:run

# Deploy to Maven Central (requires GPG key + Sonatype credentials)
mvn -Psonatype -f ./pom.xml deploy -DskipTests
# or use the provided script:
./mvn-deploy.sh

# Build a single module
mvn -f ./gx-core/pom.xml clean compile -DskipTests
```

There is no test suite currently — tests are always skipped in build scripts.

## Module Architecture

Dependency order (leaf → root):

```
gx-util  ←  gx-flow
gx-util  ←  gx-core  ←  gx-core-flow  ←  gx-workshop-flow
gx-util  ←  gx-core  ←  gx-jbpm-embedded  ←  gx-jbpm-flow
gx-blockchain  (standalone, no internal dependents)
```

For full module details, key packages, and inter-module impact notes, read `graph/modules.json`. Per-module package/class graphs are in `graph/<module-id>.json`.

### Module Roles

| Module | Role |
|--------|------|
| `gx-util` | Foundation — hashing, storage, media conversion, JPA specs, callbacks. Changes here ripple everywhere. |
| `gx-flow` | Generic Vaadin Flow UI components and charts. No domain knowledge. Safe to change independently. |
| `gx-core` | Core domain — JPA entities, Spring services, security, DMS, SMS, i18n, AWS/OwnCloud. Entity/service changes ripple to `gx-core-flow`, `gx-jbpm-*`. |
| `gx-core-flow` | Vaadin Flow views/forms for gx-core features (security UI, DMS UI, i18n management). |
| `gx-jbpm-embedded` | Embedded jBPM 6 workflow engine with Spring Boot auto-config. |
| `gx-jbpm-flow` | Vaadin Flow forms for jBPM task actions (approve, reject, skip, assign). |
| `gx-blockchain` | Sawtooth blockchain HTTP client with cryptographic transaction signing (Retrofit). |
| `gx-workshop-flow` | Reference Spring Boot application wiring all modules together. Uses Flyway + H2 (dev) / PostgreSQL (prod at `localhost:5432/graphenee`). |

## Key Architectural Patterns

- **Spring Boot Auto-Configuration:** Each module ships its own auto-config via `spring.factories` — consuming apps just add the JAR as a dependency.
- **Vaadin Flow (Java UI):** UI views and forms are Java classes annotated with `@Route`. The gx-workshop-flow demo also includes a React 18 frontend built with Vite.
- **JPA Entities in gx-core:** All persistent domain objects live under `io.graphenee.core.model.entity`. Service implementations are in `io.graphenee.core.impl`.
- **Protobuf/gRPC:** gx-core generates gRPC stubs from `.proto` files during the `sonatype` Maven profile build.
- **Flyway Migrations:** gx-workshop-flow manages schema evolution under `src/main/resources/db/migration`.

## Running the Demo App (gx-workshop-flow)

The demo app starts on port 8080. The VS Code debug configuration in `.vscode/launch.json` targets `FlowApplication` in `gx-workshop-flow`. Default DB is H2 for development; switch to PostgreSQL by configuring `spring.datasource.*` pointing to `localhost:5432/graphenee`.
