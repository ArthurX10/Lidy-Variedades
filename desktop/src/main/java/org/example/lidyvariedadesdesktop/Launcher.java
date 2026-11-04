package org.example.lidyvariedadesdesktop;

import javafx.application.Application;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
                // Lê as credenciais do banco a partir do db.properties
                java.util.Properties dbProps = carregarConfiguracaoBanco(backendJar.getParentFile());

                String dbUrl      = dbProps.getProperty("db.url", "").trim();
                String dbUsername = dbProps.getProperty("db.username", "").trim();
                String dbPassword = dbProps.getProperty("db.password", "").trim();

                // Descobre o executável java correto (bundled runtime ou sistema)
                String javaExec = "java";
                File appDir = backendJar.getParentFile();
                File appParent = appDir.getParentFile();
                if (appParent != null && new File(appParent, "runtime/bin/java.exe").exists()) {
                    javaExec = new File(appParent, "runtime/bin/java.exe").getAbsolutePath();
                } else if (new File(appDir, "runtime/bin/java.exe").exists()) {
                    javaExec = new File(appDir, "runtime/bin/java.exe").getAbsolutePath();
                }

                System.out.println("Iniciando backend: " + backendJar.getAbsolutePath());
                ProcessBuilder pb = new ProcessBuilder(
                    javaExec,
                    "-DDB_URL=" + dbUrl,
                    "-DDB_USERNAME=" + dbUsername,
                    "-DDB_PASSWORD=" + dbPassword,
                    "-jar", backendJar.getAbsolutePath()
                );
                pb.directory(appDir);
                pb.redirectErrorStream(true);
                backendProcess = pb.start();

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    if (backendProcess != null && backendProcess.isAlive()) {
                        backendProcess.destroy();
                    }
                }));

                // Aguarda o backend estar pronto (até 30 segundos)
                System.out.println("Aguardando backend iniciar...");
                for (int i = 0; i < 60; i++) {
                    if (isBackendRodando()) {
                        System.out.println("Backend iniciado com sucesso!");
                        break;
                    }
                    Thread.sleep(500);
                }
            } else {
                System.out.println("Aviso: backend.jar nao encontrado em " + currentDir.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Erro ao iniciar backend: " + e.getMessage());
        }
    }

    /**
     * Carrega as configurações do banco do arquivo db.properties.
     * Se o arquivo não existir ou estiver incompleto, abre um diálogo
     * de configuração e salva os dados para as próximas execuções.
     */
    private static java.util.Properties carregarConfiguracaoBanco(File dir) {
        File configFile = new File(dir, "db.properties");
        java.util.Properties props = new java.util.Properties();

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                props.load(reader);
                String url  = props.getProperty("db.url", "").trim();
                String user = props.getProperty("db.username", "").trim();
                // Configurações completas — pode usar direto
                if (!url.isEmpty() && !user.isEmpty()) {
                    System.out.println("Configuracao de banco carregada de: " + configFile.getAbsolutePath());
                    return props;
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler db.properties: " + e.getMessage());
            }
        }

        // Arquivo ausente ou incompleto → pede ao usuário
        String[] resultado = mostrarDialogoConfiguracao(props);

        if (resultado != null) {
            props.setProperty("db.url",      resultado[0]);
            props.setProperty("db.username", resultado[1]);
            props.setProperty("db.password", resultado[2]);

            try (FileWriter writer = new FileWriter(configFile)) {
                props.store(writer, "Configuracao do Banco de Dados - Lidy Variedades");
                System.out.println("Configuracoes salvas em: " + configFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Nao foi possivel salvar db.properties: " + e.getMessage());
            }
        }

        return props;
    }

    /**
     * Exibe um diálogo Swing simples para o usuário informar as credenciais
     * do banco de dados. Retorna {url, username, password} ou null se cancelado.
     */
    private static String[] mostrarDialogoConfiguracao(java.util.Properties props) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        javax.swing.JTextField txtUrl = new javax.swing.JTextField(
            props.getProperty("db.url", "jdbc:postgresql://HOST:5432/NOME_DO_BANCO"), 38);
        javax.swing.JTextField txtUser = new javax.swing.JTextField(
            props.getProperty("db.username", ""), 25);
        javax.swing.JPasswordField txtPass = new javax.swing.JPasswordField(
            props.getProperty("db.password", ""), 25);

        Object[] message = {
            "Primeira execução — configure a conexão com o banco de dados.",
            "Esta configuração será salva e não será pedida novamente.",
            " ",
            "URL de Conexão:", txtUrl,
            "Usuário:", txtUser,
            "Senha:", txtPass
        };

        int opt = javax.swing.JOptionPane.showConfirmDialog(
            null,
            message,
            "Lidy Variedades — Configuração do Banco de Dados",
            javax.swing.JOptionPane.OK_CANCEL_OPTION,
            javax.swing.JOptionPane.PLAIN_MESSAGE
        );

        if (opt == javax.swing.JOptionPane.OK_OPTION) {
            return new String[]{
                txtUrl.getText().trim(),
                txtUser.getText().trim(),
                new String(txtPass.getPassword()).trim()
            };
        }
        return null;
    }
}
