package bluesillybeard.generativedecay;

public enum DecayType {
    disabled, //no blocks removed
    distance1, //every other block
    distance2, //every 3 blocks
    distance3, //every 4 blocks
    random25, //25 percent chance
    random50, //50 percent chance
    random75, //75 percent chance
    random90, //90 percent change
    random99, //99 percent chance
    //Mods for this already exist. One more would simply make things worse.
    //completeVoid,
}
