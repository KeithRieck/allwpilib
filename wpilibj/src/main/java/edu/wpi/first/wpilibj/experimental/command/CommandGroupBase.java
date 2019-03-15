package edu.wpi.first.wpilibj.experimental.command;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
abstract class CommandGroupBase {

  private static Set<Command> m_groupedCommands = Collections.newSetFromMap(new WeakHashMap<>());

  static void registerGroupedCommands(Command... commands) {
    m_groupedCommands.addAll(Set.of(commands));
  }

  static Set<Command> getGroupedCommands() {
    return m_groupedCommands;
  }
}
