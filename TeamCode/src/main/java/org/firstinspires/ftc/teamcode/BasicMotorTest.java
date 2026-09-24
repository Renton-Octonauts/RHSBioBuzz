package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "BasicMotorTest", group = "Linear Opmode")
public class BasicMotorTest extends LinearOpMode {
    private DcMotor motor;

    @Override
    public void runOpMode() {
        motor = hardwareMap.get(DcMotor.class, "motor");


        waitForStart();

        while (opModeIsActive()) {
            motor.setPower(1);
            telemetry.update();
        }
    }
}