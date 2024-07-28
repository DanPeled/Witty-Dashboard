package com.frcforftc.wittydashboard.sendables.opModeControl;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.Subsystem;
import com.frcforftc.wittydashboard.sendables.ftclib.CommandSendable;

import org.frcforftc.networktables.AnnounceMethod;
import org.frcforftc.networktables.sendable.SendableBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class OpModeControlSendable {
    List<CommandSendable> m_opModes;

    public void post(String key, AnnounceMethod announceMethod, SendableBuilder builder) {
        m_opModes = new ArrayList<>();

        for (String teleOpNames :
                OpModeRegistrarSendable.collectTeleopNames(OpModeRegistrarSendable.collectOpModesMeta().stream())) {
            m_opModes.add(new CommandSendable(new Command() {
                @Override
                public Set<Subsystem> getRequirements() {
                    return Collections.emptySet();
                }

                @Override
                public void execute() {
                    OpModeController.getOpModeManager().initOpMode(teleOpNames);
                }


                @Override
                public void cancel() {
                    OpModeController.getOpModeManager().stopActiveOpMode();
                }
            }));
        }


        for (CommandSendable sendable :
                m_opModes) {
            builder.post(key + "/" + sendable.getCommandName(), announceMethod);
        }
    }
}
