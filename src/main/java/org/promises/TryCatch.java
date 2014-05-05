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

package org.promises;

/**
 * 
 */
public abstract class TryCatch<T, R> extends Deferred<R> implements Continuation<T, R> {

    protected abstract R doTry(T value) throws Exception;

    protected abstract R doCatch(T value, Throwable cause) throws Exception;

    @Override
    public final void onSuccess(final T value) {
        try {
            R result;

            try {
                result = doTry(value);
            } catch (final Throwable t) {
                result = doCatch(value, t);
            }
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
