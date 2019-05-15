/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#pragma once

#include <functional>

#include <wpi/mutex.h>

#include "frc/Notifier.h"
#include "frc/experimental/controller/PIDController.h"

namespace frc {
namespace experimental {

// TODO: remove when this class is moved into frc namespace
using frc::Notifier;

class PIDControllerRunner {
 public:
  /**
   * Allocate a PIDControllerRunner.
   *
   * @param controller       The controller on which to call Update().
   * @param controllerOutput The function which updates the plant using the
   *                         controller output passed as the argument.
   */
  PIDControllerRunner(PIDController& controller,
                      std::function<double(void)> measurementSource,
                      std::function<void(double)> controllerOutput);

  ~PIDControllerRunner();

  PIDControllerRunner(PIDControllerRunner&&) = default;
  PIDControllerRunner& operator=(PIDControllerRunner&&) = default;

  /**
   * Begin running the controller.
   */
  void Enable();

  /**
   * Stop running the controller.
   *
   * This sets the output to zero before stopping.
   */
  void Disable();

  /**
   * Returns whether controller is running.
   */
  bool IsEnabled() const;

 private:
  Notifier m_notifier{&PIDControllerRunner::Run, this};
  PIDController& m_controller;
  std::function<double(void)> m_measurementSource;
  std::function<void(double)> m_controllerOutput;
  bool m_enabled = false;

  mutable wpi::mutex m_thisMutex;

  // Ensures when Disable() is called, m_controllerOutput() won't run if
  // Controller::Update() is already running at that time.
  mutable wpi::mutex m_outputMutex;

  void Run();
};

}  // namespace experimental
}  // namespace frc
