package de.team33.patterns.io.phobos;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
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
        final String info = InfoType.valueOf(entry.type().name())
                                    .info(entry);
        result.add(String.format(format, "", name, info));
        if ((FileType.DIRECTORY == entry.type()) && (0 < entry.entries().size())) {
            entry.entries()
                 .stream()
                 .map(e -> report(e, indent + 1))
                 .forEach(result::add);
        }
        return String.join(NEW_LINE, result);
    }

    private enum InfoType {

        MISSING(entry -> entry.type() + ";"),
        REGULAR(entry -> String.format("%s (%,d, %s);", entry.type(), entry.totalSize(), entry.lastUpdated())),
        DIRECTORY(entry -> String.format("%s (%,d, %s) ...", entry.type(), entry.totalSize(), entry.lastUpdated())),
        SYMBOLIC(entry -> entry.type() + ";"),
        SPECIAL(entry -> entry.type() + ";");

        private final Function<FileEntry, String> toInfo;

        InfoType(Function<FileEntry, String> toInfo) {
            this.toInfo = toInfo;
        }

        final String info(final FileEntry entry) {
            return toInfo.apply(entry);
        }
    }
}
