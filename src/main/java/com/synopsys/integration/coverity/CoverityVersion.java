/**
 * coverity-common
 *
 * Copyright (c) 2019 Synopsys, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.coverity;

import java.io.Serializable;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

/**
 * An abstract representation of a Coverity version number. Conventional version numbers, as well as current and past
 * codenames are all valid and comparable.
 */
public class CoverityVersion implements Comparable<CoverityVersion>, Serializable {
    public static final CoverityVersion VERSION_JASPER = new CoverityVersion(8, 0, 0, 0);
    public static final CoverityVersion VERSION_JASPER1 = new CoverityVersion(8, 1, 0, 0);
    public static final CoverityVersion VERSION_PACIFIC = new CoverityVersion(2018, 12, "2018.12");

    final int major;
    final int minor;
    final int patch;
    final int hotfix;
    private String srmVersion;

    public CoverityVersion(final int major, final int minor, final int patch, final int hotfix, final String srmVersion) {
        this.hotfix = hotfix;
        this.minor = minor;
        this.patch = patch;
        this.major = major;
        this.srmVersion = srmVersion;
    }

    public CoverityVersion(final int major, final int minor, final String srmVersion) {
        this.hotfix = 0;
        this.minor = minor;
        this.patch = 0;
        this.major = major;
        this.srmVersion = srmVersion;
    }

    public CoverityVersion(final int major, final int minor, final int patch, final int hotfix) {
        this(major, minor, patch, hotfix, null);
    }

    public CoverityVersion(final int major, final int minor) {
        this(major, minor, 0, 0, null);
    }

    public static Optional<CoverityVersion> parse(String s) {
        try {
            if (StringUtils.isEmpty(s)) {
                return Optional.empty();
            }

            final String[] parts = s.split("\\.");

            int major;
            int minor;
            int patch = 0;
            int hotfix = 0;
            String srmVersion = null;

            if (parts.length == 2) {
                //srm number
                major = Integer.parseInt(parts[0]);
                srmVersion = s;
                final String[] srmParts = parts[1].split("-SP|-");

                if (srmParts.length == 2) {
                    minor = Integer.parseInt(srmParts[0]);
                    patch = Integer.parseInt(srmParts[1]);
                } else if (srmParts.length == 3) {
                    minor = Integer.parseInt(srmParts[0]);
                    patch = Integer.parseInt(srmParts[1]);
                    hotfix = Integer.parseInt(srmParts[2]);
                } else {
                    minor = Integer.parseInt(parts[1]);
                }
            } else if (parts.length == 3) {
                //number
                major = Integer.parseInt(parts[0]);
                minor = Integer.parseInt(parts[1]);
                patch = Integer.parseInt(parts[2]);
            } else if (parts.length == 4) {
                //number w/hotfix
                major = Integer.parseInt(parts[0]);
                minor = Integer.parseInt(parts[1]);
                patch = Integer.parseInt(parts[2]);
                hotfix = Integer.parseInt(parts[3]);
            } else {
                return Optional.empty();
            }

            return Optional.of(new CoverityVersion(major, minor, patch, hotfix, srmVersion));
        } catch (Exception ignored) {
            // Do nothing
        }

        return Optional.empty();
    }

    public boolean isSrmVersion() {
        return StringUtils.isNotBlank(srmVersion);
    }

    @Override
    public String toString() {
        if (this.isSrmVersion()) {
            return srmVersion;
        }

        return major + "." + minor + "." + patch + (hotfix > 0 ? ("." + hotfix) : "");
    }

    public int compareTo(final CoverityVersion o) {
        if (major != o.major) {
            return Integer.compare(major, o.major);
        }

        if (minor != o.minor) {
            return Integer.compare(minor, o.minor);
        }

        if (patch != o.patch) {
            return Integer.compare(patch, o.patch);
        }

        return Integer.compare(hotfix, o.hotfix);
    }

    /**
     * The way that Compare Major Minor works is that the argument passed in is the analysis version.
     * Compares the version's major and minor version number.
     * @param version
     * @return true if the current version is greater than or equals to the passed in version; Otherwise false
     */
    public boolean compareToAnalysis(final CoverityVersion version) {
        if (major == version.major) {
            return minor >= version.minor;
        } else {
            return major > version.major;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CoverityVersion other = (CoverityVersion) o;

        return this.compareTo(other) == 0;
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = 31 * result + major;
        result = 31 * result + minor;
        result = 31 * result + patch;
        result = 31 * result + hotfix;
        return result;
    }
}
