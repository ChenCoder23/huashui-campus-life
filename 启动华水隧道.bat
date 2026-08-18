@echo off
chcp 65001 >nul
title Huashui Tunnel - MySQL(3308) + Nacos(18848) + gRPC(19848) + Console(18080)
echo ================================
echo   Huashui SSH Tunnel
echo   MySQL        -> localhost:3308
echo   Nacos HTTP    -> localhost:18848
echo   Nacos gRPC    -> localhost:19848
echo   Nacos Console -> http://localhost:18080
echo ================================
echo.
echo Type password, keep this window open.
echo.

ssh -L 3308:localhost:3306 -L 18848:localhost:8848 -L 19848:localhost:9848 -L 18080:localhost:8080 ubuntu@43.143.130.165 -N

pause
