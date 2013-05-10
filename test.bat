@echo off

set servers=127.0.0.1,192.168.0.1,10.100.0.1

for %%i in (%servers%) do (
  echo %%i
)