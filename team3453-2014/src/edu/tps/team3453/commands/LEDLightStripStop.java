/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tps.team3453.commands;

/**
 *
 * @author digimo
 */
public class LEDLightStripStop extends CommandBase {
    
    public LEDLightStripStop () {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(ledLightStrip);        
    }

    protected void initialize() {
        
    }

    protected void execute() {
        ledLightStrip.off();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        
    }

    protected void interrupted() {
        
    }
    
    
}
