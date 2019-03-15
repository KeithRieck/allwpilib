package edu.wpi.first.wpilibj.experimental.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommandRequirementsTest extends CommandTestBase {
  @Test
  void requirementInterruptTest() {
    CommandScheduler scheduler = new CommandScheduler();

    Subsystem requirement = new ASubsystem();

    MockCommandHolder interruptedHolder = new MockCommandHolder(true, requirement);
    Command interrupted = interruptedHolder.getMock();
    MockCommandHolder interrupterHolder = new MockCommandHolder(true, requirement);
    Command interrupter = interrupterHolder.getMock();

    scheduler.scheduleCommand(interrupted, true);
    scheduler.run();
    scheduler.scheduleCommand(interrupter, true);
    scheduler.run();

    verify(interrupted).initialize();
    verify(interrupted).execute();
    verify(interrupted).interrupted();
    verify(interrupted, never()).end();

    verify(interrupter).initialize();
    verify(interrupter).execute();

    assertFalse(scheduler.isScheduled(interrupted));
    assertTrue(scheduler.isScheduled(interrupter));
  }

  @Test
  void requirementUninterruptibleTest() {
    CommandScheduler scheduler = new CommandScheduler();

    Subsystem requirement = new ASubsystem();

    MockCommandHolder interruptedHolder = new MockCommandHolder(true, requirement);
    Command notInterrupted = interruptedHolder.getMock();
    MockCommandHolder interrupterHolder = new MockCommandHolder(true, requirement);
    Command interrupter = interrupterHolder.getMock();

    scheduler.scheduleCommand(notInterrupted, false);
    scheduler.scheduleCommand(interrupter, true);

    assertTrue(scheduler.isScheduled(notInterrupted));
    assertFalse(scheduler.isScheduled(interrupter));
  }

  @Test
  void parallelGroupRequirementTest() {
    Subsystem system1 = new ASubsystem();
    Subsystem system2 = new ASubsystem();
    Subsystem system3 = new ASubsystem();
    Subsystem system4 = new ASubsystem();

    CommandScheduler scheduler = new CommandScheduler();

    MockCommandHolder command1Holder = new MockCommandHolder(true, system1, system2);
    Command command1 = command1Holder.getMock();
    MockCommandHolder command2Holder = new MockCommandHolder(true, system3);
    Command command2 = command2Holder.getMock();
    MockCommandHolder command3Holder = new MockCommandHolder(true, system3, system4);
    Command command3 = command3Holder.getMock();

    Command group = new ParallelCommandGroup(command1, command2);

    scheduler.scheduleCommand(group, true);
    scheduler.scheduleCommand(command3, true);

    assertFalse(scheduler.isScheduled(group));
    assertTrue(scheduler.isScheduled(command3));
  }

  @Test
  void sequentialGroupRequirementTest() {
    Subsystem system1 = new ASubsystem();
    Subsystem system2 = new ASubsystem();
    Subsystem system3 = new ASubsystem();
    Subsystem system4 = new ASubsystem();

    CommandScheduler scheduler = new CommandScheduler();

    MockCommandHolder command1Holder = new MockCommandHolder(true, system1, system2);
    Command command1 = command1Holder.getMock();
    MockCommandHolder command2Holder = new MockCommandHolder(true, system3);
    Command command2 = command2Holder.getMock();
    MockCommandHolder command3Holder = new MockCommandHolder(true, system3, system4);
    Command command3 = command3Holder.getMock();

    Command group = new SequentialCommandGroup(command1, command2);

    scheduler.scheduleCommand(group, true);
    scheduler.scheduleCommand(command3, true);

    assertFalse(scheduler.isScheduled(group));
    assertTrue(scheduler.isScheduled(command3));
  }
}