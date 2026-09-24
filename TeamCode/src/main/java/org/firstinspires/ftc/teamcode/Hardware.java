package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Hardware {
    private static Hardware self;
    private OpMode opMode;
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    // Use this one
    public DcMotor intakeMotor;

    // shooter motor
    public DcMotor shooterMotor;

    // Transition Motor Names
    public CRServo transLeft;
    public CRServo transRight;

    // Powers can be adjusted here
    private double intakePower = 1.0;
    private double shootPower = 0.8;

    private Hardware(OpMode opMode) {
        self = this;
        this.opMode = opMode;
    }

    public static Hardware getInstance(OpMode opMode) {
        if (self == null) self = new Hardware(opMode);
        return self;
    }

    public void init(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Intake motor
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        // Shooter motor
        shooterMotor = hardwareMap.get(DcMotor.class, "motor");
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Transport servos (optional). These move the game element into the shooter.
        try {
            transLeft = hardwareMap.get(CRServo.class, "transLeft");
        } catch (Exception e) {
            transLeft = null;
        }

        try {
            transRight = hardwareMap.get(CRServo.class, "transRight");
            transRight.setDirection(CRServo.Direction.REVERSE); // transRight is reversed because builder wiring problems
        } catch (Exception e) {
            transRight = null;
        }
    }

    public void setToRunToPosition() {
        setPower(0);
        setTarget(0);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setPower(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    public void rightSidePower(double power) {
        frontRight.setPower(power);
        backRight.setPower(power);
    }

    public void leftSidePower(double power) {
        frontLeft.setPower(power);
        backLeft.setPower(power);
    }

    public void setTarget(int ticks) {
        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);
    }

    public void setToNoEncoder() {
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setWithEncoder() {
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

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

    public void setIntakePower(double power) {
        if (intakeMotor != null) {
            double pos = 0.0; // neutral
            if (power > 0.05) pos = 1.0;      // intake in
            else if (power < -0.05) pos = -1.0; // out
            intakeMotor.setPower(pos);
            return;
        }

    }

    // Stop intake (set to neutral). Works for single-servo or dual-servo intake.
    public void stopIntake() {
        if (intakeMotor != null) {
            intakeMotor.setPower(0.0);
            return;
        }
    }

    public void stopMotor() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        shooterMotor.setPower(0);
        intakeMotor.setPower(0);
        transLeft.setPower(0);
        transRight.setPower(0);
    }

    // Set shooter motor power (-1.0 .. 1.0). Safe if shooterMotor is not configured.
    public void setShooterPower(double power) {
        if (shooterMotor != null) shooterMotor.setPower(power);

        // When shooter is active (beyond a small deadband), run transport servos.
        // If your transport servos are continuous-rotation servos, consider using
        // CRServo rather than Servo and call setPower instead of setPosition.
        double threshold = 0.05;
        if (transLeft != null && transRight != null) {
            if (Math.abs(power) > threshold) {
                // Set positions for "feeding". These values are conventional: adjust
                // to match your hardware orientation. Left goes forward -> 1.0,
                // right is mirrored -> 0.0. If your servos need reversed values,
                // swap them or change to appropriate values.
                transLeft.setPower(1.0);
                transRight.setPower(0.0);
            } else {
                // Neutral / stopped positions
                transLeft.setPower(0.0);
                transRight.setPower(0.0);
            }
        }
    }

    // Stop shooter.
    public void stopShooter() {
        if (shooterMotor != null) shooterMotor.setPower(0);
        // also neutralize transports
        if (transLeft != null) transLeft.setPower(0.0);
        if (transRight != null) transRight.setPower(0.0);
    }
}