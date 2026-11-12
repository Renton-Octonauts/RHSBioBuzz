package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCompatibilityManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "MainBotTest03")
public class MainBotTest03 extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;

    private DcMotorEx flyWheel;
    private DcMotorEx midIntake;
    private DcMotorEx mainIntake;
    private Servo stopper;

    @Override
    public void runOpMode() {

        double Strafe = 0;
        float Drive = 0;
        float Rotate = 0;

        boolean ServoOpen = false;
        double ServoPosClosed = 0.2; // Change this Varible to the servos closed position
        double ServoPosOpen = 0; // Change this varible to the Servos opened position 
        int LanchVeloMin = 1150; // This is the velocity of which you want the balls to shoot at
        int StrongMotorPower = 1; // This controls the middle intake and how fast they run (Max Power)(Dont Change)
        double MidMotorPower = 0.8; // This controls the middle intake and how fast they run (Mid Power) (Change this if you want to change shooting intake speed)
        int WeakMotorPower = 0; // This controls the middle intake and how fast they run (No Power)(Dont Change)


// Hardware get info stuff

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        flyWheel = hardwareMap.get(DcMotorEx.class, "flyWheel");
        stopper = hardwareMap.get(Servo.class, "stopper");
        midIntake = hardwareMap.get(DcMotorEx.class, "midIntake");
        mainIntake = hardware.get(DcMotorEx.class, "mainIntake");

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        flyWheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        midIntake.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        mainIntake.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);


        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {

                telemetry.addLine("Down Dpad - Keep balls");
                telemetry.addLine("Up Dpad - Shoots balls");
                telemetry.addLine("Right bumper - Stop Intake");
                telemetry.addLine("Left bumper - Spit out balls");

                telemetry.addLine();
                flyWheel.setPower(StrongMotorPower);

                frontLeft.setPower(Strafe * 1 + Drive * 1 + Rotate * -1);
                backLeft.setPower(Strafe * 1 + Drive * 1 + Rotate * 1);
                frontRight.setPower(Strafe * 1 + Drive * -1 + Rotate * -1);
                backRight.setPower(Strafe * 1 + Drive * -1 + Rotate * 1);
                Drive = gamepad1.left_stick_y * 1;
                Rotate = gamepad1.left_stick_x * 1;
                Strafe = gamepad1.right_stick_x * -1;

                // Fly Wheel controls (Dpad up, Dpad down)


                if (gamepad1.dpad_up) {
                    timer.reset();
                    flyWheel.setVelocity(LanchVeloMin);
                    midIntake.setDirection(DcMotorSimple.Direction.FORWARD);
                    mainIntake.setDirection(DcMotorSimple.Direction.FORWARD);
                    mainIntake.setPower(StrongMotorPower);


                    if (flyWheel.getVelocity() >= LanchVeloMin) {
                        flyWheel.setVelocity(LanchVeloMin);
                        midIntake.setPower(MidMotorPower); //Fast intake
                        //         stopper.setPosition(ServoPosOpen); // Open Pos
                        ServoOpen = true;
                        telemetry.addLine("Shooting");
                    }
                }
                if (gamepad1.dpad_down) {
                    flyWheel.setVelocity(LanchVeloMin - 150);
                    midIntake.setPower(MidMotorPower); //Slower intake
                    mainIntake.setPower(StrongMotorPower);
                    //      stopper.setPosition(ServoPosClosed); // Closed Pos
                    midIntake.setDirection(DcMotorSimple.Direction.FORWARD);
                    ServoOpen = false;
                    telemetry.addLine("Holding");
                }

                telemetry.addData("Current FlyWheel Velocity: ", flyWheel.getVelocity());

                // Front Intake Controls

                if (gamepad1.right_bumper) { // graby graby balls should be norm
                    midIntake.setPower(WeakMotorPower);
                    mainIntake.setPower(WeakMotorPower);
                }
                if (gamepad1.left_bumper) { // bady bady balls 
                    midIntake.setDirection(DcMotorSimple.Direction.REVERSE); //Reversy revere
                    mainIntake.setDirection(DcMotorSimple.Direction.REVERSE);
                    mainIntake.setPower(StrongMotorPower);
                    telemetry.addLine("Spitting Out Balls");
                    stopper.setPosition(ServoPosClosed);
                }

                if (ServoOpen == true) {
                    stopper.setPosition(ServoPosOpen);
                }
                if (ServoOpen == false) {
                    stopper.setPosition(ServoPosClosed);

                }

                telemetry.addData("Servo Position", stopper.getPosition());
                if (stopper.getPosition() == ServoPosClosed) {
                    telemetry.addLine("The Stopper is closed");
                } else {
                    telemetry.addLine("The Stopper is Open");
                }

                telemetry.update();
            }
        }
    }
}
