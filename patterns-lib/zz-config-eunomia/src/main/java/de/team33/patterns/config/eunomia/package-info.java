/**
 * Provides persistent, hierarchical configuration based on Java records.
 * <p>
 * A configuration is represented by a record type and may be supplied at different
 * {@link de.team33.patterns.config.eunomia.ConfigLevel configuration levels}.
 * The available levels are combined according to their precedence to obtain the effective configuration.
 * <p>
 * The {@link de.team33.patterns.config.eunomia.ConfigRepo ConfigRepo} class provides the primary API
 * for creating, reading, writing, and resetting configurations.
 * Each repository is associated with exactly one record type and is initialized with a default configuration.
 * <p>
 * Configuration values from higher-precedence levels override values from lower-precedence levels
 * only when they are explicitly present.
 * Missing values and {@code null} values therefore leave existing values unchanged.
 * This allows configuration records to represent partially specified configurations,
 * which is particularly useful for hierarchical configuration.
 *
 * @see <a href="https://de.wikipedia.org/wiki/(15)_Eunomia" target="_blank">Eunomia (Asteroid)</a>
 * @see <a href="https://de.wikipedia.org/wiki/Eunomia_(Mythologie)" target="_blank">Eunomia (Mythologie)</a>
 */
package de.team33.patterns.config.eunomia;
