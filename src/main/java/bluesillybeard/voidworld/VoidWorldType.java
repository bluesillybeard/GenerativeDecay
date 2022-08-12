package bluesillybeard.voidworld;

public enum VoidWorldType {
    disabled, //no blocks removed
    distance1, //every other block
    distance2, //every 3 blocks
    distance3, //every 4 blocks
    random25, //25 percent chance
    random50, //50 percent chance
    random75, //75 percent chance
    random90, //90 percent change
    random99, //99 percent chance
    //completeVoid, //always remove, except the block at 0,60, 0 so there is a place to spawn
}
