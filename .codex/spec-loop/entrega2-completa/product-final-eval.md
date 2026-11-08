# Codex Product Final Eval

- Target: `/Users/luizrs/StudioProjects/Projeto4`
- Started: `2026-11-08T18:19:47Z`
- Finished: `2026-11-08T18:19:49Z`
- Status: `FAIL`
- PASS: `6`
- WARN: `1`
- FAIL: `10`
- INFO: `6`

## Findings

| Status | Area | Message | Evidence |
|---|---|---|---|
| PASS | codebase | Project has a recognizable code manifest | package/pubspec/go/cargo/etc |
| PASS | docs | README exists | README.md |
| PASS | specs | Spec artifacts exist | .codex/spec-loop or openspec/specs |
| FAIL | technical gates | Technical gate report missing | .codex/state/technical-gates-report.md |
| FAIL | security gates | Security gate report missing | /Users/luizrs/StudioProjects/Projeto4/.codex/state/security-gates-report.md |
| INFO | api gates | No API code/schema detected by final eval |  |
| WARN | context gate | Context gate report missing; token/context safety was not evaluated | /Users/luizrs/StudioProjects/Projeto4/.codex/state/context-gate-report.md |
| INFO | agent eval gate | No agent-chain eval config detected by final eval |  |
| PASS | chain cockpit | Active chain cockpit exists | .codex/state/chain-cockpit.md |
| PASS | handoff ledger | Agent handoff ledger has evidence | .codex/state/chain-handoffs/handoffs.jsonl |
| FAIL | routing scorecard | Active chain run exists but routing scorecard is missing | /Users/luizrs/StudioProjects/Projeto4/.codex/state/routing-scorecard.md |
| FAIL | parallel review plan | Active chain run exists but parallel review plan is missing | /Users/luizrs/StudioProjects/Projeto4/.codex/state/parallel-review-plan.md |
| FAIL | chain timeline | Active chain run exists but chain timeline is missing | /Users/luizrs/StudioProjects/Projeto4/.codex/state/chain-timeline.md |
| FAIL | handoff quality | Active chain run exists but handoff quality report is missing | /Users/luizrs/StudioProjects/Projeto4/.codex/state/handoff-quality-report.md |
| FAIL | chain replay | Active chain run exists but chain replay report is missing | /Users/luizrs/StudioProjects/Projeto4/.codex/state/chain-replay-report.md |
| FAIL | acceptance | Acceptance gate report missing | /Users/luizrs/StudioProjects/Projeto4/.codex/state/acceptance-gate-report.md |
| INFO | ui gates | No frontend files detected by final eval |  |
| PASS | human gates | No open human gates | .codex/state/human-gates-report.md |
| FAIL | secrets | Gitleaks is not installed; scanner was skipped | install gitleaks for strict local secret validation |
| FAIL | runbook | Runbook/readiness/handoff docs missing | docs/ |
| INFO | external resources | No external resource records detected |  |
| INFO | notion health | No Agent Driven OS/Notion config detected by final eval |  |
| INFO | final status | Readiness verdict: BLOCKED; ready-for-review is forbidden while any FAIL or open human gate remains | resolve failures and rerun this strict gate |

