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
import edu.wpi.first.wpilibj.Timer;
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
    private double manualPower = 1.0;
    private final double maxPower = 1.00;
    private final double minRunPower = 0.50;
    private final int fwdDir = -1;
    private final int revDir = 1;
    private Timer timer = new Timer();
    private Timer postTimer = new Timer();
    private Timer catTimer = new Timer();
    private Timer fireTimer = new Timer();
    // rattles the firing pin a bit to make sure it's not sticking while we fire
    private boolean fireRattle = false;
    private boolean lightOn = false;
    private boolean downOverRide = false;
    private boolean donePostWinchWait = false;
       
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private static final SpeedController catapultMotor1 = new Victor(RobotMap.catapultMotor1);
    private static final SpeedController catapultMotor2 = new Victor(RobotMap.catapultMotor2);
    private static final DigitalInput limitSwitchCatapultLatch = new DigitalInput(RobotMap.limitSwitchCatapultLatch);
    private static final DigitalInput limitSwitchCatapultDown = new DigitalInput(RobotMap.limitSwitchCatapultDown);
    private static final DigitalInput limitSwitchCatapultGearbox = new DigitalInput(RobotMap.limitSwitchCatapultGearbox);
    private static final DigitalInput catapultRemoved = new DigitalInput(RobotMap.catapultRemoved);
    
    private static final Relay solenoid = new Relay(RobotMap.catapultSolenoid);
    // catapult solenoid: pos: FiringLatch; neg: GearBox
    private static final Relay light = new Relay(RobotMap.lightSolenoid);
    // light Spike: pos: light
      
    public static class State {

        public final int state;
        public final String value;  
        static final int kSafety_val = 0;
        static final int kWinch_val = 1;
        static final int kReady_val = 2;
        static final int kFire_val = 3;
        static final int kOVERRIDE_val = 10;
        static final int kPOST_WINCH_val = 11;
        static final int kPOST_FIRE_val = 12;
        public static final State kSafety = new State(kSafety_val);
        public static final State kWinch = new State(kWinch_val);
        public static final State kReady = new State(kReady_val);
        public static final State kFire = new State(kFire_val);
        public static final State kOVERRIDE = new State(kOVERRIDE_val);
        public static final State kPOSTWINCH = new State(kPOST_WINCH_val);
        public static final State kPOSTFIRE = new State(kPOST_FIRE_val);

        private State (int state) {
            this.state = state;
            if (state == kSafety_val) {
                value = "Safety";
            } else if (state == kWinch_val) {
                value = "Winch";
            } else if (state == kReady_val) {
                value = "Ready";
            } else if (state == kFire_val) {
                value = "Fire";
            } else if (state == kOVERRIDE_val) {
                value = "OVER RIDE";
            } else if (state == kPOST_WINCH_val) {
                value = "POST WINCH";
            } else if (state == kPOST_FIRE_val) {
                value = "POST FIRE";
            } else {
                value = "Invalid";
            }
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
     
    public void initSubSystem() {
        stop();
        solenoid.set(Relay.Value.kOff);
        setState(State.kSafety);
    }
    
    public void setManualPower (double p) {
        manualPower = p * 0.7;
        if (manualPower < 0.2) {
            manualPower = 0.2;
        }
    }
    
    public double getCurrentOutput () {
        return currentOutput;
    }
    
    public void setState (State state) {
        currentState = state;
        lightOn();
        System.out.println("Catapult setting State to: "+state.value);
        if (state.equals(State.kReady)) {
            stop();
        }
        if (state.equals(State.kSafety)) {
            stop();
            solenoid.set(Relay.Value.kOff);
        }        
        if ((state.equals(State.kWinch)) || (state.equals(State.kOVERRIDE))) {
            stop();
            solenoid.set(Relay.Value.kOff);
            latched = false;
            downOverRide = false;
            catTimer.stop();
            catTimer.reset();
            catTimer.start();
        }
        if (state.equals(State.kPOSTWINCH)) {
            stop();
            postTimer.stop();
            postTimer.reset();
            postTimer.start();
            if (isDownOverRide()) {
                donePostWinchWait = false;
                catTimer.stop();
                catTimer.reset();
                catTimer.start();
            } else {
                solenoid.set(Relay.Value.kReverse);
                setMotor(revDir * 1.0);
            }
        }
        if (state.equals(State.kFire)) {
            stop();
            fireTimer.stop();
            fireTimer.reset();
            fireTimer.start();
            fireRattle = true;
            solenoid.set(Relay.Value.kReverse); 
            if (isDownOverRide()) {
                catTimer.stop();
                catTimer.reset();
                catTimer.start();                
            }
        }
        if (state.equals(State.kPOSTFIRE)) {
            stop();
            downOverRide = false;
            solenoid.set(Relay.Value.kReverse);
            postTimer.stop();
            postTimer.reset();
            postTimer.start();
        }
    }
    
    public State getState () {
        return currentState;
    }
    
    public void setRollerArmState (TopRollerArm.State state) {
        this.rollerArmState = state;
    }
    
    public void dispatch() {
        if (isCatapultRemoved()) {
            stop();
            setState(State.kOVERRIDE);
            runLights();
            return;
        }
        State s = getState();
        System.out.println("Catapult dispatch in State: "+s.value);
        if (s.equals(State.kSafety)) {
            runSafety();
        } else if ((s.equals(State.kWinch) || s.equals(State.kOVERRIDE))) {
            runWinch();
        } else if (s.equals(State.kReady)) {
            runReady();
        } else if (s.equals(State.kFire)) {
            runFire();
        } else if (s.equals(State.kPOSTWINCH)) {
            runPOSTWINCH();
        } else if (s.equals(State.kPOSTFIRE)) {
            runPOSTFIRE();
        }
        runLights();
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

        // stop the winching if winching is more than 1.75s 
        //      or if the catapult down limitswitch is set
        if (isDown() || (catTimer.get() > 1.75)) {
            stop();
//            setState(State.kReady);
            latched = false;
            stop();
            if (catTimer.get() > 1.75) {
                downOverRide = true;
            } else {
                downOverRide = false;
            }
            setState(State.kPOSTWINCH);
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
            setMotor(fwdDir * minRunPower);
            return;
        }
        if ((latched) && (!isLatched())) {
            if (isDown()) {
                latched = false;
                stop();
                setState(State.kPOSTWINCH);
            } else {
                setMotor(fwdDir * minRunPower);
            }
            return;
        } else {
            setMotor(fwdDir * maxPower);
            
        }
        
    }
    
    private void runPOSTWINCH() {
        if (isDownOverRide() && !donePostWinchWait) {
            // wait 1.5s for the catapult to unwind if it was unlatched.
            if (catTimer.get() < 1.50) {
                return;
            } else {
                donePostWinchWait = true;
                catTimer.stop();
                catTimer.reset();
                postTimer.stop();
                postTimer.reset();
                postTimer.start();
            }
        }
        // Wait 0.1 seconds for the catapult arm gearbox to free itself from the shifter
        if ((postTimer.get() > 0.350) || limitSwitchCatapultGearbox.get()) {
//       if ((postTimer.get() > 3.650)) {
            stop();
            postTimer.stop();
            setState(State.kReady);
        } else {
            solenoid.set(Relay.Value.kReverse);
            setMotor(revDir * 1.0);
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
                if (isDownOverRide()) {
                    // if we had overrode the catapult down limitswitch
                    //   wait for 0.50s and assume that catapult latch has released
                    if (catTimer.get() > 0.50) {
                        downOverRide = false;
                        catTimer.stop();
                        catTimer.reset();
                    }
                }
                // cycle thru the firing pin every 0.050 second until the catapult releases
                if (fireTimer.get() > 0.010) {
                    if (fireRattle) {
                        fireRattle = false;
                    } else {
                        fireRattle = true;
                    }
                    fireTimer.stop();
                    fireTimer.reset();
                    fireTimer.start();
                }
                if (fireRattle) {
                    solenoid.set(Relay.Value.kOn);
                } else {
                    solenoid.set(Relay.Value.kReverse);         
                }
                return;
            }
        } else {
            solenoid.set(Relay.Value.kReverse);
            setState(State.kPOSTFIRE);
            return;
        }        
    }
    
    private void runPOSTFIRE() {
        // Wait 2.5 seconds for the catapult arm to settle
        if (postTimer.get() > 1.500) {
            setState(State.kSafety);
            postTimer.stop();
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

    public void runLights() {
        State s = getState();
        System.out.println("Catapult runLights State to: "+s.value + " ; Timer: " + timer.get() );
        if (s.equals(State.kSafety)) {
            if (isLightOn()) {
                if (timer.get() > 0.250) {
                    lightOff();
                }
            } else {
                if (timer.get() > 1.000) {
                    lightOn();
                }
            }            
        }
        if ((s.equals(State.kWinch)) || (s.equals(State.kPOSTWINCH))) {
            if (isLightOn()) {
                if (timer.get() > 0.350) {
                    lightOff();
                }
            } else {
                if (timer.get() > 0.350) {
                    lightOn();
                }
            }           
        }
        if (s.equals(State.kReady)) {
            if (isLightOn()) {
                if (timer.get() > 2.000) {
                    lightOff();
                }
            } else {
                if (timer.get() > 0.250) {
                    lightOn();
                }
            }
        }
        if ((s.equals(State.kFire) || s.equals(State.kPOSTFIRE))) {
            if (isLightOn()) {
                if (timer.get() > 3.000) {
                    lightOff();
                }
            } else {
                if (timer.get() > 0.250) {
                    lightOn();
                }
            }
        }
        if (s.equals(State.kOVERRIDE)) {
            if (isLightOn()) {
                if (timer.get() > 0.150) {
                    lightOff();
                }
            } else {
                if (timer.get() > 0.150) {
                    lightOn();
                }
            }
        }
    }
    
    // speed input into setMotor
    //  postive value moves arm forward
    //  negative value moves arm backward
    public void setMotor(double speed) {
        
        System.out.println("Catapult Calling setMotor: "+speed);
        currentOutput = speed;
        
        // should use limitswitch to stop the setting of the motor here
        
        if (((speed != 0) && (isDown())) && !(this.getState().equals(State.kPOSTWINCH))) {
            // pushing on the joystick and reached limit
            System.out.println("Catapult Hit Stop limit");
            
            stop();
            
        } else {
            catapultMotor1.set( manualPower * speed);
            catapultMotor2.set( manualPower * speed);
            currentOutput = speed * manualPower;
        }
        
//        updateStatus();
    }
    
    public boolean isDown() {
        // Down limit switch is Normally Open
        if (isDownOverRide()) {
            return true;
        }
        return (!limitSwitchCatapultDown.get());
    }
    public boolean isDownOverRide() {
        return downOverRide;
    }
    public boolean isLatched() {
        return (limitSwitchCatapultLatch.get());
    }
    
    public boolean isCatapultRemoved() {
        return (catapultRemoved.get());
    }
    
    public void lightOn() {
        light.set(Relay.Value.kForward);
        lightOn = true;
        timer.reset();
        timer.start();
    }
    public void lightOff() {
        light.set(Relay.Value.kOff);
        lightOn = false;
        timer.reset();
        timer.start();
    }
    public boolean isLightOn() {
        return (lightOn);
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
