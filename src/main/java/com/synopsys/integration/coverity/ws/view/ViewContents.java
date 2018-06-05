/**
 * coverity-common
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
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
package com.synopsys.integration.coverity.ws.view;

import java.util.List;
import java.util.Map;

public class ViewContents {
    private final Long totalRows;
    private final Long rowsOffset;
    private final List<String> columns;
    private final List<Map<String, Object>> rows;

    public ViewContents(Long totalRows, Long rowsOffset, List<String> columns, List<Map<String, Object>> rows) {
        this.totalRows = totalRows;
        this.rowsOffset = rowsOffset;
        this.columns = columns;
        this.rows = rows;
    }

    public Long getTotalRows() {
        return totalRows;
    }

    public Long getRowsOffset() {
        return rowsOffset;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }
}
