/**
 * coverity-common
 *
 * Copyright (C) 2019 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
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
package com.synopsys.integration.coverity.executable;

// ...
// 1
// ...
public enum CoverityEnvironmentVariable implements SynopsysEnvironmentVariable {
    USER("COV_USER"),
    PASSPHRASE("COVERITY_PASSPHRASE"),
    PASSPHRASE_FILE("COVERITY_PASSPHRASE_FILE");

    // ...
    // 2
    // ...

    // Paraphrased from the Coverity devs:
    // "All Coverity tools written as C++ programs share the same backend args parser, so the flags should be accepted by all of them but may-or-may-not be used."
    //
    // Therefore, this enum should only be populated with things we absolutely want the tools to pick up from environment variables, like passwords.
    // -rotte (1/18/2019)

    private final String name;

    CoverityEnvironmentVariable(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
