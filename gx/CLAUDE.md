# CLAUDE.md — Graphenee Framework

## How to Navigate This Codebase

File locations and relationships are stored in the **`graph/`** directory as a key-value + dependency graph:

1. **Start with the module graph**: Read `graph/modules.json` to understand which modules are in scope and their dependency edges.
2. **Drill into a module**: Read `graph/<module-id>.json` (e.g. `graph/gx-core.json`) to look up class paths, packages, and intra-module edges.
3. **Impact analysis**: Follow edges in the graph to identify what else may be affected by a change.
4. **Update the graph**: When adding, removing, or moving files, update the relevant node entry in `graph/<module-id>.json`.

Do NOT load the entire graph at once — read only the module files relevant to the current task.

All paths in graph files are relative to the project root: `D:\sandbox\graphenee\gx`

---

## Project Overview

**Graphenee** is an open-source enterprise Java framework for rapid web application development, built on Spring Boot and Vaadin Flow. It provides a ready-made multi-tenant application shell with security, document management, SMS, i18n, and workflow support.

- **Version**: 4.1.1-SNAPSHOT
- **License**: Apache License 2.0
- **Repository**: https://github.com/ijazfx/graphenee

### Key capabilities
- Multi-tenant namespace model with policy-based access control (security groups, policies, statements)
- Vaadin Flow UI component library (grids, forms, dialogs, charts, file upload/download)
- Document management system (DMS) with versioning, tagging, shared links and access grants
- SMS gateway integration (AWS SNS, Twilio, Eocean)
- i18n/localization with per-locale term translations (RTL support)
- jBPM embedded workflow engine integration
- Sawtooth blockchain HTTP client
- AWS integrations (KMS encryption, S3 storage, SNS messaging)
- OwnCloud WebDAV file storage
- JWT-based REST API authentication
- Flyway database migration support
- FFmpeg-based media conversion

---

## Module Map

| Module | Purpose | Graph file |
|--------|---------|------------|
| `gx-util` | Utility classes, callbacks, hashing, file storage, media conversion | `graph/gx-util.json` |
| `gx-flow` | Vaadin Flow base UI components, charts, data converters, event utilities | `graph/gx-flow.json` |
| `gx-core` | Core domain model, services, security, SMS, DMS, i18n, AWS, ML | `graph/gx-core.json` |
| `gx-core-flow` | Vaadin Flow UI views and forms for all gx-core features | `graph/gx-core-flow.json` |
| `gx-jbpm-embedded` | Embedded jBPM workflow engine integration | `graph/gx-jbpm-embedded.json` |
| `gx-jbpm-flow` | Vaadin Flow UI forms for jBPM task management | `graph/gx-jbpm-flow.json` |
| `gx-blockchain` | Sawtooth blockchain HTTP client integration | `graph/gx-blockchain.json` |
| `gx-workshop-flow` | Reference/demo application showing all Graphenee features | `graph/gx-workshop-flow.json` |

Module-level dependency edges: `graph/modules.json`

---

## Technology Stack

| Technology | Version | Usage |
|-----------|---------|-------|
| Spring Boot | 3.5.4 | Core framework, DI, security, scheduling |
| Vaadin Flow | 24.x | Server-side web UI framework |
| React | 18.3.1 | Frontend integration via Vaadin |
| Hibernate / Spring Data JPA | — | ORM and repository layer |
| Flyway | — | Database schema migration |
| jBPM | — | Embedded business process management |
| AWS SDK | — | S3, SNS (SMS), KMS encryption |
| gRPC / Protocol Buffers | 4.32.1 / 1.59.1 | Protobuf definitions for SMS config |
| JWT (jjwt) | — | Stateless REST API authentication |
| Retrofit2 | — | HTTP client for external REST APIs |
| Apache POI | — | Excel/spreadsheet import and export |
| iText PDF | — | PDF generation |
| FFmpeg | — | Audio/video/image format conversion |
| Netty | — | DNS resolution utilities |
| Lombok | — | Boilerplate reduction |
| Node.js / Vite / TypeScript | 5.x | Frontend build toolchain |

---

## Build & Run

```bash
# Compile all modules (skip tests)
mvn-build.bat                          # Windows
./mvn-build.sh                         # Linux/Mac
# Equivalent: mvn -f ./pom.xml -U clean compile -DskipTests

# Deploy to Maven Central (Sonatype)
mvn-deploy.bat                         # Windows
./mvn-deploy.sh                        # Linux/Mac
# Equivalent: mvn -Psonatype -f ./pom.xml deploy -DskipTests

# Run the workshop demo application
cd gx-workshop-flow && mvn spring-boot:run
```

**Key configuration**: `gx-workshop-flow/src/main/resources/application.properties`
(database connection, Flyway settings, file storage type and paths)
