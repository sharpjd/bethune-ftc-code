package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Pushbot: Mecanum wheels", group="mode")

public class Main2 extends LinearOpMode {

    /* Declare OpMode members. */
    HardwarePushbot robot           = new HardwarePushbot();   // Use a Pushbot's hardware
    double          clawOffset      = 0;                       // Servo mid position
    final double    CLAW_SPEED      = 0.02 ;                   // sets rate to move servo
    private ElapsedTime     runtime = new ElapsedTime();

    double speedMultiplier = 1.0;

    @Override
    public void runOpMode() {
        double left;
        double right;
        double drive;
        double turn;
        double max;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();
        resetMotors();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
            // This way it's also easy to just drive straight, or just turn.
            drive = -gamepad1.left_stick_y;
            turn  =  gamepad1.right_stick_x;
            
            // speed multiplier
            if (gamepad1.dpad_down){
                // prevent quickly increasing the speed by using runtime
                if (runtime.seconds() > 0.2){
                    speedMultiplier = Math.max(0.05, speedMultiplier - 0.05);
                    runtime.reset();
                }
            }
            if (gamepad1.dpad_up){
                if (runtime.seconds() > 0.2){
                    speedMultiplier = Math.min(1.00, speedMultiplier + 0.05);
                    runtime.reset();
                }
            }

            mecanumDrive_Cartesian(-gamepad1.right_stick_x, gamepad1.right_stick_y, gamepad1.left_stick_x);
            }
        }
    public void mecanumDrive_Cartesian(double x, double y, double rotation)
{
    double wheelSpeeds[] = new double[4];

    wheelSpeeds[0] = x + y - rotation;
    wheelSpeeds[1] = -x + y + rotation;
    wheelSpeeds[2] = -x + y - rotation;
    wheelSpeeds[3] = x + y + rotation;

    //normalize(wheelSpeeds);
    reduceSpeeds(wheelSpeeds, speedMultiplier);

    robot.frontLeft.setPower(-wheelSpeeds[0]);
    robot.frontRight.setPower(wheelSpeeds[1]);
    robot.backLeft.setPower(-wheelSpeeds[2]);
    robot.backRight.setPower(wheelSpeeds[3]);

    telemetry.addData("front Left", robot.frontLeft.getPower());
    telemetry.addData("front Right", robot.frontRight.getPower());
    telemetry.addData("back Left", robot.backLeft.getPower());
    telemetry.addData("back Right", robot.backRight.getPower());
    telemetry.addData("speedMultiplier", speedMultiplier);
    telemetry.update();

}   //end mecanumDrive_Cartesian

private void reduceSpeeds(double[] wheelSpeeds, double multiplier){
    for (int i = 0; i < wheelSpeeds.length; i ++){
        wheelSpeeds[i] = wheelSpeeds[i] * multiplier;
    }
}

private void resetMotors(){
    robot.frontLeft.setPower(0);
    robot.frontRight.setPower(0);
    robot.backLeft.setPower(0);
    robot.backRight.setPower(0);
    
    robot.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    robot.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    robot.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    robot.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    
    // reset target positions
    robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition());
    robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition());
    robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition());
    robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition());
}


// private void normalize(double[] wheelSpeeds)
// {
//     double maxMagnitude = Math.abs(wheelSpeeds[0]);

//     for (int i = 1; i < wheelSpeeds.length; i++)
//     {
//         double magnitude = Math.abs(wheelSpeeds[i]);

//         if (magnitude > maxMagnitude)
//         {
//             maxMagnitude = magnitude;
//         }
//     }

//     if (maxMagnitude > 1.0)
//     {
//         for (int i = 0; i < wheelSpeeds.length; i++)
//         {
//             wheelSpeeds[i] /= maxMagnitude;
//         }
//     }
// }   //normalize
}//end class
