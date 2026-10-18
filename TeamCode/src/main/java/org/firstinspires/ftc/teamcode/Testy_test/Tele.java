package org.firstinspires.ftc.teamcode.Testy_test;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.List;

@TeleOp(name = "BioBuzz TeleOp", group = "Linear Opmode")
public class Tele extends LinearOpMode {

    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() {
        Hardware hw = Hardware.getInstance(this);
        hw.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            // Update Pinpoint before reading its pose.
            hw.odo.update();

            // Press A once to reset the field position without repeatedly resetting while held.
            // TODO might need to change this if drivers are accidentally hitting this
            boolean resetButtonPressed = gamepad1.a;
            if (resetButtonPressed && !hw.previousResetButtonState) {
                hw.odo.setPosition(new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.RADIANS, 0));
                hw.odo.update();
            }
            hw.previousResetButtonState = resetButtonPressed;

            double y = -gamepad1.left_stick_y;    // forward/back
            double x = gamepad1.left_stick_x;     // strafe
            double rx = gamepad1.right_stick_x;   // rotate

            double frontLeftPower = y + x + rx;
            double backLeftPower = y - x + rx;
            double frontRightPower = y - x - rx;
            double backRightPower = y + x - rx;

            // Scale all wheel powers together when needed to preserve the requested direction.
            // Compares all power, if all less than 1.0, make to 1.0 (max power)
            double maxPower = Math.max(1.0, Math.max(Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower)),
                    Math.max(Math.abs(frontRightPower), Math.abs(backRightPower))));
            hw.frontLeft.setPower(frontLeftPower / maxPower);
            hw.backLeft.setPower(backLeftPower / maxPower);
            hw.frontRight.setPower(frontRightPower / maxPower);
            hw.backRight.setPower(backRightPower / maxPower);

            // if the battery is below a certain voltage it sets the power of movement to less so we can focus power on intake and fly wheel
            if(hw.currentVS <= 11.8) {
                hw.frontLeft.setPower(frontLeftPower / 2);
                hw.backLeft.setPower(backLeftPower / 2);
                hw.frontRight.setPower(frontRightPower / 2);
                hw.backRight.setPower(backRightPower / 2);
            }

            // intake and flywheel
            if(gamepad1.right_trigger_pressed) {
                // TODO might need to lower this
                hw.flyWheel.setVelocity(6000);
            } else {
                hw.flyWheel.setPower(0);
            }

            // Noodle intake
            if(gamepad1.left_trigger_pressed) {
                hw.intake.setPower(1);
            } else {
                hw.intake.setPower(0);
            }

            /*
             * For Auto ball collection in the future
             */

            // Get current detections
            List<AprilTagDetection> currentDetections = hw.aprilTag.getDetections();
            telemetry.addData("# AprilTags Detected", currentDetections.size());

            /**
             * Detect balls based on color {@link Hardware#getBallColor(int)}
             */
            StringBuilder ballColors = new StringBuilder("Ball colors: ");
            for (AprilTagDetection detection : currentDetections) {
                if(detection instanceof AprilTagSingleDetection) {
                    AprilTagSingleDetection single = (AprilTagSingleDetection) detection;
                    String color = hw.getBallColor(single.id);
                    if (color != null) {
                        ballColors.append(color).append(", ");
                    }

                    if(single.metadata != null) {
                        telemetry.addLine(String.format(
                                "ID %d: %s", single.id, single.metadata.name));
                    } else {
                        telemetry.addLine(String.format(
                                "ID %d: Unknown", single.id));
                    }
                }
            }
            if (ballColors.length() > 13) { // Remove trailing comma and space
                ballColors.setLength(ballColors.length() - 2);
            } else {
                ballColors.append("None detected");
            }
            telemetry.addLine(ballColors.toString());


            Pose2D position = hw.odo.getPosition();
            telemetry.addData("Odometry X", "%.1f mm", position.getX(DistanceUnit.MM));
            telemetry.addData("Odometry Y", "%.1f mm", position.getY(DistanceUnit.MM));
            telemetry.addData("Heading", "%.1f deg", position.getHeading(AngleUnit.DEGREES));
            telemetry.addData("Reset position", "Press A");
            telemetry.addData("Front left power", hw.frontLeft.getPower());
            telemetry.addData("Front right power", hw.frontRight.getPower());
            telemetry.addData("Back left power", hw.backLeft.getPower());
            telemetry.addData("Back right power", hw.backRight.getPower());
            telemetry.update();

            // TODO add april tag telemetry
        }
    }
}