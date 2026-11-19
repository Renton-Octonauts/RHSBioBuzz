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

import org.firstinspires.ftc.teamcode.pedro.Constants;

// Cool Example https://github.com/BeepBot99/CodeBloodedDecodeV2/blob/master/src/main/java/org/firstinspires/ftc/teamcode/opmodes/autos/BlueClose15Full.java

@Autonomous(name = "Red Alliance Pollen Line Auto, Faces Hive V2", group = "Autonomous")
public class RedPollenLineAutoV2 extends LinearOpMode {

    private Follower follower;
    private DcMotorEx flyWheel;
    private DcMotorEx midIntake;
    private DcMotorEx mainIntake;
    private Servo stopper;
    private Servo flowerBarRight;
    private Servo flowerBarLeft;

    final double ServoPosClosed = 0.5;
    final double ServoPosOpen = 0;

    final double ServoPosUp = 0; // TODO Values for bar
    final double ServoPosDown = 0.7; // TODO Values for bar

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose redAlliancePollenLineFacingHive = poseFactory.of(57.5, 11, 90);
    private final Pose walkUpAndShoot = poseFactory.of(57.5, 27.5, 90);
    private final Pose moveToPollen = poseFactory.of(26.5, 7.9, 180);
    private final Pose moveToPollenControl1 = poseFactory.of(57.5, 7.9, 0);
    private final Pose moveToPollenSegment1Start = poseFactory.of(26.5, 7.9, 90);
    private final Pose moveToPollenSegment1End = poseFactory.of(26.5, 7.9, 180);
    private final Pose moveToPollenSegment2Start = poseFactory.of(26.5, 7.9, 180);
    private final Pose moveToPollenSegment2End = poseFactory.of(26.5, 7.9, 180);
    private final Pose collectPollenStart = poseFactory.of(26.5, 7.9, 180);
    private final Pose collectPollen = poseFactory.of(11, 7.9, 180);
    private final Pose moveToOtherSideAndShoot = poseFactory.of(57.5, 114, 270);
    private final Pose moveToOtherSideAndShootControl1 = poseFactory.of(56.858, 46.7501, 0);
    private final Pose moveToOtherSideAndShootControl2 = poseFactory.of(5, 114, 0);
    private final Pose moveToOtherSideAndShootSegment1Start = poseFactory.of(57.5, 114, 180);
    private final Pose moveToOtherSideAndShootSegment1End = poseFactory.of(57.5, 114, 270);
    private final Pose moveToOtherSideAndShootSegment2Start = poseFactory.of(57.5, 114, 270);
    private final Pose moveToOtherSideAndShootSegment2End = poseFactory.of(57.5, 114, 270);
    private final Pose collectFlower = poseFactory.of(46.6, 130.5, 90);
    private final Pose collectFlowerControl1 = poseFactory.of(46.6, 114, 0);
    private final Pose collectFlowerSegment1Start = poseFactory.of(46.6, 130.5, 270);
    private final Pose collectFlowerSegment1End = poseFactory.of(46.6, 130.5, 90);
    private final Pose collectFlowerSegment2Start = poseFactory.of(46.6, 130.5, 90);
    private final Pose collectFlowerSegment2End = poseFactory.of(46.6, 130.5, 90);
    private final Pose walkUpAndShoot_2 = poseFactory.of(57.5, 114, 270);
    private final Pose walkUpAndShoot_2Control1 = poseFactory.of(46.6, 114, 0);
    private final Pose walkUpAndShoot_2Segment1Start = poseFactory.of(57.5, 114, 90);
    private final Pose walkUpAndShoot_2Segment1End = poseFactory.of(57.5, 114, 90);
    private final Pose walkUpAndShoot_2Segment2Start = poseFactory.of(57.5, 114, 90);
    private final Pose walkUpAndShoot_2Segment2End = poseFactory.of(57.5, 114, 270);
    private final Pose park = poseFactory.of(7.9, 123, 270);
    private final Pose parkControl1 = poseFactory.of(35.3822, 129.3828, 0);

