package org.firstinspires.ftc.teamcode.Testy_test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


// Basic Autonomous mode for easy points this season

/*
 *  To use pedropathing, use this tutorial for autotuning, this includes what the x y offsets truly are.
 * https://www.youtube.com/watch?v=0MRF2dhb7hs
 * This tutorial is really good, but we would need to use the commons or hazen for this because it needs a lot of space to truly work
 */
@Autonomous(name= "Biobuzz Basic Autonomous")
public class Auto extends LinearOpMode {

    @Override
    public void runOpMode() {
        Hardware hw = Hardware.getInstance(this);
        hw.init(hardwareMap);

        waitForStart();

        while(opModeIsActive()) {
            hw.setPower(1);
            sleep(1000);
            hw.setPower(0);

            telemetry.addData("EZ points right here", hw.flyWheel.getPower());
            telemetry.update();
        }
    }
}