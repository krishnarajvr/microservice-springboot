/*
 * Copyright (c) 2020 Network New Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.company.micro.common.validator.schema;

import java.util.Set;

public class ValidationResult {

    private Set<ValidationMessage> validationMessages;

    private CollectorContext collectorContext;

    public ValidationResult(Set<ValidationMessage> validationMessages, CollectorContext collectorContext) {
        super();
        this.validationMessages = validationMessages;
        this.collectorContext = collectorContext;
    }

    public Set<ValidationMessage> getValidationMessages() {
        return validationMessages;
    }

    public CollectorContext getCollectorContext() {
        return collectorContext;
    }

}
