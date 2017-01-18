import org.apache.commons.cli.*;
import java.util.Properties;

/**
 * Created by Levi Muniz on 10/30/16.
 */
public class CliParser {

    private Options opt = new Options();

    public CliParser(String[] args, Properties properties) {
        opt.addOption("h", "help", false, "Display application's help message.");
        opt.addOption("m", "masterIP", true, "IP of master (if self, use 127.0.0.1 or own IP).");
        opt.addOption("i", "interface", true, "Preferred interface to allow connections on.");
        opt.addOption("r", "regenConfig", false, "Regenerate application's default configuration file.");
        opt.addOption("nogui", false, "Run MeshFS without graphical user interface (server mode only)");
        opt.addOption("reconfig", false, "Reconfigure MeshFS graphically");
        try {
            CommandLine cmd = (new DefaultParser()).parse(opt, args);

            if (cmd.hasOption("h")) {
                help();
            }

            if (cmd.hasOption("m")) {
                properties.setProperty("masterIP", cmd.getOptionValue("m"));
            }

            if (cmd.hasOption("r")) {
                ConfigParser.loadDefaultProperties();
            }

            if (cmd.hasOption("nogui")) {
                MeshFS.nogui = true;
            }

            if (cmd.hasOption("reconfig")) {
                MeshFS.configure = true;
            }

        } catch (ParseException e) {
            help();
        }
    }

    private void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MeshFS [options]", opt);
        System.exit(0);
    }
}
