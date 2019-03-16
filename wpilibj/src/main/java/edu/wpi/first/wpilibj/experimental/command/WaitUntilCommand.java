package edu.wpi.first.wpilibj.experimental.command;

import edu.wpi.first.wpilibj.Timer;

public class WaitUntilCommand implements Command {

  private final double m_time;

  /**
   * This command will do nothing, and will end when the current match time exceeds the specified
   * time.
   *
   * @param seconds the match time at which to end the command.
   */
  public WaitUntilCommand(double seconds){
    m_time = seconds;
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {

  }

  @Override
  public void end() {

  }

  @Override
  public boolean isFinished() {
    return Timer.getMatchTime() >= m_time;
  }
}