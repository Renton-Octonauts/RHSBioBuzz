package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name = "basic motor test")
public class Shooter extends LinearOpMode {
    private DcMotorEx motor;

    @Override
    public void runOpMode() {
        motor = hardwareMap.get(DcMotorEx.class, "motor");

        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                motor.setVelocity(1120);
                telemetry.update();
            }
        }
    }
}