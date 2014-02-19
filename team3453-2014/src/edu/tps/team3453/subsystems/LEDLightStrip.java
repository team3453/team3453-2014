/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tps.team3453.subsystems;

import edu.tps.team3453.RobotMap;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Relay;
import edu.tps.team3453.commands.LEDLightStripStop;

/**
 *
 * @author digimo
 */
public class LEDLightStrip extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private static final Relay light = new Relay(RobotMap.ledLighStrip);

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new LEDLightStripStop());
    }
    
    
    public void off() {
        light.set(Relay.Value.kOff);
    }
    
    public void on() {
        light.set(Relay.Value.kOn);
    }
}
