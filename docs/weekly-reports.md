# Week 3 (10/12/2026-10/18/2026)
**Planning and Progress Tracking**:
1. Didn't have admin permission for setting up branch protection, Github Actions and finish the following steps.

# Week 4 (10/19/2026-10/25/2026)
**Decisions**:
- Application: Chess, built as a GUI application with **Java Swing** (same library as the reference starter code).
- First feature: **Game Setup Phase** ([requirements](requirements/game-setup.md), [design](design/game-setup.md)).
- Game rules documented in [requirements/game-rules.md](requirements/game-rules.md).

**Planning and Progress Tracking**:
1. [done] Youran Ma: Gradle CI and build status badge ([PR #1](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/pull/1))
2. [done] Mengpang Xing: Game rules, Game Setup user story / use case, and design docs ([PR #3](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/pull/3))

# Week 5 (10/26/2026-11/01/2026)
**Planning and Progress Tracking**:
1. [done] Mengpang: Add piece types, colors, and piece tests ([PR #9](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/pull/9))
2. [done] Yurou Quan: Implement Board functionality and complete BVA tests; update Board documentation and configure UTF-8 encoding ([PR #12](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/pull/12))
3. [done] Youran Ma: Implement Position with bounds checking, equality, BVA, and tests ([PR #13](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/pull/13))
4. [done] Naman Dua: Game (TDD, BVA G01–G06) and Swing GUI (Main, ChessFrame, BoardPanel), Board/Position stubbed until merged ([PR #11](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/pull/11))

# Week 6 (11/2/2026-11/8/2026)
**Planning and Progress Tracking**:
1. [done] Mengpang Xing: One Turn requirements (user story, acceptance criteria, use case) and design docs ([PR #15](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/pull/15))
2. [done] Naman Dua: Click-to-move GUI (BoardClickHandler with TDD + BVA, BoardPanel, ChessFrame turn label and error dialog), makeMove stubbed until the make-move task merges ([PR #23](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/pull/23))
3. [done] Mengpang Xing: Set up Checkstyle and SpotBugs and clean up findings ([PR #25](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/pull/25).
   - Baseline: 1,237 Checkstyle violations (1,236 indentation and one line length) and one SpotBugs finding.
   - Converted Java source and tests to tabs and wrapped long lines, including lines made longer by Checkstyle's tab expansion. Kept separate lowercase commits for each file and rule, with follow-up fixes recorded separately.
   - Reviewed `Game.getBoard()` against the design and BVA G01/G06. Added a method-level `EI_EXPOSE_REP` suppression because the GUI must share the supplied board across resets; documented the exception in the README. Added SpotBugs annotations as a compile-only dependency.
   - Validation: all 110 tests pass with none skipped; Checkstyle and SpotBugs report zero findings in main and test sources. A clean build exposed one final annotation line-length issue; the subsequent build passed after correcting it. No game behavior or test expectations changed.
4. [done] Mengpang Xing: Implement Pawn, Knight, and King move rules with BVA and tests ([PR #32](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/pull/32)).
5. [done] Youran Ma: Implement Rook, Bishop, and Queen move rules with BVA and tests ([PR #30](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/pull/30)).
6. [done] Yurou Quan: Implement Board.movePiece and Game.makeMove (turn rules and captures) with BVA and tests ([PR #37](https://github.com/nu-cs-sqe/cs380-software-quality-engineering-20262701-course-project-group-10/pull/37))
   - Board.movePiece with capture support; Game.makeMove with turn rules, legality and own-piece capture checks (BOARD-MOVE-01 to 11, G07 to G18).
   - Replaced magic numbers in Board with named constants.



# Week 7 (11/9/2026-11/15/2026)
**Planning and Progress Tracking**:
1. [not started] Person: Task (Links to PR)
2. [not started] Person: Task (Links to PR)
3. [80% done] Person: Task (Links to PR)
4. etc.

# Week 8 (11/16/2026-11/22/2026)
**Planning and Progress Tracking**:
1. [not started] Person: Task (Links to PR)
2. [not started] Person: Task (Links to PR)
3. [80% done] Person: Task (Links to PR)
4. etc.



# Week 9 (11/23/2026-11/29/2026)
**Planning and Progress Tracking**:
1. [not started] Person: Task (Links to PR)
2. [not started] Person: Task (Links to PR)
3. [80% done] Person: Task (Links to PR)
4. etc.


# One section per week until the last week of class (the week before the Finals week)


# Week X (XX/XX/2026-XX/XX/2026) TEMPLATE (You can change the format to whatever the team likes better)
**Planning and Progress Tracking**:
1. [done] Person: Task (Links to PR)
2. [not started] Person: Task (Links to PR)
3. [80% done] Person: Task (Links to PR)
