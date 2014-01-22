/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.tps.team3453.RobotMap;
import edu.tps.team3453.commands.LeftArmDoNothing;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author admin
 */
public class LeftArm extends PIDSubsystem {

    private static final double Kp = 0.0;
    private static final double Ki = 0.0;
    private static final double Kd = 0.0;
    private static final double Kf = 0.0;
    
    private static double KoutMin = 0.2;
    private static double KoutMax = 0.25;
    private static double Ksetpoint = 0; //550;
    
    private static final SpeedController leftArm = new Victor(RobotMap.leftArm);
    private static final DigitalInput limitSwitchLeftArmReach = new DigitalInput(2, RobotMap.limitSwitchLeftArmReach);
    private static final DigitalInput limitSwitchLeftArmPull = new DigitalInput(2, RobotMap.limitSwitchLeftArmPull);
    private static final Encoder leftEncoder = new Encoder(RobotMap.leftArmEncoderA,RobotMap.leftArmEncoderB);

    private static double  currentOutput = 0.0;
    private static double  currentRate = 0.0;
    private static double  currentAverageRate = 09.0;
    private static double  rateArray[];
    private static int     rateArrayIndex = -1;
    private static boolean firstTimeRate = true;
    
    private static final double pullMax =     -0.7;
    private static final double pullHigh =    -0.6;
    private static final double pullMidHigh = -0.55;
    private static final double pullMid =     -0.5;
    private static final double pullMidLow =  -0.45;
    private static final double pullLow =     -0.35;
    private static final double pullMin =     -0.30;
    
    private static double currentPull = -0.30;
        
    // Initialize your subsystem here
    public LeftArm() {
        super("LeftArm", Kp, Ki, Kd, Kf);

        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
        
        rateArray = new double[10];
        leftEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);
        setAbsoluteTolerance(50.0);
        
        disable();
        leftEncoder.stop();
        leftEncoder.reset();
        leftEncoder.start();
        setSetpoint(Ksetpoint);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new LeftArmDoNothing());
    }
    
    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
        
        // advance the index and reset it to 0 if we have ran off the highend
        rateArrayIndex++;
        if (rateArrayIndex > 9) {
            rateArrayIndex = 0;
        }
        
        // get the raw rate and store it in the window 
        currentRate = leftEncoder.getRate();
        rateArray[rateArrayIndex] = currentRate;
        
        // prime the entire array with the first measurement if this is the first measurement
        if (firstTimeRate) {
            firstTimeRate = false;
            for (int i = 0; i < 10; i++) {
                rateArray[i] = currentRate;
            }
        }
        
        // calculate the sum and produce the average of the array
        int s = 0;
        for (int i = 0; i < 10; i++) {
            s += rateArray[i];
        }
        currentAverageRate = s / 10.0;
        if (onTarget()) {
            getPIDController().setPID(Kp, Ki, Kd, currentOutput);
        }
        return (currentAverageRate);
    }
    
    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
        if (output > KoutMax) {
            output = KoutMax;
        }
        if (output < KoutMin) {
            output = KoutMin;
        }
        
        currentOutput = output;
        leftArm.set(output);
    }
    
    public void leftArmReach(){
        // changed from 0.3 to 0.335
        leftArm.set(0.335);
    }
    public void leftArmPull(){
        // increased from -0.3 to -0.6
        leftArm.set(currentPull);
    }
    public void stop(){
        disable();
        leftArm.set(0);
    }
    public boolean isExtended() {
       return limitSwitchLeftArmReach.get();
    }
    public boolean isRetracted() {
        return limitSwitchLeftArmPull.get();
    }
    
    public void up5 () {
        currentOutput += 0.05;
        checkOutput();
        printCurrentOutput();
    }
    
    public void down5 () {
        currentOutput -= 0.05;
        checkOutput();
        printCurrentOutput();

    }
    
    public void printCurrentOutput() {
        System.out.println("Left Arm Current Output is "+currentOutput);
    }
    
    public void runCurrentOutput() {
        System.out.println("Run Left Arm Current Output is "+currentOutput);
        leftArm.set(currentOutput);
    }
    
    private void checkOutput() {
        if (currentOutput > 1.0) {
            currentOutput = 1.0;
        } else if (currentOutput < -1.0) {
            currentOutput = -1.0;
        }
    }
    public void updateStatus() {
        SmartDashboard.putNumber("Left Arm Enc count ", leftEncoder.get());
        SmartDashboard.putNumber("Left Arm Motor out ", leftArm.get());
        SmartDashboard.putNumber("Left Arm Enc rate ",  currentRate);        
    }
    
    public void checkRate() {
        
    }
    
    public void setPullMin() {
        currentPull = pullMin;
    }
    public void setPullLow() {
        currentPull = pullLow;
    }
    public void setPullMidLow() {
        currentPull = pullMidLow;
    }
    public void setPullMid() {
        currentPull = pullMid;
    }
    public void setPullMidHigh() {
        currentPull = pullMidHigh;
    }
    public void setPullHigh() {
        currentPull = pullHigh;
    }
    public void setPullMax() {
        currentPull = pullMax;
    }
    
}
