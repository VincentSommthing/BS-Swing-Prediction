package defaultSwingPredictor;
import utils.VecPair;
import swingPredictor.Swing;

public class VagueSwing implements Swing {
    VecPair enterInfo;
    VecPair exitInfo;

    public VagueSwing(VecPair enterInfo, VecPair exitInfo) {
        this.enterInfo = enterInfo;
        this.exitInfo = exitInfo;
    }

    @Override
    public void mirror() {
        enterInfo.p.x = 3 - enterInfo.p.x;
        exitInfo.p.y = 3 - exitInfo.p.y;
    }

    @Override
    public String toString() {
        return "(" + enterInfo.p.x + ", " + enterInfo.p.y + ") -> (" + exitInfo.p.x + ", " + exitInfo.p.y + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof VagueSwing otherSwing) {
            return enterInfo.equals(otherSwing.enterInfo) && exitInfo.equals(otherSwing.exitInfo);
        }
        return false;
    }
}
