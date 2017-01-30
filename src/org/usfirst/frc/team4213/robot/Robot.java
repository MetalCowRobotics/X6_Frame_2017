package org.usfirst.frc.team4213.robot;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.kauailabs.nav6.frc.IMU;
import com.kauailabs.nav6.frc.IMUAdvanced;

import org.team4213.lib14.CowDash;
import org.team4213.lib14.PIDController;
import org.team4213.lib14.Xbox360Controller;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser chooser;
	//RobotDrive myRobot = new RobotDrive(8, 9);
	Talon leftMotor = new Talon(8);
	Talon rightMotor = new Talon(9);
	
	Talon shooterMotor = new Talon(7);
	Xbox360Controller xboxController = new Xbox360Controller(1);
	
	Joystick moveNumber = new Joystick(0);
	Gyro gyro = new ADXRS450_Gyro();
	double oldWheel;
	double sensitivity;
	PIDController PID;
	ScriptEngineManager engineManager = new ScriptEngineManager();

	ScriptEngine engine = engineManager.getEngineByName("nashorn");

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		gyro.calibrate();
		
		chooser = new SendableChooser();
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		PID = new PIDController("Controller", 30, 0, 0, 1);	

	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	public void autonomousInit() {
		
		autoSelected = (String) chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here
			break;
		case defaultAuto:
		default:
			// Put default auto code here
			break;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		
	/*	if (moveNumber.getRawButton(1)) {
			
			//myRobot.arcadeDrive(-1.0, );
		}
		myRobot.arcadeDrive(moveNumber.getRawAxis(1), -moveNumber.getRawAxis(4));
		//myRobot.tankDrive(-moveNumber.getRawAxis(1), -moveNumber.getRawAxis(5));
		 
		
	 */
		
		
	/*	
		double angle = gyro.getAngle() % 360;
		DriverStation.reportError("angle = " + angle, false);
		
		double targetAngle = 90;
		PID.setTarget(targetAngle);
		
		double power = PID.feedAndGetValue(angle);

		DriverStation.reportError("Something :" + power, false);
		myRobot.arcadeDrive(0, -power);
	*/	
		double leftY = -moveNumber.getRawAxis(1);
		double rightY = moveNumber.getRawAxis(5);
		
		leftMotor.set(scale(leftY));
		rightMotor.set(scale(rightY));
		
		DriverStation.reportError("Left : " + leftY, false);
		DriverStation.reportError("Right : " + rightY, false);


		
	}

	public double scale(double wheel){
		wheel =  func(func(func(wheel)));
		
		/*double negInertia = wheel - oldWheel;
		oldWheel = wheel;
		
		double negInertiaAccumulator = 0.0;
	    double negInertiaScalar;
	    
	    if (wheel * negInertia > 0) {
	    	negInertiaScalar = 2.5;
	    } else {
	    	if (Math.abs(wheel) > 0.65) {
	    		negInertiaScalar = 5.0;
	    	} else {
	    		negInertiaScalar = 3.0;
	    	}
        }
	    sensitivity = 0.75;
	    
	
	    double negInertiaPower = negInertia * negInertiaScalar;
	    negInertiaAccumulator += negInertiaPower;

	    wheel = wheel + negInertiaAccumulator;
	    if (negInertiaAccumulator > 1) {
	      negInertiaAccumulator -= 1;
	    } else if (negInertiaAccumulator < -1) {
	      negInertiaAccumulator += 1;
	    } else {
	      negInertiaAccumulator = 0;
	    }*/
	    
	    return wheel;
	}
	private double func(double wheel){
		
		double wheelNonLinearity = 0.5;
		return Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
				/ Math.sin(Math.PI / 2.0 * wheelNonLinearity);
		
		

	}
	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		
		// pseudo code
		//reset gyro
		//move the right wheel thing forwards and backwards or vise versa at 0.5 speed until gyro tells you it is at x degrees
		//move the left wheel thing backwards and forwards or vise versa at 0.5 speed until gyro tells you it is at x degrees
		//if the degrees is off do some math (y-x) and move the answer of that (h) right or left.
		// y-x
		// PID Loop
		
		
		
		System.out.println("CurrentMotorSpeed: "+ shooterMotor.get());
		
		
		if(xboxController.getButton(2)){ //B
			if(shooterMotor.get()<1){
			shooterMotor.set(shooterMotor.get()+.01);
			}
		}
		
		if(xboxController.getButton(1)){ //A
			if(shooterMotor.get()>0){
			shooterMotor.set(shooterMotor.get()-.01);
			}
		}
		
		if(xboxController.getButton(3)){ //x
			shooterMotor.set(0);
		}
		
		
	}
}
