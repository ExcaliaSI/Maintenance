/*
 * Maintenance - https://git.io/maintenancemode
 * Copyright (C) 2018 KennyTV (https://github.com/KennyTV)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.kennytv.maintenance.core.proxy.command.subcommand;

import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import eu.kennytv.maintenance.core.proxy.command.ProxyCommandInfo;
import eu.kennytv.maintenance.core.runnable.MaintenanceRunnableBase;
import eu.kennytv.maintenance.core.util.SenderInfo;

import java.util.Collections;
import java.util.List;

public final class SingleStarttimerCommand extends ProxyCommandInfo {

    public SingleStarttimerCommand(final MaintenanceProxyPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean hasPermission(final SenderInfo sender) {
        return sender.hasMaintenancePermission("timer");
    }

    @Override
    public void execute(final SenderInfo sender, final String[] args) {
        if (args.length == 2) {
            if (checkPermission(sender, "timer")) return;
            if (plugin.getCommandManager().checkTimerArgs(sender, args[1], "starttimerUsage")) return;
            if (plugin.isMaintenance()) {
                sender.sendMessage(getMessage("alreadyEnabled"));
                return;
            }

            plugin.startMaintenanceRunnable(Integer.parseInt(args[1]), true);
            sender.sendMessage(getMessage("starttimerStarted").replace("%TIME%", plugin.getRunnable().getTime()));
        } else if (args.length == 3) {
            if (checkPermission(sender, "servertimer")) return;
            if (plugin.getCommandManager().checkTimerArgs(sender, args[2], "singleStarttimerUsage", false)) return;

            final Server server = plugin.getCommandManager().checkSingleTimerArgs(sender, args);
            if (server == null) return;
            if (plugin.isMaintenance(server)) {
                sender.sendMessage(getMessage("singleServerAlreadyEnabled").replace("%SERVER%", server.getName()));
                return;
            }

            final MaintenanceRunnableBase runnable = plugin.startSingleMaintenanceRunnable(server, Integer.parseInt(args[2]), true);
            sender.sendMessage(getMessage("starttimerStarted").replace("%TIME%", runnable.getTime()));
        } else
            sendHelp(sender);
    }

    @Override
    protected String[] helpMessage() {
        return fromStrings("§6/maintenance starttimer [server] <minutes> §7(After the given time in minutes, maintenance mode will be enabled)");
    }

    @Override
    public List<String> getTabCompletion(final String[] args) {
        return args.length == 2 ? plugin.getCommandManager().getServersCompletion(args[1].toLowerCase()) : Collections.emptyList();
    }
}
