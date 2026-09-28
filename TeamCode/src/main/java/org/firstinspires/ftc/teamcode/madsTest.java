package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCompatibilityManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.usb.UsbConstants;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

@TeleOp(name = "madsTest")
public class madsTest extends LinearOpMode {

// THIS CODE IS NOT TESTED DONT IMMIDETELY RUN ALL OF THIS ON BOT


//  GoBildaPinpointDriver pinpoint;
// Servo import lowk might not work ts did NOT auto finish itself when I put it in


    //Webcam (Plus Apriltag Processor)(we get range from the telemetry april tag func)

    private static final boolean USE_WEBCAM = true;
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;
    static final int VENDOR_ID = 046D; //this is for future ref incase we need to use CamCompManager
    static final int PRODUCT_ID = 0825;

    // Motors

    private DcMotor flyWheel;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor mainIntake;
    private DcMotor middleIntake;

    // Servos

    private Servo intakeFrontLeft;
    private Servo intakeFrontRight;
    private Servo ballBlock;

    @Override
    public void runOpMode() {

//    Ignore this I was testing Odometry
//    pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
//    pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_Bar_Pods);
//    pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);

        double Strafe = 0;
        float Drive = 0;
        float Rotate = 0;

// Hardware get info stuff

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        flyWheel = hardwareMap.get(DcMotor.class, "flyWheel");
        mainIntake = hardwareMap.get(DcMotor.class, "mainIntake");
        middleIntake = hardwareMap.get(DcMotor.class, "middleIntake");

        intakeFrontLeft  = hardwareMap.get(Servo.class, "intakeFrontLeft");
        intakeFrontRight = hardwareMap.get(Servo.class, "intakeFrontRight");
        ballBlock = hardwareMap.get(Servo.class, "ballBlock");

        CameraCompatibilityManager.getInstance();
        initAprilTag();
        telemetry.addData("DS preview on/off", "Camera Stream");
        telemetry.addData(">", "Touch START to start OpMode");
        telemetry.update();


        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                //       pinpoint.update();
                //       Pose2D pos = pinpoint.getPosition();

                // Drive code is like mixing up rights and lefts idk how to fix that (I did try making it negitive and that lowk didnt work yall got that)
                frontLeft.setPower(Strafe * 1 + Drive * 1 + Rotate * 1);
                backLeft.setPower(Strafe * 1 + Drive * 1 + Rotate * -1);
                frontRight.setPower(Strafe * 1 + Drive * -1 + Rotate * 1);
                backRight.setPower(Strafe * 1 + Drive * -1 + Rotate * -1);
                Drive = gamepad1.left_stick_y * 1;
                Rotate = gamepad1.left_stick_x * 1;
                Strafe = gamepad1.right_stick_x * 1;
                
                // AprilTag Stuff
                telemetryAprilTag();
                telemetry.update();

                // Ball Blocker controls (right bumper, left bumper)
                if (gamepad1.right_bumper) {
                    ballBlock.setPosition(1); //uh put actual posisitions here
                }  else if (gamepad1.left_bumper) {
                    ballBlock.setPosition(0); //test these por favor (Servos do positions between 1 - 0 so like test between those for the right rotational positions)
                }
                // Main intake controls (a , b)
                if (gamepad1.a) { //Balls going in
                    mainIntake.setPower(1);
                    middleIntake.setPower(0);
                    intakeFrontLeft.setPower(1);
                    intakeFrontRight.setPower(-1);
                }
                if (gamepad1.b) { //Balls going out
                    mainIntake.setPower(-1);
                    middleIntake.setPower(-1);
                    intakeFrontLeft.setPower(-1); //Check left and right intake settings
                    intakeFrontRight.setPower(1);
                }

                // Fly Wheel controls (Dpad up, Dpad down)
                if (gamepad1.dpad_up) {
                    flyWheel.setPower(1);
                }
                if (gamepad1.dpad_down) {
                    flyWheel.setPower(-0.01); //This is like this because if it isnt then it dosent stop fast enough on bot
                }
            }
        }
    }

//    private int getDetectionsRange() {
//        aprilTag.getDetections();
//        int foundRange = detection.ftcPose.range;
//        
//    }


    
    // I got initAprilTag() and telemetryAprilTag() from Concept.Apriltag.java
    private void initAprilTag() {

        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder().build();

                // The following default settings are available to un-comment and edit as needed.
                //.setDrawAxes(true) // Changed in V12.0
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                //.setDrawCubeProjection(false)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
                // ... these parameters are fx, fy, cx, cy.

    

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second (default)
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second (default)
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        //aprilTag.setDecimation(3);

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);

    }   // end method initAprilTag()
    
    private void telemetryAprilTag() {

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection instanceof AprilTagSingleDetection) {
                AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;

                if (singleDet.metadata != null) {
                    telemetry.addLine(String.format("\n==== (ID %d) %s", singleDet.id, singleDet.metadata.name));
                    telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                    telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                    telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                } else {
                    telemetry.addLine(String.format("\n==== (ID %d) Unknown", singleDet.id));
                    telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", singleDet.center.x, singleDet.center.y));
                }
            } else {
                AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                telemetry.addLine(String.format("\n==== Tag Cluster (%s)", clusterDet.metadata.name));
                telemetry.addLine(String.format("Percent tags found: %d", clusterDet.percentClusterFound));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            }
        }   // end for() loop

        // Add "key" information to telemetry
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");

    }   // end method telemetryAprilTag()


}

