# BioBuzz StarterBot TeleOp Guide

`BioBuzzStarterbotTeleop` is a driver-controlled OpMode for the StarterBot's
two-sided drive, intake, and launcher. It appears in the Driver Station as
**BioBuzz StarterBot Teleop**.

## Driver Station hardware names

The configured hardware names must match these strings exactly:

| Device | Configuration name | SDK type |
| --- | --- | --- |
| Left drive motor | `left_drive` | `DcMotor` |
| Right drive motor | `right_drive` | `DcMotor` |
| Intake motor | `intakeMotor` | `DcMotor` |
| Launcher motor | `flyWheel` | `DcMotorEx` |
| Windmill feeder | `trans` | `CRServo` |
| Left intake servo | `intakeLeft` | `CRServo` |
| Right intake servo | `intakeRight` | `CRServo` |

The launcher must support encoder velocity control because the OpMode uses its
measured velocity to decide when to run the windmill feeder.

## Controls

| Gamepad 1 control | Action |
| --- | --- |
| Left stick up/down | Drive forward/backward |
| Right stick left/right | Turn |
| Right trigger | Run intake forward proportionally to trigger pressure |
| Left trigger | Run intake in reverse proportionally to trigger pressure |
| Right bumper | Spin up the launcher; feed when measured speed is above the minimum |

The two triggers are combined: `right trigger - left trigger`. Pressing both by
the same amount results in zero intake power. The left bumper intake helper in
the Java file is not called by the main loop, so the left bumper does not
control intake in the current OpMode.

## Initialization and operation

During initialization, the OpMode maps hardware, sets the left drive motor to
forward and the right drive motor to reverse, and sets drive and intake motors
to brake at zero power. Check motor direction on the robot before driving.

The launcher runs in `RUN_USING_ENCODER` mode with PIDF coefficients `40, 0, 0,
12.5`. Holding the right bumper requests `6000` encoder ticks per second. The
windmill runs only while the bumper is held and the measured launcher velocity
is greater than `1200` ticks per second. Releasing the bumper sets launcher
velocity and windmill power to zero.

Drive uses arcade mixing: left power is forward plus turn, and right power is
forward minus turn. The OpMode reports the last commanded left and right drive
powers through telemetry.

## Source

Implementation: [BioBuzzStarterbotTeleop.java](../BioBuzzStarterbotTeleop.java).
