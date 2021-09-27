import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.PSPNXController;

class RobotImpl {
	class MotorWrapper {
		public enum PSPJoystickAxis {
			LeftX,
			LeftY,
			RightX,
			RightY;
		}

		private Motor motor;
		private PSPJoystickAxis axis;
		private int factor;

		private static int getValueForAxis(PSPNXController controller, PSPJoystickAxis axis) {
			switch (this.axis) {
				case PSPJoystickAxis.LeftX:
					return controller.getLeftX();
				case PSPJoystickAxis.LeftY:
					return controller.getLeftY();
				case PSPJoystickAxis.RightX:
					return controller.getRightX();
				case PSPJoystickAxis.RightY:
					return controller.getRightY();
			}
		}

		public MotorWrapper(Motor motor, PSPJoystickAxis axis, int factor) {
			this.motor = motor;
			this.axis = axis;
			this.factor = factor;
		}

		public update(PSPNXController controller) {
			final int valueForAxis = getValueForAxis(controller, this.axis);
			this.motor.setSpeed(valueForAxis * this.factor);
			if (valueForAxis >= 0) {
				this.motor.forward();
			} else {
				this.motor.backward();
			}
		}
	}

	private PSPNXController psp;
	private MotorWrapper leftMotor;
	private MotorWrapper rightMotor;

	public RobotImpl(PSPNXController psp) {
		this.psp = psp;
		this.psp.setDigitalMode(true);
		this.leftMotor = new MotorWrapper(Motor.A, MotorWrapper.PSPJoystickAxis.LeftY, -5);
		this.leftMotor = new MotorWrapper(Motor.B, MotorWrapper.PSPJoystickAxis.RightY, -5);
	}

	public run() {
		this.leftMotor.update(this.psp);
		this.rightMotor.update(this.psp);
	}
}

public class Robot {
	public static void main(String[] args) {
		RobotImpl impl = new RobotImpl(new PSPNXController(SensorPort.S1));
		while (true) {
			impl.run();
		}
	}
}
