package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.pedropathing.paths.interpolator.Interpolator;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

// Cool Example https://github.com/BeepBot99/CodeBloodedDecodeV2/blob/master/src/main/java/org/firstinspires/ftc/teamcode/opmodes/autos/BlueClose15Full.java

@Autonomous(name = "Red Alliance Pollen Line Auto, Faces Hive", group = "Autonomous")
public class RedPollenLineAuto extends LinearOpMode {

    private Follower follower;
    private DcMotorEx flyWheel;
    private DcMotorEx midIntake;
    private DcMotorEx mainIntake;
    private Servo stopper;

    double ServoPosClosed = 0.2; // TODO make sure Servo Position is CLOSED
    double ServoPosOpen = 0; // TODO make sure Servo Position is OPEN

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose redAlliancePollenLineFacingHive = poseFactory.of(56, 11, 90);
    private final Pose point1180 = poseFactory.of(56, 33, 270);
    private final Pose headToLine = poseFactory.of(25, 7.9, 180);
    private final Pose headToLineControl1 = poseFactory.of(56.1053, 18.9803, 0);
    private final Pose headToLineSegment1Start = poseFactory.of(25, 7.9, 270);
    private final Pose headToLineSegment1End = poseFactory.of(25, 7.9, 180);
    private final Pose headToLineSegment2Start = poseFactory.of(25, 7.9, 180);
    private final Pose headToLineSegment2End = poseFactory.of(25, 7.9, 180);
    private final Pose collectBallssleep = poseFactory.of(11, 7.9, 180);
    private final Pose walkDiagonal = poseFactory.of(30, 40, 180);
    private final Pose walkBackAndRotateBackwardsStart = poseFactory.of(30, 40, 180);
    private final Pose walkBackAndRotateBackwards = poseFactory.of(30, 105, 270);
    private final Pose walkDiagonal_2 = poseFactory.of(59, 120, 270);
    private final Pose walkBackAndShootsleep = poseFactory.of(59, 130.5, 270);
    private final Pose walkUpStart = poseFactory.of(59, 130.5, 270);
    private final Pose walkUp = poseFactory.of(59, 120, 270);
    private final Pose point9180AndMoveToFlowersleepStart = poseFactory.of(59, 120, 270);
    private final Pose point9180AndMoveToFlowersleep = poseFactory.of(46.5, 120, 90);
    private final Pose collectPollensleepStart = poseFactory.of(46.5, 120, 90);
    private final Pose collectPollensleep = poseFactory.of(46.5, 130.5, 90);
    private final Pose walkBack = poseFactory.of(46.5, 120, 90);
    private final Pose point12180AndMoveToHiveStart = poseFactory.of(46.5, 120, 90);
    private final Pose point12180AndMoveToHive = poseFactory.of(59, 120, 270);
    private final Pose walkBackAndShootsleep_2 = poseFactory.of(59, 130.5, 270);
    private final Pose walkForward = poseFactory.of(59, 120, 270);
    private final Pose strafeToParking = poseFactory.of(7.9, 120, 270);

