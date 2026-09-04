package de.team33.patterns.files.pluto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class FileTypeTest {

    private static final LinkOption[] ORIGINAL = {LinkOption.NOFOLLOW_LINKS};
    private static final LinkOption[] RESOLVE = {};

    static Stream<Case> validCases() {
        return Stream.of(new Case(MockFileAttributes.REGULAR_FILE, FileType.REGULAR_FILE),
                         new Case(MockFileAttributes.DIRECTORY, FileType.DIRECTORY),
                         new Case(MockFileAttributes.OTHER, FileType.SPECIAL_FILE),
                         new Case(MockFileAttributes.BASIC_LINK, FileType.SYMBOLIC_LINK),
                         new Case(Util.MISSING_FILE_ATTRIBUTES, FileType.MISSING),
                         new Case(new LinkAttributes(ORIGINAL, // strange case ...
                                                     MockFileAttributes.REGULAR_FILE), FileType.REGULAR_FILE),
                         new Case(new LinkAttributes(RESOLVE,
                                                     MockFileAttributes.REGULAR_FILE), FileType.REGULAR_FILE),
                         new Case(new LinkAttributes(ORIGINAL, // strange case ...
                                                     MockFileAttributes.DIRECTORY), FileType.DIRECTORY),
                         new Case(new LinkAttributes(RESOLVE,
                                                     MockFileAttributes.DIRECTORY), FileType.DIRECTORY),
                         new Case(new LinkAttributes(ORIGINAL, // strange case ...
                                                     MockFileAttributes.OTHER), FileType.SPECIAL_FILE),
                         new Case(new LinkAttributes(RESOLVE,
                                                     MockFileAttributes.OTHER), FileType.SPECIAL_FILE),
                         new Case(new LinkAttributes(ORIGINAL,
                                                     MockFileAttributes.BASIC_LINK), FileType.SYMBOLIC_LINK),
                         new Case(new LinkAttributes(RESOLVE, // strange case ...
                                                     MockFileAttributes.BASIC_LINK), FileType.SYMBOLIC_LINK),
                         new Case(new LinkAttributes(ORIGINAL, // strange case ...
                                                     Util.MISSING_FILE_ATTRIBUTES), FileType.MISSING),
                         new Case(new LinkAttributes(RESOLVE,
                                                     Util.MISSING_FILE_ATTRIBUTES), FileType.MISSING));
    }

    static Stream<BasicFileAttributes> invalidCases() {
        return Stream.of(MockFileAttributes.UNKNOWN,
                         new LinkAttributes(ORIGINAL, MockFileAttributes.UNKNOWN),
                         new LinkAttributes(RESOLVE, MockFileAttributes.UNKNOWN));
    }

    @ParameterizedTest
    @MethodSource("validCases")
    final void of(final Case given) {
        final var type = FileType.of(given.attributes);
        assertEquals(given.expected, type);
    }

    @ParameterizedTest
    @MethodSource("invalidCases")
    final void of_unknown(final BasicFileAttributes given) {
        try {
            final var type = FileType.of(given);
            fail("expected to fail - but was " + type);
        } catch (final NoSuchElementException e) {
            // OK, as expected
            // e.printStackTrace();
        }
    }

    interface MockFileAttributes extends BasicFileAttributes {

        MockFileAttributes REGULAR_FILE = new MockFileAttributes() {
            @Override
            public boolean isRegularFile() {
                return true;
            }

            @Override
            public String toString() {
                return "Mock.REGULAR_FILE";
            }
        };

        MockFileAttributes DIRECTORY = new MockFileAttributes() {
            @Override
            public boolean isDirectory() {
                return true;
            }

            @Override
            public String toString() {
                return "Mock.DIRECTORY";
            }
        };

        MockFileAttributes OTHER = new MockFileAttributes() {
            @Override
            public boolean isOther() {
                return true;
            }

            @Override
            public String toString() {
                return "Mock.OTHER";
            }
        };

        MockFileAttributes BASIC_LINK = new MockFileAttributes() {
            @Override
            public boolean isSymbolicLink() {
                return true;
            }

            @Override
            public String toString() {
                return "Mock.BASIC_LINK";
            }
        };

        MockFileAttributes UNKNOWN = new MockFileAttributes() {
            @Override
            public String toString() {
                return "Mock.UNKNOWN";
            }
        };

        @Override
        default FileTime lastModifiedTime() {
            return null;
        }

        @Override
        default FileTime lastAccessTime() {
            return null;
        }

        @Override
        default FileTime creationTime() {
            return null;
        }

        @Override
        default boolean isRegularFile() {
            return false;
        }

        @Override
        default boolean isDirectory() {
            return false;
        }

        @Override
        default boolean isSymbolicLink() {
            return false;
        }

        @Override
        default boolean isOther() {
            return false;
        }

        @Override
        default long size() {
            return 0;
        }

        @Override
        default Object fileKey() {
            return null;
        }
    }

    record Case(BasicFileAttributes attributes, FileType expected) {
    }
}