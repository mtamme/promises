/*
 * Copyright © Martin Tamme
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.util.concurrent;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an executor completion.
 * 
 * @param <T> The value type.
 */
abstract class ExecutorCompletion<T> implements Completion<T> {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ExecutorCompletion.class);

    /**
     * The executor.
     */
    private final Executor _executor;

    /**
     * Initializes a new instance of the {@link ExecutorCompletion} class.
     * 
     * @param executor The executor.
     */
    protected ExecutorCompletion(final Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("Executor must not e null");
        }

        _executor = executor;
    }

    /**
     * Handles the success completion.
     * 
     * @param value The value.
     */
    protected abstract void doSuccess(T value);

    /**
     * Handles the failure completion.
     * 
     * @param cause The cause.
     */
    protected abstract void doFailure(Throwable cause);

    @Override
    public final void setSuccess(final T value) {
        _executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    doSuccess(value);
                } catch (final Throwable t) {
                    logger.warn("Failed to execute completion", t);
                }

            }
        });
    }

    @Override
    public final void setFailure(final Throwable cause) {
        _executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    doFailure(cause);
                } catch (final Throwable t) {
                    logger.warn("Failed to execute completion", t);
                }
            }
        });
    }
}