    // Autonomous routine, EDIT HERE!, TODO add raising and lowering of flower extraction bar
    public Command autoRoutine() {
        return sequential(
            shootProcess(),
            follow(follower, path1180()),
            follow(follower, headToLine()),
            follow(follower, collectBallssleep()),
            follow(follower, walkDiagonal()),
            follow(follower, walkBackAndRotateBackwards()),
            follow(follower, walkDiagonal_2()),
            follow(follower, walkBackAndShootsleep()),
            shootProcess(),
            follow(follower, walkUp()),
            follow(follower, path9180AndMoveToFlowersleep()),
            // TODO lower flower bar
            follow(follower, collectPollensleep()),
            follow(follower, walkBack()),
            // TODO raise flower bar
            follow(follower, path12180AndMoveToHive()),
            follow(follower, walkBackAndShootsleep_2()),
            shootProcess(),
            follow(follower, walkForward()),
            follow(follower, strafeToParking())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        flyWheel = hardwareMap.get(DcMotorEx.class, "flyWheel");
        midIntake = hardwareMap.get(DcMotorEx.class, "midIntake");
        mainIntake = hardwareMap.get(DcMotorEx.class, "mainIntake");
        stopper = hardwareMap.get(Servo.class, "stopper");

        // Set motor zero-power behavior
        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        midIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mainIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        follower.setPose(redAlliancePollenLineFacingHive);
        follower.update();

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
    }

    public Path path1180() {
        return Paths.line(redAlliancePollenLineFacingHive, point1180).linear(redAlliancePollenLineFacingHive, point1180);
    }

    public Path headToLine() {
        return Paths.curve(point1180, headToLineControl1, headToLine).heading(Interpolator.piecewise().until(0.5, Interpolator.linear(headToLineSegment1Start, headToLineSegment1End)).until(1, Interpolator.linear(headToLineSegment2Start, headToLineSegment2End)));
    }

    public Path collectBallssleep() {
        return Paths.line(headToLine, collectBallssleep).constant(collectBallssleep);
    }

    public Path walkDiagonal() {
        return Paths.line(collectBallssleep, walkDiagonal).constant(walkDiagonal);
    }

    public Path walkBackAndRotateBackwards() {
        return Paths.line(walkBackAndRotateBackwardsStart, walkBackAndRotateBackwards).linear(walkBackAndRotateBackwardsStart, walkBackAndRotateBackwards);
    }

    public Path walkDiagonal_2() {
        return Paths.line(walkBackAndRotateBackwards, walkDiagonal_2).constant(walkDiagonal_2);
    }

    public Path walkBackAndShootsleep() {
        return Paths.line(walkDiagonal_2, walkBackAndShootsleep).constant(walkBackAndShootsleep);
    }

    public Path walkUp() {
        return Paths.line(walkUpStart, walkUp).linear(walkUpStart, walkUp);
    }

    public Path path9180AndMoveToFlowersleep() {
        return Paths.line(point9180AndMoveToFlowersleepStart, point9180AndMoveToFlowersleep).linear(point9180AndMoveToFlowersleepStart, point9180AndMoveToFlowersleep);
    }

    public Path collectPollensleep() {
        return Paths.line(collectPollensleepStart, collectPollensleep).linear(collectPollensleepStart, collectPollensleep);
    }

    public Path walkBack() {
        return Paths.line(collectPollensleep, walkBack).constant(walkBack);
    }

    public Path path12180AndMoveToHive() {
        return Paths.line(point12180AndMoveToHiveStart, point12180AndMoveToHive).linear(point12180AndMoveToHiveStart, point12180AndMoveToHive);
    }

    public Path walkBackAndShootsleep_2() {
        return Paths.line(point12180AndMoveToHive, walkBackAndShootsleep_2).constant(walkBackAndShootsleep_2);
    }

    public Path walkForward() {
        return Paths.line(walkBackAndShootsleep_2, walkForward).constant(walkForward);
    }

    public Path strafeToParking() {
        return Paths.line(walkForward, strafeToParking).constant(strafeToParking);
    }

    private Command shootProcess() {
        return sequential (
            instant(() -> {
                flyWheel.setPower(1.0);
                midIntake.setPower(1.0);
                mainIntake.setPower(1.0);
                stopper.setPosition(ServoPosOpen);
            }),
            waitMs(3000),
            instant(() -> {
                flywheel.setPower(0);
                midIntake.setPower(0);
                mainIntake.setPower(0);
                stopper.setPosition(ServoPosClosed);
            })
        );
    }

    private Command raiseFlowerBar() {
        // TODO when Build team finishes applying flower bar extractor. do the same thing with shoot command but flowerBarServoRight.setPosition(69 or smth); y left side too.
        return
    }

    private Command lowerFlowerBar() {
        // TODO when Build team finishes applying flower bar extractor. do the same thing with shoot command but flowerBarServoRight.setPosition(69 or smth); y left side too.
        return
    }
}
