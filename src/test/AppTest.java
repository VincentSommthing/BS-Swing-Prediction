package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import program.Program;


public class AppTest {
    @Test
    public void idkTest(TestReporter reporter) {
        reporter.publishEntry("hi");
    }
}
