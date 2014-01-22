
package edu.tps.team3453;

import edu.tps.team3453.commands.ArmDoNothing;
import edu.tps.team3453.commands.ArmJoystickControl;
import edu.tps.team3453.commands.DumperArmBack;
import edu.tps.team3453.commands.DumperArmForward;
import edu.tps.team3453.commands.LidClose;
import edu.tps.team3453.commands.LidOpen;
import edu.tps.team3453.commands.CameraPyramidApproach;
import edu.tps.team3453.commands.SolenoidLock;
import edu.tps.team3453.commands.SolenoidUnlock;
import edu.tps.team3453.commands.CameraWhileOnPyramid;
import edu.tps.team3453.commands.ArmPull;
import edu.tps.team3453.commands.ArmReach;

import edu.tps.team3453.commands.ClimberChassisBackward;
import edu.tps.team3453.commands.ClimberChassisForward;
import edu.tps.team3453.commands.ClimberChassisJoystickControl;
import edu.tps.team3453.commands.LeftArmDown5;
import edu.tps.team3453.commands.LeftArmUp5;
import edu.tps.team3453.commands.LeftMotorAt10;
import edu.tps.team3453.commands.LeftMotorAt12;
import edu.tps.team3453.commands.LeftMotorAt15;
import edu.tps.team3453.commands.LeftMotorAt20;
import edu.tps.team3453.commands.LeftMotorDown5;
import edu.tps.team3453.commands.LeftMotorUp5;

import edu.tps.team3453.commands.LeftCameraFreeControl;
import edu.tps.team3453.commands.RightCameraFreeControl;
import edu.tps.team3453.commands.ClimberChassisBackward;
import edu.tps.team3453.commands.ClimberChassisForward;
import edu.tps.team3453.commands.ClimberChassisJoystickControl;
import edu.tps.team3453.commands.DriveTeleop;
import edu.tps.team3453.commands.KillSwitch;
import edu.tps.team3453.commands.LeftArmJoystickControl;

import edu.tps.team3453.commands.PanServoClockwise;
import edu.tps.team3453.commands.PanServoCounterClockwise;

import edu.tps.team3453.commands.RearWheelExtend;
import edu.tps.team3453.commands.RearWheelRetract;
import edu.tps.team3453.commands.RightArmDown5;
import edu.tps.team3453.commands.RightArmUp5;
import edu.tps.team3453.commands.RunArmsAtCurrent;
import edu.tps.team3453.commands.TiltServoClockwise;
import edu.tps.team3453.commands.TiltServoCounterClockwise;

import edu.tps.team3453.commands.TestLimitSwitchTesting;
import edu.tps.team3453.commands.RearWheelJoystickControl;
import edu.tps.team3453.commands.RightArmJoystickControl;

