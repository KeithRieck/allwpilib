package edu.wpi.first.wpilibj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.wpilibj.experimental.controller.PIDController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PIDInputOutputTest {
  private PIDController m_controller;

  @BeforeEach
  void setUp() {
    m_controller = new PIDController(0, 0, 0);
  }

  @Test
  void outputRangeTest() {
    m_controller.setP(1);
    m_controller.setOutputRange(-50, 50);

    assertEquals(-50, m_controller.calculate(100, 0), 1e-5);
    assertEquals(50, m_controller.calculate(0, 100), 1e-5);
  }

  @Test
  void inputRangeTest() {
    m_controller.setP(1);
    m_controller.setOutputRange(-1000, 1000);
    m_controller.setInputRange(-50, 50);

    assertEquals(-100, m_controller.calculate(100, 0), 1e-5);
    assertEquals(50, m_controller.calculate(0, 100), 1e-5);
  }

  @Test
  void continuousInputTest() {
    m_controller.setP(1);
    m_controller.setInputRange(-180, 180);
    m_controller.setContinuous(true);

    assertTrue(m_controller.calculate(-179, 179) < 0.);
  }

  @Test
  void proportionalGainOutputTest() {
    m_controller.setP(4);

    double out = m_controller.calculate(.025, 0);

    assertEquals(-.1, out, 1e-5);
  }

  @Test
  void integralGainOutputTest() {
    m_controller.setI(4);

    double out = 0;

    for (int i = 0; i < 5; i++) {
      out = m_controller.calculate(.025, 0);
    }

    assertEquals(-.5*m_controller.getPeriod(), out, 1e-5);
  }

  @Test
  void derivativeGainOutputTest() {
    m_controller.setD(4);

    m_controller.calculate(0, 0);
    double out = m_controller.calculate(.0025, 0);

    assertEquals(-.01/m_controller.getPeriod(), out, 1e-5);
  }
}