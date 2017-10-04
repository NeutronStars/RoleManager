package fr.neutronstars.rolemanager.command;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.command.Command;
import fr.neutronstars.nbot.command.SimpleCommand;
import fr.neutronstars.nbot.entity.Channel;
import fr.neutronstars.nbot.entity.Message;
import fr.neutronstars.nbot.entity.User;
import fr.neutronstars.rolemanager.RoleManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;

/**
 * Created by NeutronStars on 23/09/2017
 */
public class RoleCommand
{
    private final RoleManager roleManager;

    public RoleCommand(RoleManager roleManager)
    {
        this.roleManager = roleManager;
    }

    @Command(name = "role", guilds={238975753969074177L})
    private void onRole(SimpleCommand command, User user, final Channel channel, String[] args, Message message)
    {
        roleManager.setGuild();

        if(args.length == 0)
        {
            if(roleManager.hasMessage(channel.getIdLong())) roleManager.getMessage(channel.getIdLong()).deleteTheMessage();
            channel.sendMessage("Cliques sur l'un des emojis pour choisir un role.").queue(message1 -> {
                roleManager.setMessage(channel.getIdLong(), new Message(message1));

                for(String emote : roleManager.getEmotesId()) message1.addReaction(NBot.getJDA().getEmoteById(emote)).queue();
            });
            return;
        }

        if(!roleManager.getGuild().getMember(user.getUser()).hasPermission(Permission.ADMINISTRATOR)) return;

        try {
            Emote emote = NBot.getJDA().getEmoteById(args[0]);
            if(emote == null)
            {
                channel.sendMessageToChannel(command.getSimpleName()+" <EmoteID> <Role>");
                return;
            }

            if(message.getMentionedRoles().size() == 0)
            {
                if(roleManager.hasReaction(emote.getId()))
                {
                    roleManager.removeEmote(emote.getId());
                    channel.sendMessageToChannel("Role-Emote supprimé.");
                    return;
                }
                channel.sendMessageToChannel(command.getSimpleName()+" <EmoteID> <Role>");
                return;
            }

            roleManager.addRoleEmote(args[0], message.getMentionedRoles().get(0).getIdLong());
            channel.sendMessageToChannel("Role ajouté : "+message.getMentionedRoles().get(0).getAsMention()+" >> "+emote.getAsMention());
        }catch(Exception e)
        {
            channel.sendMessageToChannel(command.getSimpleName()+" <EmoteID> <Role>");
        }
    }
}
