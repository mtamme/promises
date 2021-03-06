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

package org.util.concurrent.promise;

/**
 * Represents an apply continuation.
 * 
 * @param <T> The value type.
 * @param <R> The result type.
 */
public abstract class Apply<T, R> implements Continuation<T, R> {

    /**
     * Handles the apply continuation.
     * 
     * @param value The value.
     * @return The result.
     * @throws Exception
     */
    protected abstract R doApply(T value) throws Exception;

    /**
     * Handles the failure continuation.
     * 
     * @param cause The cause.
     * @throws Exception
     */
    protected void onFailure(final Throwable cause) throws Exception {
    }

    @Override
    public final void onSuccess(final T value, final Completable<? super R> result) throws Exception {
        final R newValue = doApply(value);

        result.setSuccess(newValue);
    }

    @Override
    public final void onFailure(final Throwable cause, final Completable<? super R> result) throws Exception {
        onFailure(cause);
        result.setFailure(cause);
    }
}
