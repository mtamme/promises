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
public abstract class OnComplete<T> implements Continuation<T, T> {

    protected abstract void onSuccess(T value);

    protected abstract void onFailure(Throwable cause);

    @Override
    public final void onSuccess(final T value, final Completable<T> completable) {
        try {
            onSuccess(value);
            completable.setSuccess(value);
        } catch (final Throwable t) {
            completable.setFailure(t);
        }
    }

    @Override
    public final void onFailure(final Throwable cause, final Completable<T> completable) {
        try {
            onFailure(cause);
            completable.setFailure(cause);
        } catch (final Throwable t) {
            t.addSuppressed(cause);
            completable.setFailure(t);
        }
    }
}
