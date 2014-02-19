/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.tps.team3453.RobotMap;
import edu.tps.team3453.commands.TopRollerArmDoNothing;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author digimo
 */
public class TopRollerArm extends PIDSubsystem {
    
    private static final double Kp = 0.7;
    private static final double Ki = 0.0;
    private static final double Kd = 0.0;
    private static final double Kf = 0.0;
    private static final double KoutMin = 0.2;
    private static final double KoutMax = 0.7;
    private static final double Ksetpoint = 340.0;
    private static final double targetTolerance = 15.0;
    private double upperlimit;
    private double outerupperlimit;     // start slowing down motor at outerupperlimit
    private double lowerlimit;
    private double outerlowerlimit;     // start slowing down motor at lowerupperlimit
    private double currentOutput;
    
    private boolean stopped = true;
    private boolean enabled = false;
    
    private final double rollerArmSetPtStow = 40.0;
    private final double rollerArmSetPtUpRight = 285.0;
    private final double rollerArmSetPtSuck = 550.0;
    private final double rollerArmSetPtDown = 970.0;
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private static final SpeedController topRollerArm = new Victor(RobotMap.topRollerArmMotor);
    private static final DigitalInput limitSwitchTopRollerArmReach = new DigitalInput(RobotMap.limitSwitchTopRollerArmReach);
    private static final DigitalInput limitSwitchTopRollerArmPull = new DigitalInput(RobotMap.limitSwitchTopRollerArmPull);
    private static final AnalogChannel rollerArmPot = new AnalogChannel(RobotMap.rollerArmPot);
    
    // Initialize your subsystem here
    public TopRollerArm() {
        super("TopRollerArm", Kp, Ki, Kd, Kf);

        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
        //leftEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);
        
        setEnabled(false);

        setTheSetpoint(Ksetpoint);

        //this.getPIDController().setTolerance(new PIDController.AbsoluteTolerance(targetTolerance));
        //setAbsoluteTolerance(new PIDController.AbsoluteTolerance(targetTolerance));
        
    }    
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new TopRollerArmDoNothing());
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    protected void setEnabled (boolean k) {
        enabled = k;
        if (enabled) {
            enable();
        } else {
            disable();
        }
    }
    
    // speed input into setMotor
    //  postive value moves arm forward
    //  negative value moves arm backward
    public void setMotor(double speed) {
        
        System.out.println("Calling setMotor: "+speed);
        currentOutput = speed;
        
        // should use limitswitch to stop the setting of the motor here
        
        if (isEnabled() && isOnTarget()) {
            System.out.println("We are onTarget");
            off();
        }

        if ((speed < 0) && (limitSwitchTopRollerArmPull.get())) {
            // pulling on the joystick and reached limit
            System.out.println("Hit Pull limit");
            stop();
            
        } else if ((speed > 0) && (limitSwitchTopRollerArmReach.get())) {
            // pushing on the joystick and reached limit
            System.out.println("Hit Reach limit");
            stop();
            
        } else {
            if (isEnabled()) {
                // adjust motor speed to less than 0.5 once the arm
                //   is within outer limits, i.e. 3 * targetTolerance
                double currentPos = returnPIDInput();
                if ((currentPos > outerlowerlimit) && (currentPos < outerupperlimit)) {
                    System.out.println("Within outer limits, slowing to 0.5");
                    if (speed > 0) {
                        speed = 0.5;
                    } else if (speed < 0) {
                        speed = -0.5;
                    }
                }
            }
            // physical positive value to topRollerArm.set pulls arm backwards
            // physical negative value to topRollerArm.set pushes arm forward
            if (speed == 0) {
                setStopped(true);
            } else {
                setStopped(false);
            }
            topRollerArm.set( -1 * 0.7 * speed);
            currentOutput = speed * -1 * 0.7;
        }
        
//        updateStatus();
    }
    
    public void stop() {
        System.out.println("Called stop");
        topRollerArm.set(0);
        currentOutput = 0;
        setStopped(true);
    }

    // disable PID and stop the motors
    public void off() {
        System.out.println("Called off");
        setEnabled(false);
        stop();
        setStopped(true);
    }    
    
    public void setStopped(boolean k) {
        stopped = k;
    }
    
    public boolean isStopped() {
        return stopped;
    }
    
    protected double returnPIDInput() {
        return rollerArmPot.pidGet();
//        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected void usePIDOutput(double p) {
        System.out.println("usePIDOutput: "+p);
        if (p != 0) {
            setMotor(p);
        }
//        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // returnPIDInput returns -3.0 if no Pot attached
    
    // find the current arm position and advance 
    //   into the next position by PID
    public void PIDadvance() {
        double currentPos = returnPIDInput();
        if (currentPos < rollerArmSetPtUpRight) {
            goTo(rollerArmSetPtUpRight);
        } else if (currentPos < rollerArmSetPtSuck) {
            goTo(rollerArmSetPtSuck);
        } else if (currentPos < rollerArmSetPtDown) {
            goTo(rollerArmSetPtDown);
        }
    }
    
    // find the current position and backup
    //   into the next position by PID
    public void PIDbackup() {
        double currentPos = returnPIDInput();
        if (currentPos > rollerArmSetPtSuck) {
            goTo(rollerArmSetPtSuck);
        } else if (currentPos > rollerArmSetPtUpRight) {
            goTo(rollerArmSetPtUpRight);
        } else if (currentPos > rollerArmSetPtStow) {
            goTo(rollerArmSetPtStow);
        }
    }
    
    // sets the setpoint and enable PID
    public void goTo(double pos) {
        setTheSetpoint(pos);
        setEnabled(true);
    }

    // sets the PID setpoint and also calculate the boundary limits
    public void setTheSetpoint (double pos) {
        setSetpoint(pos);
        upperlimit = pos + targetTolerance;
        lowerlimit = pos - targetTolerance;
        outerupperlimit = pos + (3*targetTolerance);
        outerlowerlimit = pos - (3*targetTolerance);
    }
    
    // returns true if current arm position is within tolerance of the setpoint
    public boolean isOnTarget () {
        double currentPos = returnPIDInput();
        return ((currentPos > lowerlimit) && (currentPos < upperlimit));
    }
    
    public void updateStatus() {
        SmartDashboard.putNumber("Arm Position: ", returnPIDInput());
        System.out.println("Arm Position: " + returnPIDInput());
        SmartDashboard.putBoolean("Arm PID Enabled: ", isEnabled());
        System.out.println("Arm PID Enabled: " + isEnabled());
        SmartDashboard.putNumber("upper, lower: "+ upperlimit +",", lowerlimit);
        System.out.println("upper, lower: "+upperlimit +","+lowerlimit);
        SmartDashboard.putNumber("currentOutput: ",currentOutput);
        System.out.println("currentOutput: "+currentOutput);
    }    
}
