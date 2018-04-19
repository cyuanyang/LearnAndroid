
cmake_minimum_required(VERSION 3.4.1)
# project("CMAKE_HELLO")
MESSAGE("hello word ========= \n")

# 打印信息
message("PROJECT_SOURCE_DIR=${PROJECT_SOURCE_DIR}")

message("PROJECT_BINARY_DIR=${PROJECT_BINARY_DIR}")

message("CMAKE_CACHEFILE_DIR = ${CMAKE_CACHEFILE_DIR}")

message("PROJECT_NAME = ${PROJECT_NAME}")

# 给变量赋值 set

SET(VAR_TEST1 FFFFFF)

SET(PATH ${PROJECT_SOURCE_DIR})

message("VAR_TEST1 = ${VAR_TEST1}")
message("PATH = ${PATH}")

add_library(cmakedemo SHARED src/main/cpp/cmakedemo.cpp)

target_link_libraries(
                       cmakedemo
                       log )

