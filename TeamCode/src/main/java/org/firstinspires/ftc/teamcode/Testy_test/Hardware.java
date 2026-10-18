package org.firstinspires.ftc.teamcode.Testy_test;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class Hardware {
    private static Hardware self;
    private OpMode opMode;
    GoBildaPinpointDriver odo;
    boolean previousResetButtonState;

    private VoltageSensor vs;

    double currentVS = vs.getVoltage();

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    DcMotor intake;

    DcMotorEx flyWheel;

    AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    private Hardware(OpMode opMode) {
        self = this;
        this.opMode = opMode;
    }

    public static Hardware getInstance(OpMode opMode) {
        if (self == null) self = new Hardware(opMode);
        return self;
    }

    public void init(HardwareMap hardwareMap) {
        odo = hardwareMap.get(GoBildaPinpointDriver.class, "odometry");

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        intake = hardwareMap.get(DcMotor.class, "intakeMotor");
        flyWheel = hardwareMap.get(DcMotorEx.class, "flyWheel");

        vs = hardwareMap.get(VoltageSensor.class, "Control Hub");

        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Odometry computer Config
        // TODO Update these if the robot changes lengths and stuff
        odo.setOffsets(400, 380, DistanceUnit.MM);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        // Keep the robot stationary while the Pinpoint calibrates its IMU
        odo.resetPosAndIMU();
        odo.setPosition(new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.RADIANS, 0));

        // Initialize the AprilTag processor
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();

        // Initialize the VisionPortal with the Logitech webcam named "webcam" and enable live view
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "webcam"))
                .addProcessor(aprilTag)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .enableLiveView(true)
                .build();

        telemetry.addData("Status", "AprilTag initialized with Logitech webcam and live view enabled");
        telemetry.update();
    }

    public void start() {
        visionPortal.resumeStreaming();
    }

    String getBallColor(int id) {
        switch (id) {
            case 1: return "Red";
            case 2: return "Blue";
            case 3: return "Yellow";
            default: return null; // Unknown tag
        }
    }

    public void stop() { visionPortal.close(); }

    public void setPower(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    // might need to update for new robot
    public void strafeLeft(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(-power);
    }

    public void strafeRight(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }

    public double getLeftPower() {
        return frontLeft.getPower() + backLeft.getPower();
    }
}
