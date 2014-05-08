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
public abstract class Catch<T> implements Continuation<T, T> {

    protected abstract T doCatch(Throwable cause) throws Exception;

    @Override
    public final void onSuccess(final T value, final Completable<T> completable) {
        completable.setSuccess(value);
    }

    @Override
    public final void onFailure(final Throwable cause, final Completable<T> completable) {
        try {
            final T value = doCatch(cause);

            completable.setSuccess(value);
        } catch (final Throwable t) {
            t.addSuppressed(cause);
            completable.setFailure(t);
        }
    }
}