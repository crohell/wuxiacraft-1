package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class ProgressCommand extends CommandBase {

    @Override
    @Nonnull
    public String getName() {
        return "progress";
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "/progress set <player> <amount>";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) sender;
            if(!playerMP.world.isRemote) {
                boolean wrongUsage = true;
                if (args.length == 3) {
                    EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
                    if(targetPlayer != null) {
                        ICultivation cultivation = CultivationUtils.getCultivationFromEntity(targetPlayer);
                        double amount = Double.parseDouble(args[2]);
                        if(args[0].equalsIgnoreCase("set")) {
                            cultivation.setProgress(amount);
                            wrongUsage = false;
                        } else if(args[0].equalsIgnoreCase("add")) {
                            cultivation.addProgress(amount, true);
                            wrongUsage = false;
                        }
                    } else {
                        TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
                        text.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(text);
                        wrongUsage = true;
                    }
                }
                if (wrongUsage) {
                    TextComponentString text = new TextComponentString("Invalid arguments, use /progress set <player> <amount>");
                    text.getStyle().setColor(TextFormatting.RED);
                    sender.sendMessage(text);
                }
            }
        } else throw new CommandException("Not used correctly!");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = new ArrayList<>();
        if(args.length == 1) {
            if("set".toLowerCase().startsWith(args[0])) {
                completions.add("set");
            }
        }else if(args.length == 2) {
            for(String player : server.getPlayerList().getOnlinePlayerNames()) {
                if(player.toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(player);
                }
            }
        }
        return completions;
    }
}
