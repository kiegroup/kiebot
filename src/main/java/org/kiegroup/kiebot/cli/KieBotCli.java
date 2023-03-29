package org.kiegroup.kiebot.cli;

import java.io.IOException;

import org.kiegroup.kiebot.cli.KieBotCli.HelpCommand;
import org.kiegroup.kiebot.cli.commands.ApproveCommand;
import org.kiegroup.kiebot.cli.commands.BackportCommand;
import org.kiegroup.kiebot.cli.commands.CloseCommand;
import org.kiegroup.kiebot.cli.commands.OpenCommand;
import org.kiegroup.kiebot.cli.commands.PullRequestScopedCommands;
import org.kohsuke.github.GHEventPayload;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Group;

import io.quarkiverse.githubapp.command.airline.AbstractHelpCommand;
import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;

@Cli(
        name = "@bot",
        commands = {
                BackportCommand.class,
                CloseCommand.class,
                OpenCommand.class,
                HelpCommand.class
        },
        groups = {
                @Group(name = "approve", commands = {
                        ApproveCommand.ApprovePullRequestCommand.class,
                        ApproveCommand.ApproveCancelPullRequestCommand.class
                },
                        description = "or `/approve`. Values: `add` or `cancel`. Default is `add`.",
                        defaultCommand = ApproveCommand.ApprovePullRequestCommand.class)
        },
        description = "Kiebot main CLI. Run `/bot help` or `@bot help` for getting all available commands.")
@CliOptions(
        defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS),
        aliases = { "@kiebot", "/bot", "/kiebot" })
class KieBotCli {

    @Command(name = "help", description = "Print help")
    static class HelpCommand extends AbstractHelpCommand implements PullRequestScopedCommands {

        @Override
        public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
            super.run(issueCommentPayload);
        }
    }
}
