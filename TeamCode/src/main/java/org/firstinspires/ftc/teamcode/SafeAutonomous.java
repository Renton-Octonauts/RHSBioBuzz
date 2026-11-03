package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains a minimal example of a Linear "OpMode". An OpMode is a 'program' that runs
 * in either the autonomous or the TeleOp period of an FTC match. The names of OpModes appear on
 * the menu of the FTC Driver Station. When an selection is made from the menu, the corresponding
 * OpMode class is instantiated on the Robot Controller and executed.
 *
 * Remove the @Disabled annotation on the next line or two (if present) to add this OpMode to the
 * Driver Station OpMode list, or add a @Disabled annotation to prevent this OpMode from being
 * added to the Driver Station.
 */

@Autonomous (name = "Safe Auto Blue Alliance, facing pollen line")

public class BlueAlliancePollenLine extends LinearOpMode {

    static final double FLYWHEEL_POWER = 1 // TODO Calibrate flywheel power for accurate shooting into hive

    private DcMotor flyWheel;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor midIntake;
    private Servo stopper;




    @Override
    public void runOpMode() {

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        flyWheel = hardwareMap.get(DcMotorEx.class, "flyWheel");
        stopper = hardwareMap.get(Servo.class, "stopper");
        midIntake = hardwareMap.get(DcMotor.class, "midIntake");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        if (opModeIsActive()) {
            while (opModeIsActive()) {
                // We are using psuedo-block code to make it easier to debug for newbies. T.Q. please do not be angry at this, you might have a 2nd emergency and cant come to comp and nobody can read ur code.

                //
                forwardMovement();
                stopper.setPosition(0.91);
                midIntake.setPower(1);
                sleep(500);










                // Stop bot
                stopMovement();
                sleep(3000);
                midIntake.setPower(0);
                telemetry.update();
                break;
            }
        }
    }

    /**
     * Stops wheel movement
     */
    private void stopMovement() {
        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        sleep(100);
    }

    /**
     * Moves bot forward
     */
    private void forwardMovement() {
        frontLeft.setPower(-1);
        backLeft.setPower(-1);
        frontRight.setPower(1);
        backRight.setPower(1);
    }

    private void rightMovement() { // TODO double check this if it's truly right
        frontLeft.setPower(-1);
        backLeft.setPower(1);
        frontRight.setPower(-1);
        backRight.setPower(1);
    }

    private void leftMovement() { // TODO double check this if it's truly left
        frontLeft.setPower(1);
        backLeft.setPower(-1);
        frontRight.setPower(1);
        backRight.setPower(-1);
    }
}