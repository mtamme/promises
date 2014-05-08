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
 * 
 * @param <T>
 * @param <U>
 */
public abstract class Finally<T, U> implements Continuation<T, U> {

    protected abstract U doFinally(T value, Throwable cause) throws Exception;

    @Override
    public final void onSuccess(final T value, final Deferred<U> deferred) throws Exception {
        final U result = doFinally(value, null);

        deferred.setSuccess(result);
    }

    @Override
    public final void onFailure(final Throwable cause, final Deferred<U> deferred) throws Exception {
        final U result = doFinally(null, cause);

        deferred.setSuccess(result);
    }
}
