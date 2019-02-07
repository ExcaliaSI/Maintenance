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

package eu.kennytv.maintenance.core.command;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.util.SenderInfo;

import java.util.Collections;
import java.util.List;

public abstract class CommandInfo {
    protected final MaintenancePlugin plugin;
    private final String[] messages;
    private final String permission;

    protected CommandInfo(final MaintenancePlugin plugin, final String permission) {
        this.plugin = plugin;
        this.permission = permission;
        this.messages = helpMessage();
    }

    protected CommandInfo(final MaintenancePlugin plugin) {
        this(plugin, null);
    }

    public boolean hasPermission(final SenderInfo sender) {
        return permission == null || sender.hasMaintenancePermission(permission);
    }

    public String getPermission() {
        return permission;
    }

    public String[] getHelpMessage() {
        return messages;
    }

    public List<String> getTabCompletion(final String[] args) {
        return Collections.emptyList();
    }

    public abstract void execute(SenderInfo sender, String[] args);

    protected void sendHelp(final SenderInfo sender) {
        for (final String message : messages) {
            sender.sendMessage(message);
        }
    }

    protected boolean checkPermission(final SenderInfo sender, final String permission) {
        if (!sender.hasMaintenancePermission(permission)) {
            sender.sendMessage(getMessage("noPermission"));
            return true;
        }
        return false;
    }

    protected boolean checkArgs(final SenderInfo sender, final String[] args, final int length) {
        if (args.length != length) {
            sendHelp(sender);
            return true;
        }
        return false;
    }

    protected boolean isNumeric(final String string) {
        return string.matches("[0-9]+");
    }

    protected String[] fromStrings(final String... s) {
        return s;
    }

    protected String getMessage(final String s) {
        return plugin.getSettings().getMessage(s);
    }

    protected Settings getSettings() {
        return plugin.getSettings();
    }

    protected abstract String[] helpMessage();
}
