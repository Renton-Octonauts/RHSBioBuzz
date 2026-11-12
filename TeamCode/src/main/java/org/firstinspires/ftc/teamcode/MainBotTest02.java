package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCompatibilityManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "MainBotTest 02")
public class MainBotTest02 extends LinearOpMode {

    private ElapsedTime timer = new ElapsedTime();
    private boolean actionTriggered = false;

    private DcMotorEx flyWheel;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotorEx midIntake;
//    private DcMotor mainIntake;
    private Servo stopper;
    // todo: write your code here

    @Override
    public void runOpMode() {

//    Ignore this I was testing Odometry
//    pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
//    pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_Bar_Pods);
//    pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);

        double Strafe = 0;
        float Drive = 0;
        float Rotate = 0;
        int WheelVelo = 0;
        double ServoPosValue = 0.91;
        int intakeVelo = 0;
        int LanchVeloMin = 1100;
        timer.reset();
        

// Hardware get info stuff

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        flyWheel = hardwareMap.get(DcMotorEx.class, "flyWheel");
        stopper = hardwareMap.get(Servo.class, "stopper");
        midIntake = hardwareMap.get(DcMotorEx.class, "midIntake");
        
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flyWheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        midIntake.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

  //      Only use main intake if we add a motor to the front intake
  //      mainIntake = hardwareMap.get(DcMotor.class, "mainIntake"); 


        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                
                telemetry.addLine("Down Dpad - Keep balls");
                telemetry.addLine("Up Dpad - Shoots balls");
                telemetry.addLine("Right bumper - Stop Intake");
                telemetry.addLine("Left bumper - Spit out balls");
                telemetry.addLine();
                
                //       pinpoint.update();
                //       Pose2D pos = pinpoint.getPosition();

                // Drive code is like mixing up rights and lefts idk how to fix that (I did try making it negitive and that lowk didnt work yall got that)
                frontLeft.setPower(Strafe * 1 + Drive * 1 + Rotate * -1);
                backLeft.setPower(Strafe * 1 + Drive * 1 + Rotate * 1);
                frontRight.setPower(Strafe * 1 + Drive * -1 + Rotate * -1);
                backRight.setPower(Strafe * 1 + Drive * -1 + Rotate * 1);
                Drive = gamepad1.left_stick_y * 1;
                Rotate = gamepad1.left_stick_x * 1;
                Strafe = gamepad1.right_stick_x * -1;

                // AprilTag Stuff

                // Fly Wheel controls (Dpad up, Dpad down)
                if (gamepad1.dpad_up) {
                    timer.reset();
                    WheelVelo = LanchVeloMin;
                    flyWheel.setVelocity(WheelVelo);
                    flyWheel.setPower(1);
                    
                //    intakeVelo = 1150;
                if (flyWheel.getVelocity() >= LanchVeloMin) {
                    flyWheel.setVelocity(WheelVelo);
                    ServoPosValue = 0.5;
                    flyWheel.setPower(1); // Go go flywheel 
                    midIntake.setPower(0.8); //Fast intake
                    stopper.setPosition(ServoPosValue); // Open Pos
                    telemetry.addLine("Shooting");
                }

                }
                if (gamepad1.dpad_down) {
                    WheelVelo = LanchVeloMin - 150;
                    ServoPosValue = 0.91;
            //        intakeVelo = 1150;
                    flyWheel.setVelocity(WheelVelo);
               //     flyWheel.setPower(-0.01); //This is like this because if it isnt then it dosent stop fast enough on bot
                    midIntake.setPower(1); //Slower intake
                    stopper.setPosition(ServoPosValue); // Closed Pos
                    telemetry.addLine("Holding");
                }
                
                telemetry.addData("Current Wheel Velocity", flyWheel.getVelocity());

                // Front Intake Controls

                if (gamepad1.right_bumper) { // graby graby balls should be norm
      //              mainIntake.setPower(1);
                    midIntake.setPower(0);
                } 
                if (gamepad1.left_bumper) { // bady bady balls 
      //              mainIntake.setPower(-1);
                    midIntake.setPower(-1); //Reversy revere
                    telemetry.addLine("Spitting Out Balls");
                    ServoPosValue = 0.91;
                    stopper.setPosition(ServoPosValue);
                }
        //       if (gamepad1.dpad_left) {
        //            stopper.setPosition(0.91);
        //        }
         //       if (gamepad1.dpad_right) {
          //          stopper.setPosition(0.5);
        //        }
        flyWheel.setVelocity(WheelVelo);
       // midIntake.setVelocity(intakeVelo);
        stopper.setPosition(ServoPosValue);

        telemetry.addData("Servo Position", stopper.getPosition());
        if (ServoPosValue == 0.91) {
            telemetry.addLine("The Stopper is closed");
        } else {
            telemetry.addLine("The Stopper is Open");
            
        }
        telemetry.update();
    }
}
}
}
