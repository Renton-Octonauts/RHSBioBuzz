# Odometry test

`OdometryTest` is a driver-controlled mecanum test OpMode. It drives the four
configured drive motors and reads the robot pose from a goBILDA Pinpoint.

## Driver Station configuration

Configure these hardware names exactly as shown:

| Device | Configuration name |
| --- | --- |
| goBILDA Pinpoint | `odometry` |
| Front left drive motor | `frontLeft` |
| Front right drive motor | `frontRight` |
| Back left drive motor | `backLeft` |
| Back right drive motor | `backRight` |

The Pinpoint must have two compatible odometry pods connected. This OpMode is
configured for goBILDA 4-bar pods and initially assumes both encoder directions
are `FORWARD`.

## Robot-specific setup

Before relying on measurements, configure the Pinpoint offsets in `init()` to
match the pod positions relative to the robot's tracking center. The current
offsets are both zero millimeters, which is only correct if both pods are at
that center. The X pod offset is positive to the robot's left and the Y pod
offset is positive toward the front, as defined by goBILDA's Pinpoint driver.

With the robot stationary, initialize the OpMode so the Pinpoint can reset its
position and calibrate its IMU. Verify the pod directions: the forward pod
should count up when the robot moves forward, and the strafe pod should count
up when the robot moves left. Change the corresponding encoder direction if a
reading moves the wrong way. Confirm wheel motor directions separately before
driving; this OpMode does not set motor direction modes.

## Controls and telemetry

- Left stick: forward/back and strafe.
- Right stick horizontal: rotate.
- A: set the reported Pinpoint position to X=0, Y=0, heading=0. This does not
  recalibrate the IMU.
- Telemetry: X and Y in millimeters, heading in degrees, and each wheel's power.

The loop calls `odo.update()` before reading the pose. The four mecanum powers
are scaled together if any requested power exceeds the motor range, preserving
the requested mix of translation and rotation.

## Validation

Run the OpMode with the robot safely supported or with wheels clear of the
floor while checking motor direction. Then move the robot by hand: forward
motion should change X, left motion should change Y, and a counterclockwise
turn should increase heading according to the Pinpoint coordinate convention.
If an axis or sign is wrong, check pod mounting, offsets, and encoder direction
before changing the pose display code.
