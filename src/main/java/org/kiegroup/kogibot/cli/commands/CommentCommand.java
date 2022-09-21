package org.kiegroup.kogibot.cli.commands;

import java.io.IOException;

import org.kohsuke.github.GHEventPayload;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;

@Command(name = "comment", description = "or `/comment`. Comment the current pull request.")
@Cli(name = "/comment", defaultCommand = CommentCommand.class)
public class CommentCommand implements PullRequestScopedCommands {

    @Override
    public void run(GHEventPayload.IssueComment issueCommentPayload) throws IOException {
        issueCommentPayload.getIssue().comment("This is the comment");
    }
}
