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
 */
public abstract class Run<T, R> extends Deferred<R> implements Continuation<T, R> {

    protected abstract R doRun(T value) throws Exception;

    @Override
    public final void onSuccess(final T value) {
        try {
            final R result = doRun(value);

            setSuccess(result);
        } catch (final Throwable t) {
            setFailure(t);
        }
    }

    @Override
    public final void onFailure(final Throwable cause) {
        setFailure(cause);
    }
}
