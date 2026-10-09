package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;


/*
* NOT DONE
* https://github.com/goBILDA-Official/FtcRobotController-Add-Pinpoint/blob/goBILDA-Odometry-Driver/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/SensorGoBildaPinpointExample.java
* The link above can also be used to create odometry (:
*/
@TeleOp(name = "Odometry test", group = "Linear Opmode")
public class OdometryTest extends OpMode {

    GoBildaPinpointDriver odo;

    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;
    public DcMotor shooterMotor;

    @Override
    public void init() {
        odo = hardwareMap.get(GoBildaPinpointDriver.class, "odometry");

        // TODO make it so that the correct ones are reversed

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");

        // Odometry computer Config
        odo.setOffsets(-0, -0); // set this to where the computer is relative to the robot
        // ^^ i also don't know what else it needs
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD); // idk might need to change
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD); // also might need to change

        // Odometry Starting position
        odo.resetPosAndIMU();
        Pose2D startingPos = new Pose2D(DistanceUnit.MM, 0,0, AngleUnit.RADIANS, 0); // where the robot is starting on the field
        odo.setPosition(startingPos);
    }

    @Override
    public void loop() {

    }
}
