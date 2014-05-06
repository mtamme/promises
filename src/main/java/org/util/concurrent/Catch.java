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
public abstract class Catch<T> extends Deferred<T> implements Continuation<T, T> {

    protected abstract T doCatch(Throwable cause) throws Exception;

    @Override
    public final void onSuccess(final T value) {
        setSuccess(value);
    }

    @Override
    public final void onFailure(final Throwable cause) {
        try {
            final T value = doCatch(cause);

            setSuccess(value);
        } catch (final Throwable t) {
            t.addSuppressed(cause);
            setFailure(t);
        }
    }
}
