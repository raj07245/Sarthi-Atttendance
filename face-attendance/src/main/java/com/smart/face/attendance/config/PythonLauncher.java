package com.smart.face.attendance.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Component
public class PythonLauncher {

    @PostConstruct
    public void startPythonService() {
        try {
            // Absolute path to your Python executable inside venv
            String pythonPath = new File("face-engine/venv/bin/python").getAbsolutePath();
            // For Windows, it might be "face-engine\\venv\\Scripts\\python.exe"

            // Absolute path to app.py
            String pythonScript = new File("face-engine/app.py").getAbsolutePath();

            // Command to run
            ProcessBuilder pb = new ProcessBuilder(pythonPath, pythonScript);
            pb.directory(new File(".")); // Set working directory to project root
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Optional: Read Python output and print in Spring Boot console
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[PYTHON] " + line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            System.out.println("✅ Python face recognition service launched successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Failed to start Python face recognition service!");
        }
    }
}