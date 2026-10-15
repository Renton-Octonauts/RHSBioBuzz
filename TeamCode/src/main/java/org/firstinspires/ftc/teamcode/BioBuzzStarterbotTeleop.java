/*   MIT License
 *   Copyright (c) [2026] [Base 10 Assets, LLC]
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:

 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.

 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

/**
 * Driver-controlled two-motor drive, intake, and launcher OpMode.
 *
 * <p>Drive with left-stick Y and right-stick X. Use the triggers for intake and
 * hold the right bumper to spin up and feed the launcher. Hardware names are
 * {@code left_drive}, {@code right_drive}, {@code intakeMotor}, {@code flyWheel},
 * {@code trans}, {@code intakeLeft}, and {@code intakeRight}.</p>
 */
@TeleOp(name = "BioBuzz StarterBot Teleop", group = "StarterBot")
//@Disabled
public class BioBuzzStarterbotTeleop extends OpMode {

    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotorEx launcher = null;
    private DcMotor intake = null;
    private CRServo leftIntakeServo = null;
    private CRServo rightIntakeServo = null;
    private CRServo windmillServo = null;

    /** Launcher target speed in encoder ticks per second. */
    public final int LAUNCHER_TARGET_VELOCITY = 6000;

    /** Minimum measured speed before the windmill feeds a game element. */
    public final int LAUNCHER_MIN_VELOCITY = 6000;

    /** Drive powers last sent to each side; used for telemetry. */
    double leftPower;
    double rightPower;

    /** Intake power calculated from the gamepad triggers. */
    double intakePower;

    /** Maps configured devices and applies motor, servo, and launcher settings. */
    @Override
    public void init() {

        // These names must match the Robot Controller hardware configuration.
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        intake = hardwareMap.get(DcMotor.class, "intakeMotor");
        launcher = hardwareMap.get(DcMotorEx.class, "flyWheel");
        windmillServo = hardwareMap.get(CRServo.class, "trans");
        leftIntakeServo = hardwareMap.get(CRServo.class, "intakeLeft");
        rightIntakeServo = hardwareMap.get(CRServo.class, "intakeRight");

        // Verify motor direction on the robot so forward stick input drives forward.
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        leftDrive.setZeroPowerBehavior(BRAKE);
        rightDrive.setZeroPowerBehavior(BRAKE);
        intake.setZeroPowerBehavior(BRAKE);

        // Encoder feedback enables closed-loop launcher velocity control.
        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        leftIntakeServo.setPower(0);
        rightIntakeServo.setPower(0);
        windmillServo.setPower(0);

        rightIntakeServo.setDirection(DcMotorSimple.Direction.REVERSE);
        windmillServo.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {

        // Left stick Y drives; right stick X turns.
        double y = -gamepad1.left_stick_y;
        double rx = gamepad1.right_stick_x;
        arcadeDrive(y, rx);

        // Right trigger intakes; left trigger reverses the intake.
        intakePower = gamepad1.right_trigger - gamepad1.left_trigger;

        launch();

        intake.setPower(intakePower);
        leftIntakeServo.setPower(intakePower);
        rightIntakeServo.setPower(intakePower);

        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
        telemetry.addLine();
        telemetry.addData("FlyWheel", "%", launcher);
        telemetry.addLine();

    }

    @Override
    public void stop() {
    }

    /** Applies arcade-drive inputs to the left and right drive motors. */
    void arcadeDrive(double y, double rx) {
        leftPower = y + rx;
        rightPower = y - rx;

        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
    }

    /** Optional bumper-controlled intake helper; the main loop currently uses triggers. */
    void intake() {
        if(gamepad1.left_bumper) {
            intake.setPower(1);
            leftIntakeServo.setPower(1);
            rightIntakeServo.setPower(1);
        } else {
            intake.setPower(0);
            leftIntakeServo.setPower(0);
            rightIntakeServo.setPower(0);
        }
    }

    /** Spins up while the right bumper is held and feeds above the minimum speed. */
    void launch() {

        if (gamepad1.right_bumper) {
            launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);
        } else {
            launcher.setVelocity(0);
        }

        if (gamepad1.right_bumper && launcher.getVelocity() > LAUNCHER_MIN_VELOCITY) {
            windmillServo.setPower(1);

        } else {
            windmillServo.setPower(0);
        }
    }

}
