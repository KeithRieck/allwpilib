/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.experimental.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ParallelRaceGroupTest extends CommandTestBase {
  @Test
  void parallelRaceScheduleTest() {
    CommandScheduler scheduler = new CommandScheduler();

    MockCommandHolder command1Holder = new MockCommandHolder(true);
    Command command1 = command1Holder.getMock();
    MockCommandHolder command2Holder = new MockCommandHolder(true);
    Command command2 = command2Holder.getMock();

    Command group = new ParallelRaceGroup(command1, command2);

    scheduler.schedule(group);

    verify(command1).initialize();
    verify(command2).initialize();

    command1Holder.setFinished(true);
    scheduler.run();
    command2Holder.setFinished(true);
    scheduler.run();

    verify(command1).execute();
    verify(command1).end(false);
    verify(command2).execute();
    verify(command2).end(true);
    verify(command2, never()).end(false);

    assertFalse(scheduler.isScheduled(group));
  }

  @Test
  void parallelRaceInterruptTest() {
    CommandScheduler scheduler = new CommandScheduler();

    MockCommandHolder command1Holder = new MockCommandHolder(true);
    Command command1 = command1Holder.getMock();
    MockCommandHolder command2Holder = new MockCommandHolder(true);
    Command command2 = command2Holder.getMock();

    Command group = new ParallelRaceGroup(command1, command2);

    scheduler.schedule(group);

    scheduler.run();
    scheduler.run();
    scheduler.cancel(group);

    verify(command1, times(2)).execute();
    verify(command1, never()).end(false);
    verify(command1).end(true);

    verify(command2, times(2)).execute();
    verify(command2, never()).end(false);
    verify(command2).end(true);

    assertFalse(scheduler.isScheduled(group));
  }

  @Test
  void notScheduledCancelTest() {
    CommandScheduler scheduler = new CommandScheduler();

    MockCommandHolder command1Holder = new MockCommandHolder(true);
    Command command1 = command1Holder.getMock();
    MockCommandHolder command2Holder = new MockCommandHolder(true);
    Command command2 = command2Holder.getMock();

    Command group = new ParallelRaceGroup(command1, command2);

    assertDoesNotThrow(() -> scheduler.cancel(group));
  }
}