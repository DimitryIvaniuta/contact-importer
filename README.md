# Contact Importer Microservice

This is a **professional-grade, scalable, and fault-tolerant microservice** built using **Java 21**, **Spring Boot 3**, **Gradle**, and **Spring Batch**, designed to import very large CSV files (e.g., up to 100MB+) asynchronously and in parallel.

## Key Features

###  CSV Import via Streaming & Multithreading
- Uses **uniVocity CSV parser** for fast, streaming-based parsing (not memory-bound)
- Supports parsing CSV headers dynamically and mapping fields accordingly
- Runs processing in **parallel threads** using a configurable executor
- Uses **Spring Batch** for chunk-based processing, retries, and checkpointing

###  Asynchronous Partitioned Import
- Each large CSV file is **partitioned and imported in parallel**
- **CompletableFutures** are used per partitioned chunk
- Threads execute in isolation so one failure won't stop others

###  Import Resilience and Fault-Tolerance
- Failed threads do **not halt** other ongoing imports
- Faulted records are stored separately as unimported with original content
- Errors are logged for post-import inspection and possible re-processing

###  PostgreSQL Optimized
- DB inserts are optimized with **Hibernate batch insert settings**:
    - `hibernate.jdbc.batch_size=50`
    - `hibernate.order_inserts=true`
- **Partitioned inserts** ensure memory-efficient batch commits

###  Flyway-Based Database Migration
- All schema changes are managed via **Flyway migrations** under `db/migration`
- Includes tables for:
    - `contacts`
    - `import_files`
    - `import_statistics`
    - `unimported_contacts` (if needed)

###  Metrics & Observability
- Exposes metrics via **Spring Boot Actuator**
- Custom counters per import execution:
    - Records processed
    - Records failed
    - Job status

###  Dynamic Mapping from CSV Headers
- Fields from the CSV are read from **first line headers**
- Mapping to `Contact` entity is unified, dynamic, and extensible

###  Unimported Data Storage
- If a record can't be mapped or saved, it is stored with the **original CSV values**
- Saved as a **`JSONB`** field (`Map<String, String>`) in the database
- Provides full traceability of failed rows

###  File Upload and Import Triggering
- Multiple CSV files can be uploaded
- Each is **processed in isolated asynchronous batches**
- File metadata is saved to a `files` table
- Unique import execution tracked by `import_id`

---

## Tech Stack
- **Java 21**
- **Spring Boot 3**
- **Spring Batch**
- **Spring Data JPA** (Hibernate)
- **PostgreSQL** with JSONB
- **Gradle**
- **Flyway**
- **Actuator + Micrometer**
- **uniVocity CSV Parser**

---

## Developer Features
- Docker Compose for PostgreSQL setup
- Profiles for local/dev environments
- Import jobs can be monitored in `/actuator/batch/jobs` (if enabled)
- Logs detail errors with line number, exception, and reason

---

## Supported File Format
CSV file must include headers on the **first line**. Supported fields include:
- Office ID, User ID, User Name, Full Name, etc.
- Spouse info, Address, Phones, Emails
- Dates: Birthday, Anniversary, Move Date
- Real Estate preferences: Bedrooms, Pool, Sq. Feet, etc.

---

## Import Statistics API
Import execution tracks:
- Total records
- Successful records
- Failed records
- Skipped due to duplication or validation

A REST endpoint returns this data by import ID.


## License

This project is licensed under the [MIT License](LICENSE).

---

## Contact

**Dzmitry Ivaniuta** — [diafter@gmail.com](mailto:diafter@gmail.com) — [GitHub](https://github.com/DimitryIvaniuta)
