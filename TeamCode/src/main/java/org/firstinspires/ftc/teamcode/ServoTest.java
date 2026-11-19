package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCompatibilityManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp (name = "Servo Test", group = "Linear OpMode")
public class ServoTest extends LinearOpMode {
    private Servo stopper;

    @Override
    public void runOpMode() {


// Hardware get info stuff
        stopper = hardwareMap.get(Servo.class, "stopper");


        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {

                // Servo Open
                if (gamepad1.a) {
                    stopper.setPosition(0);
                }
                if (gamepad1.b) {
                    stopper.setPosition(0.8);
                }
                telemetry.update();
            }
        }
    }
}
