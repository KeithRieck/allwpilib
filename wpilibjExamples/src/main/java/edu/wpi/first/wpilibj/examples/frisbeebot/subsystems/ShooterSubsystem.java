package edu.wpi.first.wpilibj.examples.frisbeebot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.experimental.command.SynchronousPIDSubsystem;
import edu.wpi.first.wpilibj.experimental.controller.PIDController;

import static edu.wpi.first.wpilibj.examples.frisbeebot.Constants.ShooterConstants.*;

public class ShooterSubsystem extends SynchronousPIDSubsystem {

  private Spark m_shooterMotor = new Spark(kShooterMotorPort);
  private Spark m_feederMotor = new Spark(kFeederMotorPort);
  private Encoder m_shooterEncoder = new Encoder(kEncoderPorts[0], kEncoderPorts[1],
      kEncoderReversed);

  public ShooterSubsystem() {
    super(new PIDController(kP, kI, kD));
    getController().setAbsoluteTolerance(kShooterToleranceRPS);
    m_shooterEncoder.setDistancePerPulse(kEncoderDistancePerPulse);
  }

  @Override
  public void useOutput(double output) {
    // Use a feedforward of the form kS + kV * velocity
    m_shooterMotor.set(output + kSFractional + kVFractional * kShooterTargetRPS);
  }

  @Override
  public double getReference() {
    return kShooterTargetRPS;
  }

  @Override
  public double getMeasurement() {
    return m_shooterEncoder.getRate();
  }

  public boolean atReference() {
    return m_controller.atReference();
  }

  public void runFeeder() {
    m_feederMotor.set(kFeederSpeed);
  }

  public void stopFeeder() {
    m_feederMotor.set(0);
  }

  @Override
  public void disable() {
    super.disable();
    // Turn off motor when we disable, since useOutput(0) doesn't stop the motor due to our
    // feedforward
    m_shooterMotor.set(0);
  }
}