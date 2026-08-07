/**
 * Provides a compact, application-oriented view of file system entries.
 * <p>
 * The package centers around {@link de.team33.patterns.files.pluto.FileEntry},
 * which combines a normalized {@link java.nio.file.Path} with commonly used
 * file attributes and a simplified classification represented by
 * {@link de.team33.patterns.files.pluto.FileType}.
 * <p>
 * Instances may expose either the original attributes of symbolic links or the
 * attributes of their resolved targets.
 *
 * @see <a href="https://de.wikipedia.org/wiki/Pluto" target="_blank">Pluto (Zwergplanet)</a>
 * @see <a href="https://de.wikipedia.org/wiki/Pluton" target="_blank">Pluto (Mythologie)</a>
 */
package de.team33.patterns.files.pluto;