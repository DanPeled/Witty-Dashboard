# Witty Dashboard
> A port of WPILib's [ntcore]([https://github.com/wpilibsuite/ntcore](https://github.com/wpilibsuite/allwpilib/tree/main/ntcore)) library for the FTC robotics competition

# What's ntcore?

WPILib's [ntcore]([https://github.com/wpilibsuite/ntcore](https://github.com/wpilibsuite/allwpilib/tree/main/ntcore)) library is the main library used in the FRC robotics competition for communicating between the different dashboards and processing modules (Such as [Elastic](https://github.com/Gold872/elastic-dashboard) or a rasberry pi).
The library allows for easy communication between devices and data transfer between them.

## How is it different from [FTC Dashboard](https://github.com/acmerobotics/ftc-dashboard)

The ntcore library allows for easier usage of data transfer over how the acmerobotics FTC Dashboard approach is, instead of having to declare all of the exposed fields as static and public, you are able to call the publish method directly.

In addition, the ntcore library is only the communication framework between the dashboard and the robot, with no actual dashboard being hosted on the robot itself, which has close to minimal impact on the robot's performance, while on the other side the acmerobotics FTC Dashboard is being fully hosted on the robot together with the framework, which allows for no need to install additional software for the viewing computer, but has a much larger impact on performance.


# Getting Started
## Interacting With the library
Sample Code: 
```java
package org.firstinspires.ftc.teamcode;

import org.frcforftc.wittydashboard.WittyDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TeleOpTest extends LinearOpMode {
    int foo = 3; // Let's create a variable that we will publish as a const int to the table
    double bar = 7.0; // And another that we will publish with a setter

    @Override
    public void runOpMode() throws InterruptedException {
        /* Here we're starting the NetworkTable's server.
           We are providing the opMode instance in order for the library to automatically
           publish data about the opMode
           such as the runtime of the opmode, isStarted data and hardwaremap data,
           that data is found in the WittyDashboard/OpMode topic
        */
        WittyDashboard.start(this); 

        waitForStart();
        
        while (opModeIsActive() && !isStopRequested()) {
            /* We are putting the value onto the table with the key of "Foo" and the value of
               the foo variable.
               Since we haven't provided a setter as an additional argument for the function,
               it will be published as a const int, and changing the data through the network
               table wont modify the original value.
               Aswell, because we're putting the value inside of the main loop, any change in
               the network table will be overwritten in the next loop iteration
            */
            WittyDashboard.put("Foo", foo); 

            /* Here we have also provided a setter for the entry.
               Now, whenever we change the value of the Bar entry in the table,
               it will also affect the bar variable in this class 
            */
            WittyDashboard.put("Bar", bar, this::setBar);
        }

        // Finally, we are stopping the server to clean up the data
        WittyDashboard.stop();
    }

    /**
    * A setter function for the bar variable,
    * this function will be called whenever we change the value of the bar variable inside of
    * the
    * network table
    **/
    public void setBar(double newValue) {
        this.bar = newValue;
    }
}

```

## Installation
### Installation using gradle
Coming soon...

### Manual installation
For manual installation, you are able to simply download the repository and add it in a folder called `wittydashboard` in the root folder of your robot's code like this: 
```
- FTCRobotController/
- TeamCode/
- wittydashboard/
```

next, go to your TeamCode directory's `build.gradle` and add in the dependencies the following: 
```groovy
dependencies {
    // ... 
    implementation project(':wittydashboard')
}
```
next, go to the `build.dependencies.gradle` file found in the root directory and in the repositories block add the following lines: 
```gradle
repositories {
// ...
maven {
        url "https://repo.dairy.foundation/releases" // For coming soon features, isn't required for now
    }
maven {
        url uri("https://frcmaven.wpi.edu/release")
    }
// ...
```

finally, add the include of the project in the root `settings.gradle` and press Sync (Will appear at the top right corner with the Gradle elephant icon): 
```groovy
// ...
include ':wittydashboard'
```

