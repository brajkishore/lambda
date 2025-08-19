## What’s changing?
<!-- 1–2 lines; link to ticket/PRD/issue -->

- Issue / Doc: [JIRA-123]()

## Why?
<!-- User impact, SLO/SLA, security, cost, reliability, or DX -->

## How?
<!-- High-level design; mention data model, APIs, concurrency, storage -->

## Tests
- [ ] Unit tests
- [ ] Integration (DB, queues, HTTP)
- [ ] Failure-path (timeouts, retries, partial failures)
- [ ] Concurrency/race tests
- [ ] Security tests (authz, SQLi/IDOR payloads)
- [ ] Performance baseline (numbers below)

**Evidence / results (paste key outputs):**
unit: 57 tests, all green
perf: P50=12ms, P95=38ms (n=10k), heap +8MB at steady state


## Rollout & Ops
- [ ] Backward compatible (API/DB)
- [ ] Migrations safe (forward-only; rolled out before code)
- [ ] Feature flag / config gate
- [ ] Metrics & logs added (cardinality checked)
- [ ] Alerting thresholds set
- [ ] Runbook updated

## Risk & Blast Radius
- [ ] Data loss potential? Explain mitigation.
- [ ] Security / privacy risk? Redactions in place.
- [ ] Peak load ready? Backpressure story clear.

## Checklist (author self‑review)
- **Correctness:** IDs, equals/hashCode, time, resources, error paths
- **Concurrency:** shared mutables guarded, atomic ops, executor lifecycle
- **Security:** no raw SQL/HTML, secrets not in code/logs, authN/Z enforced
- **Performance:** avoids needless copies/sorts; DB queries indexed & bounded
- **API/Design:** immutable DTOs, clear contracts, validation, pagination
- **Readability:** naming, logging, try-with-resources, formatting consistent

> **Breaking change?** If yes, list dependency owners & comms plan.
