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
 * Represents an abstract {@link Promise}.
 * 
 * @param <V> The value type.
 */
public abstract class AbstractPromise<V> implements Promise<V> {

    @Override
    public final Promise<V> thenCall(final Callback<? super V> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null");
        }

        final CompletablePromise<V> promise = new CompletablePromise<V>();

        addCallback(new Callback<V>() {
            @Override
            public void onResolved(final V value) {
                try {
                    callback.onResolved(value);
                    promise.resolve(value);
                } catch (final Throwable t) {
                    promise.reject(t);
                }
            }

            @Override
            public void onRejected(final Throwable throwable) {
                try {
                    callback.onRejected(throwable);
                    promise.reject(throwable);
                } catch (final Throwable t) {
                    t.addSuppressed(throwable);
                    promise.reject(t);
                }
            }
        });

        return promise;
    }

    @Override
    public final <R> Promise<R> thenApply(final Function<? super V, ? extends R> function) {
        if (function == null) {
            throw new IllegalArgumentException("Function must not be null");
        }

        final CompletablePromise<R> promise = new CompletablePromise<R>();

        addCallback(new Callback<V>() {
            @Override
            public void onResolved(final V value) {
                try {
                    final R result = function.apply(value);

                    promise.resolve(result);
                } catch (final Throwable t) {
                    promise.reject(t);
                }
            }

            @Override
            public void onRejected(final Throwable throwable) {
                promise.reject(throwable);
            }
        });

        return promise;
    }

    @Override
    public final <R> Promise<R> thenCompose(final Function<? super V, ? extends Promise<R>> function) {
        if (function == null) {
            throw new IllegalArgumentException("Function must not be null");
        }

        final CompletablePromise<R> promise = new CompletablePromise<R>();
        final Callback<R> callback = new Callback<R>() {
            @Override
            public void onResolved(final R result) {
                promise.resolve(result);
            }

            @Override
            public void onRejected(final Throwable throwable) {
                promise.reject(throwable);
            }
        };

        addCallback(new Callback<V>() {
            @Override
            public void onResolved(final V value) {
                try {
                    final Promise<R> promise = function.apply(value);

                    promise.addCallback(callback);
                } catch (final Throwable t) {
                    promise.reject(t);
                }
            }

            @Override
            public void onRejected(final Throwable throwable) {
                promise.reject(throwable);
            }
        });

        return promise;
    }
}
