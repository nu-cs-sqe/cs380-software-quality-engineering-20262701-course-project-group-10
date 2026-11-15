package domain;

import org.junit.jupiter.api.Test;

public class AttackTests {
    private static final int FILE_D = 3;
    private static final int RANK_4 = 3;
    private static final Position D4 = new Position(FILE_D, RANK_4);

    // AT01: an empty board has no attackers.
    @Test
    public void AT01EmptyBoardHasNoAttacks() {
        MoveRuleAssertions.assertAttack(new Board(), D4, Color.WHITE, false);
    }
}
