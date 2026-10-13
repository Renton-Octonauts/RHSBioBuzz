package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;


@TeleOp(name = "Odometry test", group = "Linear Opmode")
public class OdometryTest extends OpMode {

    private GoBildaPinpointDriver odo;
    private boolean previousResetButtonState;

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    @Override
    public void init() {
        odo = hardwareMap.get(GoBildaPinpointDriver.class, "odometry");

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");

        // Odometry computer Config
        // Configure these offsets and directions to match the robot's pod placement and wiring.
        odo.setOffsets(0, 0, DistanceUnit.MM);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        // Keep the robot stationary while the Pinpoint calibrates its IMU.
        odo.resetPosAndIMU();
        odo.setPosition(new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.RADIANS, 0));
    }

    @Override
    public void loop() {

        // Update Pinpoint before reading its pose.
        odo.update();

        // Press A once to reset the field position without repeatedly resetting while held.
        boolean resetButtonPressed = gamepad1.a;
        if (resetButtonPressed && !previousResetButtonState) {
            odo.setPosition(new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.RADIANS, 0));
            odo.update();
        }
        previousResetButtonState = resetButtonPressed;

        double y = -gamepad1.left_stick_y;    // forward/back
        double x = gamepad1.left_stick_x;     // strafe
        double rx = gamepad1.right_stick_x;   // rotate

        double frontLeftPower = y + x + rx;
        double backLeftPower = y - x + rx;
        double frontRightPower = y - x - rx;
        double backRightPower = y + x - rx;

        // Scale all wheel powers together when needed to preserve the requested direction.
        double maxPower = Math.max(1.0, Math.max(Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower)),
                Math.max(Math.abs(frontRightPower), Math.abs(backRightPower))));
        frontLeft.setPower(frontLeftPower / maxPower);
        backLeft.setPower(backLeftPower / maxPower);
        frontRight.setPower(frontRightPower / maxPower);
        backRight.setPower(backRightPower / maxPower);

        Pose2D position = odo.getPosition();
        telemetry.addData("Odometry X", "%.1f mm", position.getX(DistanceUnit.MM));
        telemetry.addData("Odometry Y", "%.1f mm", position.getY(DistanceUnit.MM));
        telemetry.addData("Heading", "%.1f deg", position.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Reset position", "Press A");
        telemetry.addData("Front left power", frontLeft.getPower());
        telemetry.addData("Front right power", frontRight.getPower());
        telemetry.addData("Back left power", backLeft.getPower());
        telemetry.addData("Back right power", backRight.getPower());
        telemetry.update();
    }
}
