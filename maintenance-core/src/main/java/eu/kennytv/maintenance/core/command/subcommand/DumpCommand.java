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

package eu.kennytv.maintenance.core.command.subcommand;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;

import java.util.concurrent.TimeUnit;

public final class DumpCommand extends CommandInfo {
    private long lastDump;

    public DumpCommand(final MaintenancePlugin plugin) {
        super(plugin, "admin");
    }

    @Override
    public void execute(final SenderInfo sender, final String[] args) {
        if (checkArgs(sender, args, 1)) return;
        if (System.currentTimeMillis() - lastDump < TimeUnit.MINUTES.toMillis(5)) {
            sender.sendMessage(plugin.getPrefix() + "§cYou can only create a dump every 5 minutes!");
            return;
        }

        lastDump = System.currentTimeMillis();
        plugin.async(() -> {
            sender.sendMessage(plugin.getPrefix() + "§7The dump is being created, this might take a moment.");
            final String key = plugin.pasteDump();
            if (key == null) {
                if (sender.isPlayer())
                    sender.sendMessage(plugin.getPrefix() + "§cCould not paste dump (see the console for details)");
                return;
            }

            plugin.getCommandManager().sendDumpMessage(sender, "https://hasteb.in/" + key);
        });
    }

    @Override
    protected String[] helpMessage() {
        return fromStrings("§6/maintenance dump §7(Dumps some server information, used for bug reports)");
    }
}
