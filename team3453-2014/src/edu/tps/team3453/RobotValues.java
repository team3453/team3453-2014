package edu.tps.team3453;
/**
 * The set values of cameraServos, Encoders, etc.
 * @author Alexander
 */
public class RobotValues {
    
    // TopRollerTeleop joystick button values
    public static final int bsuck = 1,
                            bspit = 2,
                            bSetPtAdvance = 6,
                            bSetPtBackup = 7,
                            bArmCatapultOverRide = 3,
                            bCatapultFire1 = 8,
                            bCatapultFire2 = 9;
    
    //Values of angles for camera to turn towards
    public static final int cameraPanServoPyramidApproachAngle = 0,
                            cameraPanServoOnPyramidAngle = 0,
                            
                            cameraTiltServoPyramidApproachAngle = 0,
                            cameraTiltServoOnPyramidAngle = 0;
    
    //Power values for motors to turn clockwise or counterclockwise
    public static final int windowMotorTurnClockwiseFull = 1,
                            windowMotorTurnCountercockwiseFull = -1;
    
}