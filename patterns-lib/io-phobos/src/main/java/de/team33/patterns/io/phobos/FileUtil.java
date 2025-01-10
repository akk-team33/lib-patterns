package de.team33.patterns.io.phobos;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {

    private static final String NEW_LINE = String.format("%n");

    public static String report(final FileIndex index) {
        return index.roots()
                    .stream()
                    .map(FileUtil::report)
                    .collect(Collectors.joining(NEW_LINE));
    }

    public static String report(final FileEntry entry) {
        return report(entry, 0);
    }

    private static String report(final FileEntry entry, final int indent) {
        final List<String> result = new LinkedList<>();
        final String format = (indent == 0) ? "%s%s : %s"
                                            : String.format("%%%ds%%s : %%s", 4 * indent);
        final String name = (indent == 0) ? entry.path().toString()
                                          : entry.name();
        final String info = infoOf(entry);
        result.add(String.format(format, "", name, info));
        if (FileType.DIRECTORY == entry.type()) {
            entry.entries()
                 .map(e -> report(e, indent + 1))
                 .forEach(result::add);
        }
        return String.join(NEW_LINE, result);
    }

    private static String infoOf(final FileEntry entry) {
        return String.format(format(entry.type()), argsOf(entry));
    }

    private static Object[] argsOf(final FileEntry entry) {
        switch (entry.type()) {
            case REGULAR:
            case DIRECTORY:
                return arrayOf(entry.type(), entry.totalSize(), entry.lastUpdated());
            default:
                return arrayOf(entry.type());
        }
    }

    private static Object[] arrayOf(final Object ... args) {
        return args;
    }

    private static String format(final FileType type) {
        switch (type) {
            case REGULAR:
                return "%s (%,d, %s);";
            case DIRECTORY:
                return "%s (%,d, %s) ...";
            default:
                return "%s;";
        }
    }
}
