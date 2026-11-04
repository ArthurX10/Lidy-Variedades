@echo off
chcp 65001 >nul
title Gerando Executavel - Lidy Variedades
color 0A

echo.
echo  ============================================================
echo       GERANDO INSTALADOR EXECUTAVEL (.EXE)
echo             LIDY VARIEDADES
echo  ============================================================
echo.

:: Configura o JAVA_HOME para o JDK correto
set JAVA_HOME=C:\Program Files\Java\jdk-26.0.2
set PATH=%JAVA_HOME%\bin;C:\Users\Arthur\Downloads\wix311;%PATH%

:: Verifica se o JDK existe
if not exist "%JAVA_HOME%\bin\java.exe" (
    echo [ERRO] JDK nao encontrado em: %JAVA_HOME%
    echo        Instale o JDK 21+ e ajuste o caminho no inicio deste arquivo.
    pause
    exit /b 1
)

:: Verifica se o Python existe
where python >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERRO] Python nao encontrado. Instale o Python 3 e tente novamente.
    pause
    exit /b 1
)

echo [1/3] Compilando Backend Spring Boot...
cd /d "%~dp0backend"
call mvnw.cmd clean package -DskipTests -q
if %errorlevel% neq 0 (
    echo.
    echo [ERRO] Falha ao compilar o Backend. Veja os erros acima.
    pause
    exit /b %errorlevel%
)
echo       Backend compilado com sucesso!

echo.
echo [2/3] Compilando Desktop JavaFX...
cd /d "%~dp0desktop"
call mvnw.cmd clean package dependency:copy-dependencies -DskipTests -q
if %errorlevel% neq 0 (
    echo.
    echo [ERRO] Falha ao compilar o Desktop. Veja os erros acima.
    pause
    exit /b %errorlevel%
)
echo       Desktop compilado com sucesso!

echo.
echo [3/3] Criando instalador Windows (.exe) com jpackage...
cd /d "%~dp0"
python "%~dp0build_installer.py"
if %errorlevel% neq 0 (
    echo.
    echo [ERRO] Falha ao criar o instalador. Veja os erros acima.
    pause
    exit /b %errorlevel%
)

echo.
echo  ============================================================
echo   INSTALADOR GERADO COM SUCESSO!
echo.
echo   Local: C:\Users\Arthur\Downloads\Instalador_LidyVariedades\
echo  ============================================================
echo.
pause
