package org.example.lidyvariedadesdesktop;

import javafx.application.Application;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Launcher {

    private static Process backendProcess;

    public static void main(String[] args) {
        if (!isBackendRodando()) {
            iniciarBackend();
        }

        Application.launch(HelloApplication.class, args);
    }

    private static boolean isBackendRodando() {
        try (Socket socket = new Socket("localhost", 8080)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static void iniciarBackend() {
        try {
            File currentDir = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            File backendJar = new File(currentDir, "backend.jar");

            if (!backendJar.exists()) {
                File parentDir = currentDir.getParentFile();
                if (parentDir != null && new File(parentDir, "backend.jar").exists()) {
                    backendJar = new File(parentDir, "backend.jar");
                } else if (new File(currentDir, "app/backend.jar").exists()) {
                    backendJar = new File(currentDir, "app/backend.jar");
                }
            }

            if (backendJar.exists()) {
                String javaExec = "java";
                File parentDir = currentDir.getParentFile();
                if (parentDir != null && new File(parentDir, "runtime/bin/java.exe").exists()) {
                    javaExec = new File(parentDir, "runtime/bin/java.exe").getAbsolutePath();
                } else if (new File(currentDir, "runtime/bin/java.exe").exists()) {
                    javaExec = new File(currentDir, "runtime/bin/java.exe").getAbsolutePath();
                }

                System.out.println("Iniciando backend: " + backendJar.getAbsolutePath() + " com " + javaExec);
                ProcessBuilder pb = new ProcessBuilder(javaExec, "-jar", backendJar.getAbsolutePath());
                pb.directory(backendJar.getParentFile());
                backendProcess = pb.start();

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    if (backendProcess != null && backendProcess.isAlive()) {
                        backendProcess.destroy();
                    }
                }));

                for (int i = 0; i < 30; i++) {
                    if (isBackendRodando()) {
                        break;
                    }
                    Thread.sleep(500);
                }
            } else {
                System.out.println("Aviso: backend.jar nao encontrado.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao iniciar backend automaticamente: " + e.getMessage());
        }
    }
}
