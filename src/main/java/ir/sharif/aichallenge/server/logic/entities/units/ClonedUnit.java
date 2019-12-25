package ir.sharif.aichallenge.server.logic.entities.units;

public class ClonedUnit extends GeneralUnit {
    private Unit innerUnit;

    private int remainingTurns;

    public ClonedUnit(int unitId, GeneralUnit source, int remainingTurns) {
        super(unitId, source.getBaseUnit(), source.getPlayer(), source.getHealth(), source.getDamage());
        this.remainingTurns = remainingTurns;
    }

    private int getRemainingTurns() {
        return this.remainingTurns;
    }

    public void decreaseRemainingTurns() {
        if (this.remainingTurns == -1) return;
        this.remainingTurns--;
    }

    @Override
    public boolean isAlive() {
        return super.isAlive() && getRemainingTurns() > 0;
    }
}
