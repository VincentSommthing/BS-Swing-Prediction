package utils;

import java.util.Arrays;
import java.util.stream.Stream;

public abstract class Constants {
    public static final double EPS = 1e-8;

    public static final double UPROT = Math.PI;
    public static final double DOWNROT = 0;
    public static final double LEFTROT = 3/2 * Math.PI;
    public static final double RIGHTROT = 1/2 * Math.PI;
    public static final double UPLEFTROT = 5/4 * Math.PI;
    public static final double UPRIGHTROT = 3/4 * Math.PI;
    public static final double DOWNLEFTROT = 7/4 * Math.PI;
    public static final double DOWNRIGHTROT = 1/4 * Math.PI;

    public static final double[] DIRTOROT = {UPROT, DOWNROT, LEFTROT, RIGHTROT, UPLEFTROT, UPRIGHTROT, DOWNLEFTROT, DOWNRIGHTROT};
    public static final Vec[] DIRTOVEC = Stream.concat(
            Arrays.stream(DIRTOROT).mapToObj(Vec::fromRot),
            Stream.of(new Vec(0.0, 0.0))
        ).toArray(Vec[]::new);
}
