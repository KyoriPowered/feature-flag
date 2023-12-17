/*
 * This file is part of feature-flag, licensed under the MIT License.
 *
 * Copyright (c) 2023 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.featureflag;

import java.util.Map;
import java.util.function.Consumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Collection of feature flags.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface FeatureFlagConfig {
  /**
   * Get an empty set of feature flags.
   *
   * @return the empty feature flag set
   * @since 1.0.0
   */
  static FeatureFlagConfig empty() {
    return FeatureFlagConfigImpl.EMPTY;
  }

  /**
   * Create a builder for an unversioned feature flag set.
   *
   * @return the builder
   * @since 1.0.0
   */
  static @NotNull Builder builder() {
    return new FeatureFlagConfigImpl.BuilderImpl();
  }

  /**
   * Create a builder for a versioned feature flag set.
   *
   * @return the builder
   * @since 1.0.0
   */
  static @NotNull VersionedBuilder versionedBuilder() {
    return new FeatureFlagConfigImpl.VersionedBuilderImpl();
  }


  /**
   * Get whether a flag set contains a certain flag at all.
   *
   * @param flag the flag to check.
   * @return whether the flag has been touched.
   * @since 1.0.0
   */
  boolean has(final @NotNull FeatureFlag<?> flag);

  /**
   * Get the value set for a certain flag.
   *
   * @param flag the flag to query
   * @return the flag value
   * @param <V> the value type
   * @since 1.0.0
   */
  <V> V value(final @NotNull FeatureFlag<V> flag);

  /**
   * A composite feature flag set.
   *
   * <p>By default, this returns results for the newest supported version.</p>
   *
   * @since 1.0.0
   */
  @ApiStatus.NonExtendable
  interface Versioned extends FeatureFlagConfig {
    /**
     * The individual changes in each supported version.
     *
     * @return the child sets that exist
     * @since 1.0.0
     */
    @NotNull Map<Integer, FeatureFlagConfig> childSets();

    /**
     * Request a view of this feature flag set showing only flags available at versions up to and including {@code version}.
     *
     * @param version the version to query
     * @return a limited view of this set
     * @since 1.0.0
     */
    @NotNull Versioned at(final int version);
  }

  /**
   * A builder for feature flag sets.
   *
   * @since 4.15.0
   */
  @ApiStatus.NonExtendable
  interface Builder {
    /**
     * Set the value for a specific flag.
     *
     * @param flag the flag to set the value for
     * @param value the value
     * @return this builder
     * @param <V> the value type
     * @since 1.0.0
     */
    <V> @NotNull Builder value(final @NotNull FeatureFlag<V> flag, final @NotNull V value);

    /**
     * Apply all values from the existing feature flag set.
     *
     * @param existing the existing set
     * @return this builder
     * @since 1.0.0
     */
    @NotNull Builder values(final @NotNull FeatureFlagConfig existing);

    /**
     * Create a completed feature flag config.
     *
     * @return the built config
     * @since 1.0.0
     */
    @NotNull FeatureFlagConfig build();
  }

  /**
   * A builder for versioned feature flag sets.
   *
   * @since 1.0.0
   */
  @ApiStatus.NonExtendable
  interface VersionedBuilder {
    /**
     * Register feature flags for a specific version.
     *
     * @param version the version to register
     * @param versionBuilder the builder that will receive flags
     * @return this builder
     * @since 1.0.0
     */
    @NotNull VersionedBuilder version(final int version, final @NotNull Consumer<Builder> versionBuilder);

    /**
     * Create a completed versioned flag config.
     *
     * @return the built versioned config
     * @since 1.0.0
     */
    @NotNull Versioned build();
  }
}
