/*
 * Copyright © Martin Tamme
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
final class ExecutorCompletion<T> implements Completion<T> {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ExecutorCompletion.class);

    /**
     * The executor.
     */
    private final Executor _executor;

    /**
     * The completion.
     */
    private final Completion<T> _completion;

    /**
     * Initializes a new instance of the {@link ExecutorCompletion} class.
     * 
     * @param executor The executor.
     * @param completion The completion.
     */
    public ExecutorCompletion(Executor executor, Completion<T> completion) {
        if (executor == null) {
            throw new IllegalArgumentException("Executor must not e null");
        }
        if (completion == null) {
            throw new IllegalArgumentException("Completion must not be null");
        }

        _executor = executor;
        _completion = completion;
    }

    @Override
    public void setSuccess(final T value) {
        _executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    _completion.setSuccess(value);
                } catch (final Throwable t) {
                    logger.warn("Failed to complete", t);
                }

            }
        });
    }

    @Override
    public void setFailure(final Throwable cause) {
        _executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    _completion.setFailure(cause);
                } catch (final Throwable t) {
                    logger.warn("Failed to complete", t);
                }
            }
        });
    }
}