    // Autonomous routine, EDIT HERE!, TODO add raising and lowering of flower extraction bar
    public Command autoRoutine() {
        return sequential(
            revUp(),
            follow(follower, walkUpAndShoot()),
            waitMs(3000),
            shootProcess(),
            follow(follower, moveToPollen()),
            intakeOn(),
            follow(follower, collectPollen()),
            intakeOff(),
            revUp(),
            follow(follower, moveToOtherSideAndShoot()),
            shootProcess(),
            lowerFlowerBar(),
            intakeOn(),
            follow(follower, collectFlower()),
            waitMs(750),
            intakeOff(),
            revUp(),
            follow(follower, walkUpAndShoot_2()),
            shootProcess(),
            raiseFlowerBar(),
            follow(follower, park())
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
        flowerBarRight = hardwareMap.get(Servo.class, "flowerBarRight");
        flowerBarLeft = hardwareMap.get(Servo.class, "flowerBarLeft");

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

    public Path walkUpAndShoot() {
        return Paths.line(redAlliancePollenLineFacingHive, walkUpAndShoot).constant(walkUpAndShoot);
    }

    public Path moveToPollen() {
        return Paths.curve(walkUpAndShoot, moveToPollenControl1, moveToPollen).heading(Interpolator.piecewise().until(0.5, Interpolator.linear(moveToPollenSegment1Start, moveToPollenSegment1End)).until(1, Interpolator.linear(moveToPollenSegment2Start, moveToPollenSegment2End)));
    }

    public Path collectPollen() {
        return Paths.line(collectPollenStart, collectPollen).linear(collectPollenStart, collectPollen);
    }

    public Path moveToOtherSideAndShoot() {
        return Paths.curve(collectPollen, moveToOtherSideAndShootControl1, moveToOtherSideAndShootControl2, moveToOtherSideAndShoot).heading(Interpolator.piecewise().until(0.3, Interpolator.linear(moveToOtherSideAndShootSegment1Start, moveToOtherSideAndShootSegment1End)).until(1, Interpolator.linear(moveToOtherSideAndShootSegment2Start, moveToOtherSideAndShootSegment2End)));
    }

    public Path collectFlower() {
        return Paths.curve(moveToOtherSideAndShoot, collectFlowerControl1, collectFlower).heading(Interpolator.piecewise().until(0.7, Interpolator.linear(collectFlowerSegment1Start, collectFlowerSegment1End)).until(1, Interpolator.linear(collectFlowerSegment2Start, collectFlowerSegment2End)));
    }

    public Path walkUpAndShoot_2() {
        return Paths.curve(collectFlower, walkUpAndShoot_2Control1, walkUpAndShoot_2).heading(Interpolator.piecewise().until(0.333, Interpolator.linear(walkUpAndShoot_2Segment1Start, walkUpAndShoot_2Segment1End)).until(1, Interpolator.linear(walkUpAndShoot_2Segment2Start, walkUpAndShoot_2Segment2End)));
    }

    public Path park() {
        return Paths.curve(walkUpAndShoot_2, parkControl1, park).constant(park);
    }
    private Command shootProcess() {
        return sequential (
                // Shooting
                instant(() -> {
                    // Shooting motors should automatically be on
                    stopper.setPosition(ServoPosOpen);
                }),
                waitMs(3000),
                // Stop shooting
                instant(() -> {
                    flyWheel.setPower(0);
                    midIntake.setPower(0);
                    mainIntake.setPower(0);
                    stopper.setPosition(ServoPosClosed);
                })
        );
    }

    private Command revUp() {
        return sequential (
                instant(() -> {
                    flyWheel.setPower(-1.0);
                    midIntake.setPower(-1.0);
                    mainIntake.setPower(-1.0);
                    stopper.setPosition(ServoPosClosed);
                })
        );
    }

    private Command intakeOn() {
        return sequential (
                instant(() -> {
                    midIntake.setPower(-1.0);
                    mainIntake.setPower(-1.0);
                    stopper.setPosition(ServoPosClosed);
                })
        );
    }

    private Command intakeOff() {
        return sequential (
                instant(() -> {
                    midIntake.setPower(0);
                    mainIntake.setPower(0);
                    stopper.setPosition(ServoPosClosed);
                })
        );
    }
    private Command raiseFlowerBar() {
        return sequential(
                instant(() -> {
                    flowerBarRight.setPosition(ServoPosUp);
                    flowerBarLeft.setPosition(ServoPosUp);
                })
        );
    }

    private Command lowerFlowerBar() {
        return sequential (
                instant(() -> {
                    flowerBarRight.setPosition(ServoPosDown);
                    flowerBarLeft.setPosition(ServoPosDown);
                })
        );
    }
}
