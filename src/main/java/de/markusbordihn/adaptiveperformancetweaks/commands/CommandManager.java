/**
 * Copyright 2021 Markus Bordihn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.markusbordihn.adaptiveperformancetweaks.commands;

import com.mojang.brigadier.CommandDispatcher;
import org.apache.logging.log4j.Logger;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import de.markusbordihn.adaptiveperformancetweaks.Constants;
import de.markusbordihn.adaptiveperformancetweaks.Manager;

@EventBusSubscriber
public class CommandManager extends Manager {

  private static final Logger log = getLogger(CommandManager.class.getSimpleName());

  @SubscribeEvent
  public static void handleRegisterCommandsEvent(RegisterCommandsEvent event) {
    log.info("Registering /aptweaks commands ...");
    CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
    commandDispatcher.register(Commands.literal(Constants.MOD_COMMAND)
      // @formatter:off
        .then(CommandDebug.register())
        .then(CommandEntities.register())
        .then(CommandItems.register())
        .then(CommandKill.register())
        .then(CommandMonster.register())
        .then(CommandPlayerPositions.register())
        .then(CommandSpawnRules.register())
        .then(CommandSpawner.register())
        .then(CommandSpecialSpawnRules.register())
        .then(CommandVersion.register())
        .then(CommandWorlds.register())
      // @formatter:on
    );
  }

  public static void executeServerCommand(String command) {
    MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
    if (minecraftServer == null) {
      return;
    }
    log.debug("Execute Server Command: {}", command);
    Commands commands = minecraftServer.getCommands();
    CommandSource commandSource = minecraftServer.createCommandSourceStack().withSuppressedOutput();
    commands.performCommand(commandSource, command);
  }

  public static void executeUserCommand(String command) {
    MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
    if (minecraftServer == null) {
      return;
    }
    log.debug("Execute User Command: {}", command);
    Commands commands = minecraftServer.getCommands();
    CommandSource commandSource = minecraftServer.createCommandSourceStack();
    commands.performCommand(commandSource, command);
  }
}
