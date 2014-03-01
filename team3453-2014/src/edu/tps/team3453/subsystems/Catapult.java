/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.tps.team3453.RobotMap;
import edu.tps.team3453.commands.CatapultDoNothing;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author digimo
 */
public class Catapult extends Subsystem {

    private double currentOutput;
    
    private State currentState;
    private TopRollerArm.State rollerArmState;
    private boolean stopped = true;
    private boolean latched = false;
    private boolean geatboxlatch = false;
    private double manualPower = 0.7;
    private final double maxPower = 0.5;
    private final double minRunPower = 0.2;
       
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private static final SpeedController catapultMotor1 = new Victor(RobotMap.catapultMotor1);
    private static final SpeedController catapultMotor2 = new Victor(RobotMap.catapultMotor2);
    private static final DigitalInput limitSwitchCatapultLatch = new DigitalInput(RobotMap.limitSwitchCatapultLatch);
    private static final DigitalInput limitSwitchCatapultDown = new DigitalInput(RobotMap.limitSwitchCatapultDown);
    private static final Relay solenoid = new Relay(RobotMap.catapultSolenoid);
    private static final Relay light = new Relay(RobotMap.lightSolenoid);
    
    public static class State {

        public final int state;
        static final int kSafety_val = 0;
        static final int kWinch_val = 1;
        static final int kReady_val = 2;
        static final int kFire_val = 3;
        static final int kOVERRIDE_val = 9;
        public static final State kSafety = new State(kSafety_val);
        public static final State kWinch = new State(kWinch_val);
        public static final State kReady = new State(kReady_val);
        public static final State kFire = new State(kFire_val);
        public static final State kOVERRIDE = new State(kOVERRIDE_val);

        private State (int state) {
            this.state = state;
        }
    }
    
    // Initialize your subsystem here
    public Catapult() {
//        super("Catapult", Kp, Ki, Kd, Kf);

        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
        //leftEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);
        
        //this.getPIDController().setTolerance(new PIDController.AbsoluteTolerance(targetTolerance));
        //setAbsoluteTolerance(new PIDController.AbsoluteTolerance(targetTolerance));
        solenoid.setDirection(Relay.Direction.kBoth);
        light.setDirection(Relay.Direction.kBoth);
        
        solenoid.set(Relay.Value.kOff);
        setState(State.kSafety);
    }    
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new CatapultDoNothing());
    }
     
    public void setManualPower (double p) {
        manualPower = p * 0.7;
        if (manualPower < 0.2) {
            manualPower = 0.2;
        }
    }
    
    public void setState (State state) {
        currentState = state;
    }
    
    public State getState () {
        return currentState;
    }
    
    public void setRollerArmState (TopRollerArm.State state) {
        this.rollerArmState = state;
    }
    
    public void dispatch() {
        State s = getState();
        if (s.equals(State.kSafety)) {
            runSafety();
        } else if ((s.equals(State.kWinch) || s.equals(State.kOVERRIDE))) {
            runWinch();
        } else if (s.equals(State.kReady)) {
            runReady();
        } else if (s.equals(State.kFire)) {
            runFire();
        }
    }
    
    private void runSafety() {
        solenoid.set(Relay.Value.kOff);
        if (isDown()) {
            setState(State.kReady);
            return;
        }
        if ((rollerArmState.equals(TopRollerArm.State.kReady)) || (rollerArmState.equals(TopRollerArm.State.kSafe))) {
            latched = false;
            setState(State.kWinch);
            return;
        }
    }
    
    private void runWinch() {
        solenoid.set(Relay.Value.kOff);
        
        if (isDown()) {
            stop();
            setState(State.kReady);
            return;
        }        
        
        if (rollerArmState.equals(TopRollerArm.State.kDanger)) {
            if (!getState().equals(State.kOVERRIDE)) {
                stop();
                setState(State.kSafety);
                return;
            }
        }
        
        if (isLatched()) {
            latched = true;
            setMotor(minRunPower);
            return;
        }
        if ((latched) && (!isLatched())) {
            if (isDown()) {
                latched = false;
                stop();
                setState(State.kReady);
            } else {
                setMotor(minRunPower);
            }
            return;
        } else {
            setMotor(maxPower);
        }
        
    }
    
    private void runReady() {
        if (isDown()) {
            solenoid.set(Relay.Value.kReverse);
        } else {
            setState(State.kSafety);
            return;
        }        
    }
    
    private void runFire() {
        if (isDown()) {
            if (rollerArmState.equals(TopRollerArm.State.kDanger)) {
                // can't fire right now, arm is in the way
                setState(State.kReady);
            } else {
                solenoid.set(Relay.Value.kOn);
                return;
            }
        } else {
            solenoid.set(Relay.Value.kReverse);
            setState(State.kSafety);
            return;
        }        
    }
    
    public void requestFireState() {
        if (getState().equals(State.kReady)) {
            setState(State.kFire);
            return;
        }
    }
    
    public void requestOverride() {
        setState(State.kOVERRIDE);
    }
    
    // speed input into setMotor
    //  postive value moves arm forward
    //  negative value moves arm backward
    public void setMotor(double speed) {
        
        System.out.println("Catapult Calling setMotor: "+speed);
        currentOutput = speed;
        
        // should use limitswitch to stop the setting of the motor here
        
        if ((speed != 0) && (isDown())) {
            // pushing on the joystick and reached limit
            System.out.println("Catapult Hit Stop limit");
            
            stop();
            
        } else {
            catapultMotor1.set( -1 * manualPower * speed);
            catapultMotor2.set( -1 * manualPower * speed);
            currentOutput = speed * -1 * manualPower;
        }
        
//        updateStatus();
    }
    
    public boolean isDown() {
        return (limitSwitchCatapultDown.get());
    }
    public boolean isLatched() {
        return (limitSwitchCatapultLatch.get());
    }
    
    public void stop() {
        System.out.println("Catapult Called stop");
        catapultMotor1.set(0);
        catapultMotor2.set(0);
        currentOutput = 0;
        setStopped(true);
    }
 
    public void setStopped(boolean k) {
        stopped = k;
    }
    
    public boolean isStopped() {
        return stopped;
    }

    public void updateStatus() {
        SmartDashboard.putNumber("Catapult currentOutput: ",currentOutput);
        System.out.println("Catapult currentOutput: "+currentOutput);
    }    
}
