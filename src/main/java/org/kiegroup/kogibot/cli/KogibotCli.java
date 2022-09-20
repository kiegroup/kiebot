package org.kiegroup.kogibot.cli;

import java.io.IOException;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;
import io.quarkiverse.githubapp.command.airline.AbstractHelpCommand;
import io.quarkiverse.githubapp.command.airline.CliOptions;
import io.quarkiverse.githubapp.command.airline.CommandOptions;
import org.kiegroup.kogibot.cli.KogibotCli.HelpCommand;
import org.kiegroup.kogibot.cli.commands.CommentCommand;
import org.kiegroup.kogibot.cli.commands.PullRequestScopedCommands;
import org.kohsuke.github.GHEventPayload;

@Cli(name = "@bot", commands = {
    CommentCommand.class,
    HelpCommand.class
}, description = "Kogibot main CLI. Run `/bot help` or `@bot help` for getting all available commands.")
@CliOptions(defaultCommandOptions = @CommandOptions(scope = CommandOptions.CommandScope.PULL_REQUESTS), aliases = {
    "@kogibot", "@kogitobot", "/bot", "/kogibot", "/kogitobot" })
class KogibotCli {

  @Command(name = "help", description = "Print help")
  static class HelpCommand extends AbstractHelpCommand implements PullRequestScopedCommands {

    @Override
    public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
      super.run(issueCommentPayload);
    }
  }
}
