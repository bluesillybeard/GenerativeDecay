package bluesillybeard.generativedecay;

public enum DecayType {
    /**no blocks removed*/
    disabled,
    /**every other block is removed*/
    distance1,
    /**2 blocks between*/
    distance2,
    /**3 blocks between*/
    distance3,
    /**25 percent chance of being removed*/
    random25,
    /**50 percent chance of being removed*/
    random50,
    /**75 percent chance of being removed*/
    random75,
    /**90 percent chance of being removed*/
    random90,
    /**99 percent chance of being removed*/
    random99,
    //Mods for this already exist. One more would simply make things worse.
    //completeVoid,
}
