# Android E2 implementation

tdd-scope: required
Gap: E1 has no local data, QR, sync, secondary features and uses nested LinearLayouts.
Hypothesis: preserve Java/Retrofit/brand while introducing SQLite user-scoped repository and WorkManager, then connect screens to frozen backend contract.
Scope: Frontend E2 only; no remote operations.
TDD core: QrPayload strict parsing; SyncPolicy retries, auth and terminal states.
UI layout is verified through build/lint and rendered evidence, not called TDD.
References: official JourneyApps ZXing Embedded (Apache-2.0), Android SQLite and WorkManager docs consulted 08/11/2026.

## Iteration record — Android E2

- Hypothesis: reuse the E1 Java/Retrofit/assets and introduce owner-scoped persistence plus server-authoritative attendance to cover E2 without changing E1.
- Delta: authenticated flows, stable encrypted identity/refresh, ConstraintLayout shell and cards, real course enrollment/agenda, ZXing QR, SQLite queue/cache, WorkManager retry/reconciliation, certificates, quiz, support and notices.
- TDD evidence: initial core stubs produced 6 failing behavior tests; implementation passed 6/6. Added canonical UUID/string-token case produced 1 failure out of 7; implementation passed 7/7. Scope is only QR/retry core; UI/network/SQLite integration is not claimed as TDD.
- Fresh evidence: assembleDebug, testDebugUnitTest, lintDebug and assembleDebugAndroidTest pass. First lint run failed for three missing includeSubdomains attributes; fixed without disabling checks. Initial handoff lint: 0 errors, 83 warnings. Warnings remain for retained E1 assets, available dependency upgrades and minor Android lint recommendations.
- Parent independently ran connectedDebugAndroidTest on Pixel_10a headless: 4 tests passed (3 repository integration tests plus package smoke). No physical-camera or UI interaction claim follows from this result.
- SQLite assertions: persistent reopen, duplicate enqueue, owner isolation for cache/queue/notice state, terminal token clearing, rejected QR rescanning and confirmed deduplication.
- Fingerprint: android-e2-owner-sqlite-qr-v1; package com.example.myapplication; user/activity unique key; parser v1; compileSdk36/min24; frozen Backend/docs/http-contract.md v1.
- Refactor: Java formatting and XML resource extraction after behavior was green; final compilation/testing rerun.
- Remaining gaps: independent reviewer acceptance pending; native UI/large font/camera scanner/certificate-device UX not exercised because Computer Use native permission unavailable; runtime concurrent refresh/lost-response-worker tests are not yet automated. Real deployed E2 and physical Android need the joint test checklist in README.
- External writes: none. No production credentials, Supabase mutation, deploy, push or release.

Sources consulted: https://github.com/journeyapps/zxing-android-embedded ; https://developer.android.com/training/data-storage/sqlite ; https://developer.android.com/develop/background-work/background-tasks/persistent/getting-started/define-work . ZXing Embedded 4.3.0 is Apache-2.0; AndroidX WorkManager 2.9.1 is Apache-2.0. Google Java Format used only as a temporary formatting tool, not an app dependency.

## Repair iteration — stale authentication callback

Reviewer confirmed P1: login A could persist its session on the IO executor after Activity replacement and a successful login B. Hypothesis: a process-wide attempt generation, invalidated by replacement/destruction, plus a lifecycle-checked UI commit prevents stale callbacks from changing persisted identity. RED/GREEN covers out-of-order callbacks, destroyed screens, old destruction versus replacement, and duplicate/finishing callbacks. This is a targeted correction, not acceptance approval.

Repair evidence: AuthAttemptGateTest observed RED 3/4 failures, then GREEN 4/4; FormAndAgendaPolicyTest observed RED 3/3, then GREEN 3/3. Signup minimum now matches backend eight characters without trimming. Home initially selected earliest future using UTC; subsequent reviewer correction below aligns agenda clock with America/Sao_Paulo. Final :app:testDebugUnitTest :app:assembleDebug :app:lintDebug BUILD SUCCESSFUL (7s); 14 behavioral unit tests plus inherited arithmetic smoke pass. Independent reviewer notified for verification; author does not approve own patch.

## Acceptance repair — real Fragment and agenda timezone

Gap confirmed by reviewer: the rubric requires Fragment usage but the initial E2 shell owned RecyclerView directly. Minimal design approved by parent and reviewer before integration: ResourceListFragment owns RecyclerView creation/layout manager and releases adapter/view in onDestroyView; FragmentContainerView instantiates it in the real authenticated shell. Hub keeps data/navigation and reattaches its current adapter. FragmentScenario tests view recreation and old adapter detachment. Also corrected the timezone assumption: backend formats agenda date/time fields in America/Sao_Paulo, despite earlier contract prose saying UTC; boundary test 09/11 08:00 SP versus 10:00Z observed RED.

Final acceptance-repair evidence: instrumented Fragment stub produced RED 1/1 (missing layout manager/adapter); implementation then passed lifecycle recreation, detached old adapter, new adapter attachment and actual activity_hub nested FragmentContainerView instantiation. Full connectedDebugAndroidTest: 6/6 PASS on Pixel_10a (2 Fragment + 3 SQLite + package smoke). Timezone boundary test RED 1/4 → GREEN 4/4. All JVM tests: 15 behavior tests plus inherited smoke pass. Build, lint and complete instrumentation combined BUILD SUCCESSFUL in 24s; final lint has 0 errors and 89 warnings (including available dependency versions and retained resources). Source frozen for independent reviewer on 08/11/2026; native visual/camera inspection remains separate.

Fragment references: https://developer.android.com/guide/fragments/create and https://developer.android.com/guide/fragments/test . AndroidX Fragment1.6.2 explicitly pinned; FragmentScenario and test host manifest are test/debug dependencies only.
