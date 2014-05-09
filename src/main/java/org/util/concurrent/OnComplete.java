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
 * Represents a complete callback.
 * 
 * @param <T> The value type.
 */
public abstract class OnComplete<T> implements ThenCallback<T, T> {

    /**
     * Handles the success callback.
     * 
     * @param value The value.
     * @throws Exception The exception.
     */
    protected abstract void onSuccess(T value) throws Exception;

    /**
     * Handles the failure callback.
     * 
     * @param cause The cause.
     * @throws Exception The exception.
     */
    protected abstract void onFailure(Throwable cause) throws Exception;

    @Override
    public final void onSuccess(final T value, final Deferred<T> deferred) throws Exception {
        onSuccess(value);
        deferred.setSuccess(value);
    }

    @Override
    public final void onFailure(final Throwable cause, final Deferred<T> deferred) throws Exception {
        onFailure(cause);
        deferred.setFailure(cause);
    }
}
