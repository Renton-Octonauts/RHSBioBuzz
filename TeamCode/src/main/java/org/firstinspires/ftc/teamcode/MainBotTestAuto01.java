package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "MainBotTestAuto01")
public class MainBotTestAuto01 extends LinearOpMode {

    private DcMotor flyWheel;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor midIntake;
    private Servo stopper;

waitForStart();
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        frontLeft.setPower(0.5);
        backLeft.setPower(-0.5);
        frontRight.setPower(-0.5);
        backRight.setPower(0.5);
        sleep(1000);
        StopMovement();
        telemetry.update();
        break;
      }
    }
  }

  /**
   * Stops wheel movement
   */
  private void StopMovement() {
    backLeft.setPower(0);
    backRight.setPower(0);
    frontLeft.setPower(0);
    frontRight.setPower(0);
    sleep(100);
  }
}
