cmake_minimum_required(VERSION 3.28)
project(lab7)

set(CMAKE_CXX_STANDARD 17)

find_package(MPI REQUIRED)

add_executable(lab7 main.cpp)

target_link_libraries(lab7 PUBLIC MPI::MPI_CXX)
