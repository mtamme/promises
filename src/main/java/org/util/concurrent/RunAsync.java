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

/**
 * Represents an asynchronous run callback.
 * 
 * @param <T>
 * @param <U>
 */
public abstract class RunAsync<T, U> implements ThenCallback<T, U> {

    /**
     * Handles the asynchronous run callback.
     * 
     * @param value The value.
     * @return The promised result.
     * @throws Exception The exception.
     */
    protected abstract Promise<U> doRun(T value) throws Exception;

    @Override
    public final void onSuccess(final T value, final Deferred<U> result) throws Exception {
        final Promise<U> newValue = doRun(value);

        newValue.then(new CompleteCallback<U>() {
            @Override
            public void onSuccess(final U value) {
                result.setSuccess(value);
            }

            @Override
            public void onFailure(final Throwable cause) {
                result.setFailure(cause);
            }
        });
    }

    @Override
    public final void onFailure(final Throwable cause, final Deferred<U> result) throws Exception {
        result.setFailure(cause);
    }
}
