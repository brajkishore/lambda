# Reviewer Checklist (Java-focused, FAANG-style)

Use this alongside the PR diff. Time-box to 10 minutes.

## 0) Triage (1 min)
- [ ] PR description links to ticket/spec/tests
- [ ] Scope is focused; flags/rollout plan present

## 1) Correctness (2 min)
- [ ] Identity safe: atomic IDs; equals/hashCode consistent; value types immutable
- [ ] Time sane: `Instant`/`OffsetDateTime`, injected `Clock`, ISO-8601 at boundaries
- [ ] Resources closed: try-with-resources for JDBC/streams; bounded pools
- [ ] Errors useful: no swallowed exceptions; actionable messages; retries bounded

## 2) Concurrency (2 min)
- [ ] No shared mutable without guards; use `ConcurrentHashMap`, COW, immutables
- [ ] Atomicity: no check-then-act; use `computeIfAbsent`/atomics; minimal lock scope
- [ ] Executor lifecycle: named, bounded queue, proper `shutdown`/`awaitTermination`
- [ ] Visibility: immutable hand-off or volatile; no publication races

## 3) Security & Privacy (1 min)
- [ ] Parametrized SQL/HTTP; output encoding; validate inputs
- [ ] Secrets via config, not code/logs; rotated; least-priv DB users
- [ ] AuthN/AuthZ enforced: ownership & scopes checked on every entry point
- [ ] Logs redacted: no PII/secrets; structured logging with context keys

## 4) Performance (1 min)
- [ ] Avoid extra copies/sorts; sized collections; streaming where safe
- [ ] DB queries bounded (`LIMIT`, specific columns); indexes match predicates
- [ ] No N+1; batching or bulk ops used; backpressure in async flows
- [ ] Memory steady-state acceptable; cache has eviction policy

## 5) API / Design (1 min)
- [ ] Contracts clear: nullability documented; `Optional` for absence
- [ ] Immutability & encapsulation: no leaking internal collections
- [ ] Pagination & ordering deterministic; max page size enforced
- [ ] Versioning strategy; DTOs decoupled from persistence

## 6) Testing (1 min)
- [ ] Tests cover boundaries & failure paths; concurrency tests where relevant
- [ ] Security testcases (SQLi/IDOR); logs snapshot test for redactions
- [ ] Integration via Testcontainers; TZ tests for persistence
- [ ] Perf baseline captured for hot path

## 7) Readability / Style (1 min)
- [ ] Names intention-revealing; small focused methods
- [ ] try-with-resources; `DateTimeFormatter` not `SimpleDateFormat`
- [ ] Structured logs; correct levels; no noisy debug
- [ ] Format/lint clean; nullability annotations consistent

---

## Severity Rubric
- **P0 Blocker:** correctness/security/concurrency bug; data loss/leak; API break
- **P1 High:** perf risk; flaky behavior; missing tests on core path
- **P2 Medium:** readability/maintainability issues; minor inefficiency
- **P3 Nit:** style nits; optional refactors

---

## Comment Template (copy/paste)

**Context:** _why it matters (user/SLO/security)_

**Issue:** _quote snippet or line refs_

**Evidence:** _crash/race/spec mismatch or repro steps_

**Fix:** _concrete suggestion/code snippet_

**Scope:** _tests/docs/metrics to add_

> **Severity:** P0 / P1 / P2 / P3

---

## Bonus: Reviewer Pocket Snippets (paste into comments fast)

**Atomic ID + immutable DTO**
```java
private final AtomicLong idGen = new AtomicLong(1);
public record Tweet(long id, String userId, String text, Instant createdAt) {}
// usage:
long id = idGen.getAndIncrement();```
**Try-with-resources (JDBC) + parameterized SQL**
```private static final String INS =
    "INSERT INTO tweets(id,user_id,text,created_at) VALUES (?,?,?,?)";

try (PreparedStatement ps = conn.prepareStatement(INS)) {
  ps.setLong(1, id);
  ps.setString(2, userId);
  ps.setString(3, text);
  ps.setObject(4, OffsetDateTime.now(clock)); // stores TZ-aware timestamp
  ps.executeUpdate();
}```
**Pagination contract (deterministic ordering + max page size)**
```public interface TweetRepo {
  Page<Tweet> list(@Nullable String cursor, @Positive @Max(200) int limit);
}
// Page: items + nextCursor; order by (created_at DESC, id DESC) to ensure stability
```
**Clock injection for testability**
```public final class TweetService {
  private final Clock clock;
  public TweetService(Connection c, Clock clock) { this.conn = c; this.clock = clock; }
  public Tweet publish(...) { Instant now = Instant.now(clock); ... }
}
// test: new TweetService(conn, Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneOffset.UTC))
```
**Structured logging with PII redaction**
```// using a typical slf4j logger
log.info("tweet_published id={} userIdHash={} len={}",
         id, Integer.toHexString(userId.hashCode()), text.length());
// Never log raw tokens/emails/phones/payloads.
```
**Concurrent map write without check-then-act**
```// ensure single creation per key under concurrency
Tweet t = TWEETS.computeIfAbsent(id, k -> createTweet(k, userId, text, now));
```
**Executor lifecycle & bounded queue**
```ExecutorService exec = new ThreadPoolExecutor(
    8, 8, 60L, TimeUnit.SECONDS,
    new ArrayBlockingQueue<>(1000),
    new ThreadFactoryBuilder().setNameFormat("tweet-writer-%d").build(),
    new ThreadPoolExecutor.CallerRunsPolicy());
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
  exec.shutdown();
  try { exec.awaitTermination(30, TimeUnit.SECONDS); } catch (InterruptedException ignored) {}
}));
```
**Validation annotations (Bean Validation)**
```public record CreateTweetRequest(
    @NotBlank @Size(max = 280) String text,
    @NotBlank String userId
) {}
```
**IDOR guard (authorization check)**
```if (!Objects.equals(authenticatedUserId, requestedUserId)) {
  throw new ForbiddenException("not your resource");
}
```
**Basic concurrency test harness**
```int n = 1000;
ExecutorService pool = Executors.newFixedThreadPool(16);
CountDownLatch start = new CountDownLatch(1);
Set<Long> ids = ConcurrentHashMap.newKeySet();

for (int i = 0; i < n; i++) {
  pool.submit(() -> {
    start.await();
    Tweet t = service.publish("u1", "hi");
    ids.add(t.id());
    return null;
  });
}
start.countDown();
pool.shutdown();
pool.awaitTermination(30, TimeUnit.SECONDS);
assertEquals(n, ids.size(), "IDs must be unique under concurrency");
```



