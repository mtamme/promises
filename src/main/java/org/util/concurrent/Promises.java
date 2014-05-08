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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Provides {@link Promise} extension methods.
 */
public final class Promises {

    /**
     * Initializes a new instance of the {@link Promises} class.
     */
    private Promises() {
    }

    /**
     * Returns a resolved {@link Promise}.
     * 
     * @return A resolved {@link Promise}.
     */
    public static Promise<Void> success() {
        return success(null);
    }

    /**
     * Returns a resolved {@link Promise}.
     * 
     * @param value The value.
     * @return A resolved {@link Promise}.
     */
    public static <T> Promise<T> success(final T value) {
        return new DefaultDeferred<T>(value);
    }

    /**
     * Returns a rejected {@link Promise}.
     * 
     * @param cause The cause.
     * @return A rejected {@link Promise}.
     */
    public static <T> Promise<T> failure(final Throwable cause) {
        return new DefaultDeferred<T>(cause);
    }

    /**
     * 
     * @return
     */
    public static <T> Deferred<T> deferred() {
        return new DefaultDeferred<T>();
    }

    /**
     * Returns a {@link Future} for the specified {@link Promise}.
     * 
     * @param promise The promise.
     * @return The future.
     */
    public static <T> Future<T> toFuture(final Promise<T> promise) {
        if (promise == null) {
            throw new IllegalArgumentException("Promise must not be null");
        }

        final Awaiter<T> awaiter = new Awaiter<T>();

        promise.onComplete(awaiter);

        return new Future<T>() {
            @Override
            public boolean cancel(final boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return awaiter.isCompleted();
            }

            @Override
            public T get() throws InterruptedException, ExecutionException {
                return awaiter.get();
            }

            @Override
            public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return awaiter.get(timeout, unit);
            }
        };
    }
}
