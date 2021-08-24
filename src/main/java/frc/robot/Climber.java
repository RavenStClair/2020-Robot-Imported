/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * Add your docs here.
 */
public class Climber {
    public static CANSparkMax fishingPole = new CANSparkMax(18, MotorType.kBrushed);
    public static VictorSPX ClimberS = new VictorSPX(RobotMap.CLIMBER_S_ID);
    public static VictorSPX ClimberF = new VictorSPX(RobotMap.CLIMBER_F_ID);

    public static Boolean failSafe = true; // failsafe int
    public static boolean climberButton = false;
    static int ShooterSwitchButtons;

    static final double MAX_CURRENT = 30;

    public static void Climb() {

        if (failSafe == true && OI.Right_Joystick.getRawButton(9) && !OI.Right_Joystick.getRawButton(6)
                && climberButton == true) {
            // check forward
            ClimberF.set(ControlMode.PercentOutput, -RobotMap.CLIMBER_MOTOR_SPEED);
            ClimberS.set(ControlMode.PercentOutput, RobotMap.CLIMBER_MOTOR_SPEED);
        } else if (failSafe == true && !OI.Right_Joystick.getRawButton(9) && OI.Right_Joystick.getRawButton(6)
                && climberButton == true) {
            // check reverse
            ClimberF.set(ControlMode.PercentOutput, RobotMap.CLIMBER_MOTOR_SPEED);
            ClimberS.set(ControlMode.PercentOutput, -RobotMap.CLIMBER_MOTOR_SPEED);
        } else {
            // stop if not true
            ClimberF.set(ControlMode.PercentOutput, 0);
            ClimberS.set(ControlMode.PercentOutput, 0);
        }
        if (OI.Manipulator_Joystick.getRawButton(11) == true) {
            // Safety Lock
            climberButton = true;
        } else {
            climberButton = false;
        }
        if (OI.Manipulator_Joystick.getRawButton(9)) {
            // Hook Down
            fishingPole.set(0.7);
        } else if (OI.Manipulator_Joystick.getRawButton(7)) {
            // Hook up
            fishingPole.set(-0.7);
        } else if (OI.Manipulator_Joystick.getRawButton(9) == false && OI.Manipulator_Joystick.getRawButton(11) == false
                && OI.Manipulator_Joystick.getRawButton(1) == false) {
            fishingPole.set(0);
        }

    }

}
