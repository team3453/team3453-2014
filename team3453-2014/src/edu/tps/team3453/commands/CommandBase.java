package edu.tps.team3453.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.tps.team3453.OI;
import edu.tps.team3453.subsystems.LEDLightStrip;
import edu.tps.team3453.subsystems.LeftDriveMotor;
import edu.tps.team3453.subsystems.RightDriveMotor;
import edu.tps.team3453.subsystems.TopRoller;
import edu.tps.team3453.subsystems.TopRollerArm;
import edu.tps.team3453.subsystems.PanServo;
import edu.tps.team3453.subsystems.TiltServo;
import edu.tps.team3453.subsystems.TestLimitSwitch;
import edu.tps.team3453.subsystems.LeftJoystickToken;
import edu.tps.team3453.subsystems.RightJoystickToken;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    public static final LeftDriveMotor leftDriveMotor = new LeftDriveMotor();
    public static final RightDriveMotor rightDriveMotor = new RightDriveMotor();
    public static final LEDLightStrip ledLightStrip = new LEDLightStrip();
    public static final TopRoller topRoller = new TopRoller();
    public static final TopRollerArm topRollerArm = new TopRollerArm();
    public static final TiltServo tiltServo = new TiltServo();
    public static final PanServo panServo = new PanServo();
    public static final LeftJoystickToken leftJoystickToken = new LeftJoystickToken();
    public static final RightJoystickToken rightJoystickToken = new RightJoystickToken(); 
    
    public static final TestLimitSwitch testLimitSwitch = new TestLimitSwitch();
    
    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

        // Show what command your subsystem is running on the SmartDashboard
        //updateStatus();
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
    
    public static void updateStatus() {
        SmartDashboard.putData(leftDriveMotor);
        SmartDashboard.putData(rightDriveMotor);
        
    }
}
