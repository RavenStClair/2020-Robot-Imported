/*
the current plan with the shooter is to have the Andrew press a button that 
1) fires up the shooters to a speed determined by the people [and prob gets into a posision beforehand]
2)then delays for ~200ms and then actavates the omi to a speed [shouldent take long]
3)delay then starts the belt and will have a predetermaned amount of balls to shoot and will 'sense' when a ball has been fired and repeats with an increacing value till it reaches ball count
*/
package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FinalShooter {
    public static CANSparkMax ShooterMotorTop = new CANSparkMax(RobotMap.SHOOTER_MOTOR_TOP_ID, MotorType.kBrushless);
    public static CANSparkMax ShooterMotorBottom = new CANSparkMax(RobotMap.SHOOTER_MOTOR_BOTTOM_ID, MotorType.kBrushless);
    public static CANEncoder ShooterOneEncoder = new CANEncoder(ShooterMotorBottom);
    public static CANEncoder ShooterTwoEncoder = new CANEncoder(ShooterMotorTop);
    public static double topvelocity = 3500;
    public static double bottomvelocity = 5500;

    public static double kP = 0.0001;
    public static double kFF = 0.000173;
    //Timer to delay until at speed
    static Timer delayer = new Timer();
    // Timer to coordinate unjamming.
    static Timer jamTime = new Timer();
    // Integer for Unjam code
    public static int unjamInt = 0;
    public static boolean SpedUp = false;

    

    public static void trauma() {
        // double FF = 0;

        SmartDashboard.getNumber("p", 0.0001);
        topvelocity = SmartDashboard.getNumber("set top", 0);
        bottomvelocity = SmartDashboard.getNumber("set bottom", 0);
        SmartDashboard.putNumber("timer", delayer.get());

        SmartDashboard.putNumber("SM1V", ShooterOneEncoder.getVelocity());
        SmartDashboard.putNumber("SM2V", ShooterTwoEncoder.getVelocity());
        ShooterMotorTop.getPIDController().setP(kP);
        ShooterMotorBottom.getPIDController().setP(kP);
        ShooterMotorTop.getPIDController().setFF(kFF);
        ShooterMotorBottom.getPIDController().setFF(kFF);
        if (OI.setRefs) {
            Robot.light.setRaw(-200);
            ShooterMotorTop.getPIDController().setReference(topvelocity, ControlType.kVelocity);
            ShooterMotorBottom.getPIDController().setReference(bottomvelocity, ControlType.kVelocity);
        }
        if (OI.Manipulator_Joystick.getPOV() == 270) {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(2);
        } else if (OI.Manipulator_Joystick.getPOV() == 90) {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0);
        }
        double errorShooterRoom = 250;
        if (OI.Manipulator_Joystick.getRawButton(1) == true) {
            ShooterMotorBottom.getPIDController().setReference(bottomvelocity, ControlType.kVelocity);

            ShooterMotorTop.getPIDController().setReference(topvelocity, ControlType.kVelocity);

        }
    
        if (OI.Manipulator_Joystick.getRawButton(2)) {
            // when the trigger is released it stops shooting
            Robot.light.setRaw(0);
            topvelocity = 0;
            bottomvelocity = 0;
            ShooterMotorTop.getPIDController().setReference(topvelocity, ControlType.kVelocity);
            ShooterMotorBottom.getPIDController().setReference(bottomvelocity, ControlType.kVelocity);
            Intake.Indexer.set(ControlMode.PercentOutput, 0);
            Intake.Soubway.set(ControlMode.PercentOutput, 0);
            
        }

        if (OI.quitAll) {
            Intake.Soubway.set(ControlMode.PercentOutput, 0);
            Intake.Indexer.set(ControlMode.PercentOutput, 0);
            Intake.IntakeHigh.set(ControlMode.PercentOutput, 0);
            Intake.IntakeLow.set(ControlMode.PercentOutput, 0);
            ShooterMotorBottom.set(0);
            ShooterMotorTop.set(0);
        }
        
        if (OI.Manipulator_Joystick.getRawButton(5) == true) {
            // turns the belt and indexer forwards
            Intake.Soubway.set(ControlMode.PercentOutput, .6);
            Intake.Indexer.set(ControlMode.PercentOutput, .5);
        } else if (OI.Manipulator_Joystick.getRawButton(3)) {
            // turns the belt and indexer backwards
            Intake.Soubway.set(ControlMode.PercentOutput, -.5);
            Intake.Indexer.set(ControlMode.PercentOutput, -.3);
        } else if (OI.beltRev == false && OI.beltFWD == false
                && OI.shoot == false) {
            // else statement to turn belt off in case of none of the buttons being pressed
            Intake.Soubway.set(ControlMode.PercentOutput, 0);
            Intake.Indexer.set(ControlMode.PercentOutput, 0);
        }
        if (OI.Manipulator_Joystick.getRawButtonPressed(12)) {
            //button to set shooter speeds to long range
            bottomvelocity = 5000;
            topvelocity = 3500;
        }
        if (OI.Manipulator_Joystick.getRawButtonPressed(10)) {
            // button to set shooter speeds to mid range
            bottomvelocity = 4000;
            topvelocity = 2500;
        }
        if (OI.Manipulator_Joystick.getRawButtonPressed(8)) {
            // button to set shooter speeds to short range
            bottomvelocity = 3000;
            topvelocity = 1500;
        }
        if (OI.lightsOn) {
            Robot.light.setRaw(-200);
        } else if (OI.lightsOff && OI.shoot == false) {
            Robot.light.setRaw(0);
        }

    }

    public static void RunGun() {
        ShooterMotorTop.getPIDController().setReference(topvelocity, ControlType.kVelocity);
        ShooterMotorBottom.getPIDController().setReference(bottomvelocity, ControlType.kVelocity);
    }

    // ranges for shooter *UNTESTED*
    public static void LongRange() {
        bottomvelocity = 5500;
        topvelocity = 5500;
    }

    public static void MidRange() {
        bottomvelocity = 5500;
        topvelocity = 3500;
    }

    public static void ShortRange() {
        bottomvelocity = 5500;
        topvelocity = 2000;
    }

    public static void shuteInit() {
        ShooterMotorBottom.setIdleMode(IdleMode.kCoast);
        ShooterMotorTop.setIdleMode(IdleMode.kCoast);
        ShooterMotorTop.restoreFactoryDefaults();
        ShooterMotorBottom.restoreFactoryDefaults();
        bottomvelocity = 0;
        topvelocity = 0;
        ShooterMotorTop.setInverted(true);
        ShooterMotorBottom.setInverted(false);
    }

    /**
     * 
     * @param speed RPM
     */
    public static void shoot(double speed) {
        // basic code to make both shooting motors run a the same speed
        ShooterMotorBottom.set(speed);
        ShooterMotorTop.set(speed);
    }

    /**
     * 
     * @param speedbottom speed of bottom motor RPM
     * @param speedtop speed of top motor RPM
     */
    public static void shootWithSpin(double speedbottom, double speedtop) {
        // function for adding spin to motors.
        ShooterMotorBottom.set(speedbottom);
        ShooterMotorTop.set(speedtop);
    }

    public static void unJam() {
        // Code to unjam motor
        // Work in progress. Do not use until one of us has cleaned it up.
        // "dont think this is a nessesaty" - aidan
        /*s
         * "They still want it though. Like an automated unjam sequence or something. I
         * asked them what they wanted exactly and they did not really know though."-
         * Raven
         */
    }

}
/*
 * (RobotMap.bottomvelocity RobotMap.topvelocity) 1) (5500,2000) 2) (5500, 3500)
 * 3) (5500, 5500)
 */
