/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.executable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Remember to close the Streams when they are done being used.
 */
public class ExecutableRedirectThread extends Thread {
    private final InputStream in;
    private final OutputStream out;

    public ExecutableRedirectThread(final InputStream in, final OutputStream out) {
        super(Thread.currentThread().getName() + "-Executable Redirect Thread");
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            int i;
            while ((i = in.read()) >= 0) {
                if (i == -1) {
                    break;
                }
                out.write(i);
            }
        } catch (final IOException e) {
            // Ignore
        }
    }

}
