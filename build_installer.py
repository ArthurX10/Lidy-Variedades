import os
import shutil
import subprocess

jdk_bin = r"C:\Program Files\Java\jdk-26.0.2\bin"
jpackage_exe = os.path.join(jdk_bin, "jpackage.exe")
wix_dir = r"C:\Users\Arthur\Downloads\wix311"

base_dir = os.path.dirname(os.path.abspath(__file__))
desktop_target = os.path.join(base_dir, "desktop", "target")
backend_jar = os.path.join(base_dir, "backend", "target", "LidyVariedades-0.0.1-SNAPSHOT.jar")

input_dir = r"C:\Users\Arthur\Downloads\package_input"
output_dir = r"C:\Users\Arthur\Downloads\Instalador_LidyVariedades"

if os.path.exists(input_dir):
    shutil.rmtree(input_dir)
os.makedirs(input_dir, exist_ok=True)

if os.path.exists(output_dir):
    shutil.rmtree(output_dir)
os.makedirs(output_dir, exist_ok=True)

print("1. Copiando arquivos para o diretório de empacotamento...")
desktop_jar = os.path.join(desktop_target, "LidyVariedades-Desktop-1.0-SNAPSHOT.jar")
shutil.copy(desktop_jar, os.path.join(input_dir, "LidyVariedades-Desktop.jar"))

lib_dir = os.path.join(desktop_target, "dependency")
for f in os.listdir(lib_dir):
    if f.endswith(".jar"):
        shutil.copy(os.path.join(lib_dir, f), os.path.join(input_dir, f))

shutil.copy(backend_jar, os.path.join(input_dir, "backend.jar"))

print("Arquivos prontos no input_dir:", len(os.listdir(input_dir)))

print("2. Gerando o instalador Windows com jpackage...")
env = os.environ.copy()
env["PATH"] = wix_dir + ";" + jdk_bin + ";" + env.get("PATH", "")

cmd = [
    jpackage_exe,
    "--type", "exe",
    "--name", "LidyVariedades",
    "--app-version", "1.0",
    "--vendor", "Lidy Variedades",
    "--description", "Sistema de Gestao e Estoque - Lidy Variedades",
    "--input", input_dir,
    "--main-jar", "LidyVariedades-Desktop.jar",
    "--main-class", "org.example.lidyvariedadesdesktop.Launcher",
    "--win-shortcut",
    "--win-menu",
    "--win-dir-chooser",
    "--dest", output_dir
]

result = subprocess.run(cmd, env=env, capture_output=True, text=True)

if result.returncode == 0:
    print("\nSUCESSO TOTAL! Instalador gerado em:")
    for f in os.listdir(output_dir):
        print(" -", os.path.join(output_dir, f))
else:
    print("\nFalha ao gerar o instalador.")
    print("STDOUT:\n", result.stdout)
    print("STDERR:\n", result.stderr)
    exit(1)
