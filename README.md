# Project README

## Overview

This repository contains implementations of various algorithms, each contained in its respective folder. The goal is to provide a unified environment for testing and analyzing algorithms, complete with configurable parameters and comprehensive output statistics.

## How to Run
1. **JDK Setup**  
   The code is able to run on any JDK available in intelliJ and Eclipse, after opening the project, click on add JDK and the code will be ready to run.

2. **Navigate to the Algorithm Folder**  
   Each algorithm is organized in its own folder. To run a specific algorithm, navigate to its directory.

3. **Find the `Simulator` Class**  
   Within each algorithm's directory, you'll find a `Simulator` class which serves as the main entry point for running that algorithm.

4. **Configure Parameters**  
   Inside the `Simulator` class, locate the `main` function. This function contains the parameters that can be modified based on your specific requirements.

5. **Execute the `Simulator`**  
   Once parameters are configured, run the `Simulator` class to execute the algorithm. 

6. **Example output:**
```shell
Problem Name: TEST1234
Execution Time: 12345ms
Used Memory: 12345 bytes
Bins: [[a 2D array of bins]]
Number of Bins Used: 100
Mean Wastage per Bin: 1234