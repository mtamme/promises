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
 * Represents a run callback.
 * 
 * @param <T> The value type.
 * @param <U> The result type.
 */
public abstract class Run<T, U> implements ThenCallback<T, U> {

    /**
     * Handles the run callback.
     * 
     * @param value The value.
     * @return The result.
     * @throws Exception The exception.
     */
    protected abstract U doRun(T value) throws Exception;

    @Override
    public final void onSuccess(final T value, final Deferred<U> deferred) throws Exception {
        final U result = doRun(value);

        deferred.setSuccess(result);
    }

    @Override
    public final void onFailure(final Throwable cause, final Deferred<U> deferred) throws Exception {
        deferred.setFailure(cause);
    }
}
