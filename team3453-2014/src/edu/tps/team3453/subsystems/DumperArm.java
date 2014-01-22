/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.tps.team3453.RobotMap;
import edu.tps.team3453.Utils;
import edu.tps.team3453.commands.DumperArmDoNothing;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Madeline
 */
public class DumperArm extends Subsystem {
    private static final Relay dumperArmMotor = new Relay(RobotMap.dumperArmMotor);
    private static final DigitalInput limitSwitchDumperUp = new DigitalInput(2, RobotMap.limitSwitchDumperUp);
    private static final DigitalInput limitSwitchDumperDown = new DigitalInput(2, RobotMap.limitSwitchDumperDown);
    public void initDefaultCommand() {
        setDefaultCommand(new DumperArmDoNothing());
    }
    public void Up(){
        dumperArmMotor.set(Relay.Value.kReverse);
    }
    public void Down(){
        dumperArmMotor.set(Relay.Value.kForward);
    }
    public void Stop() {
        dumperArmMotor.set(Relay.Value.kOff);
    }    
    public boolean isUp () {
        return(Utils.negate(limitSwitchDumperUp.get()));
    }
    public boolean isDown () {
        return(Utils.negate(limitSwitchDumperDown.get()));
    }
}
