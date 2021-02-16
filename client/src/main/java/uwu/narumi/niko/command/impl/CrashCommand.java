package uwu.narumi.niko.command.impl;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import uwu.narumi.niko.Niko;
import uwu.narumi.niko.command.Command;
import uwu.narumi.niko.command.CommandInfo;
import uwu.narumi.niko.exception.CommandException;
import uwu.narumi.niko.exploit.Exploit;
import uwu.narumi.niko.exploit.argument.ArgumentParser;
import uwu.narumi.niko.helper.ChatHelper;

@CommandInfo(
    alias = "crash",
    description = ":D",
    usage = ".crash <method/list> [method arguments]",
    aliases = {"exploit", "lag"}
)
public class CrashCommand extends Command {

  @Override
  public void execute(String... args) throws CommandException {
    if (args.length == 0) {
      throw new CommandException("Usage: &d" + getUsage());
    }

    if (args[0].equalsIgnoreCase("list")) {
      ChatHelper.printMessage(
          "Available methods: &d" + Niko.INSTANCE.getExploitManager().getExploits().stream()
              .map(Exploit::getName).collect(Collectors.joining(", ")));
    } else if (args[0].equalsIgnoreCase("info") && args.length > 1) {
      Niko.INSTANCE.getExploitManager().getExploit(args[1])
          .ifPresentOrElse(exploit -> ChatHelper.printMessage(
              String.format("&5%s&f: &d%s\n", exploit.getName(), exploit.getDescription())),
              () -> ChatHelper.printMessage(String.format("Exploit \"%s\" not found.\n", args[0])));
    } else {
      Optional<Exploit<?>> exploit = Niko.INSTANCE.getExploitManager().getExploit(args[0]);
      if (exploit.isPresent()) {
        exploit.get().execute(
            ArgumentParser.parseArgs(exploit.get(), Arrays.copyOfRange(args, 1, args.length)));
        return;
      }

      throw new CommandException("Exploit not found. Use \".crash list\" to see all exploits.");
    }
  }
}