import edu.tps.team3453.commands.setTeleopDriveForward;
import edu.tps.team3453.commands.setTeleopDriveReverse;
import edu.tps.team3453.commands.setTeleopDriveSensitiveTrimmed;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.buttons.DigitalIOButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    public static Joystick joystick = new Joystick(1);
    public static Joystick joystick2 = new Joystick(2);
    Button b1 = new JoystickButton (joystick, 1);
    Button b2 = new JoystickButton (joystick, 2);
    Button b3 = new JoystickButton (joystick, 3);
    Button b4 = new JoystickButton (joystick, 4);
    Button b5 = new JoystickButton (joystick, 5);
    Button b6 = new JoystickButton (joystick, 6);
    Button b7 = new JoystickButton (joystick, 7);
    Button b8 = new JoystickButton (joystick, 8);
    Button b9 = new JoystickButton (joystick, 9);
    Button b10 = new JoystickButton (joystick, 10);
    Button b11 = new JoystickButton (joystick, 11);
    Button b12 = new JoystickButton (joystick, 12);
    Button joystick2b1 = new JoystickButton (joystick2, 1);
    Button joystick2b2 = new JoystickButton (joystick2, 2);
    Button joystick2b3 = new JoystickButton (joystick2, 3);
    Button joystick2b4 = new JoystickButton (joystick2, 4);
    Button joystick2b5 = new JoystickButton (joystick2, 5);
    Button joystick2b6 = new JoystickButton (joystick2, 6);
    Button joystick2b7 = new JoystickButton (joystick2, 7);
    Button joystick2b8 = new JoystickButton (joystick2, 8);
    Button joystick2b9 = new JoystickButton (joystick2, 9);
    Button joystick2b10 = new JoystickButton (joystick2, 10);

    public OI(){
        //b3.whenPressed(new ArmReach());
        //b2.whenPressed(new ArmPull());
        
        // Driver - Joystick 1
        // TeleOp Drive
        // Left Camera Free Control
        // KillSwitch
        //b1.whenPressed(new CameraPyramidApproach());
        //b2.whenPressed(new KillSwitch());
        //b3.whenPressed(new KillSwitch());
        b1.whenPressed(new setTeleopDriveForward());
        b1.whenReleased(new setTeleopDriveReverse());
        
        b2.whenPressed(new setTeleopDriveSensitiveTrimmed(true));
        b2.whenReleased(new setTeleopDriveSensitiveTrimmed(false));
        
        //b4.whenPressed(new DriveTeleop());
        //b5.whenPressed(new LeftCameraFreeControl());
        //Joystick Axis- Forward= negative, Backwards= positive for attack3 and extreme3DPro
       
        // Operator - Joystick 2
        // Arm Control
        // Right Camera Free Control
        
        // Climber Chassis Control
        // RearWheel Control
        // Left Arm Control
        // Right Arm Control
        // KillSwitch
        // Camera Pyramid
        // Dumper
        // Lid 
        joystick2b1.whenPressed(new CameraWhileOnPyramid());
        joystick2b2.whenPressed(new KillSwitch());
        joystick2b3.whenPressed(new KillSwitch());
        joystick2b4.whenPressed(new ArmJoystickControl());
        joystick2b5.whenPressed(new RightCameraFreeControl());
        joystick2b6.whenPressed(new ClimberChassisJoystickControl());
        joystick2b7.whenPressed(new RearWheelJoystickControl());
        joystick2b8.whenPressed(new LeftArmJoystickControl());
        joystick2b9.whenPressed(new RightArmJoystickControl());
        
/* For Testing only        
        b6.whenPressed(new DumperArmForward());
        b7.whenPressed(new DumperArmBack());
        b8.whenPressed(new CameraPyramidApproach());
        b9.whenPressed(new CameraWhileOnPyramid());

        
        b10.whenPressed(new LidOpen());
        b11.whenPressed(new LidClose());
        b12.whenPressed(new TestLimitSwitchTesting());
*/
        
/* For Testing only
        joystick2b2.whenPressed(new SolenoidLock());
        joystick2b3.whenPressed(new SolenoidUnlock());
        joystick2b4.whenPressed(new ClimberChassisForward());
        joystick2b5.whenPressed(new ClimberChassisBackward());
        b3.whenPressed(new controlRearWheel());
        // joystick2b6.whenPressed(new RearWheelExtend());
        // joystick2b7.whenPressed(new RearWheelRetract());
        joystick2b8.whenPressed(new ArmPull());
        joystick2b9.whenPressed(new ArmReach());
        
        b4.whenPressed(new PanServoCounterClockwise());
        b5.whenPressed(new PanServoClockwise());
        //b3.whenPressed(new TiltServoClockwise());
*/
        
/* For Testing only 
        joystick2b2.whenPressed(new LeftCameraFreeControl());
        joystick2b3.whenPressed(new SolenoidLock());
        joystick2b4.whenPressed(new SolenoidUnlock());
        joystick2b5.whenPressed(new ClimberChassisForward());
        joystick2b6.whenPressed(new ClimberChassisBackward());
        joystick2b7.whenPressed(new RearWheelJoystickControl());
        joystick2b8.whenPressed(new KillSwitch());
        // joystick2b6.whenPressed(new RearWheelExtend());
        // joystick2b7.whenPressed(new RearWheelRetract());
        joystick2b9.whenPressed(new ArmPull());
        joystick2b10.whenPressed(new ArmReach());
        
        
        b4.whenPressed(new PanServoCounterClockwise());
        b5.whenPressed(new PanServoClockwise());
        b3.whenPressed(new TiltServoClockwise());

        //b2.whenPressed(new TiltServoCounterClockwise());
        b2.whenPressed(new ClimberChassisJoystickControl());
 */
        // Arm diagnostics
        /*
        b5.whenPressed(new ArmDoNothing());
        b6.whenPressed(new RunArmsAtCurrent());
        b7.whenPressed(new LeftArmUp5());
        b8.whenPressed(new LeftArmDown5());
        b9.whenPressed(new RightArmUp5());
        b10.whenPressed(new RightArmDown5());
        * */
    }
    
    //// CREATING BUTTONS
    
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // Another type of button you can create is a DigitalIOButton, which is
    // a button or switch hooked up to the cypress module. These are useful if
    // you want to build a customized operator interface.
    // Button button = new DigitalIOButton(1);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
}

