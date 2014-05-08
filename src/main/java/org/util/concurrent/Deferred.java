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

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents a deferred.
 * 
 * @param <T> The value type.
 */
public final class Deferred<T> implements Promise<T>, Completable<T> {

    /**
     * The current state of the {@link Deferred}.
     */
    private final AtomicReference<State<T>> _state;

    /**
     * Initializes a new instance of the {@link Deferred} class.
     */
    public Deferred() {
        final State<T> initialState = new PendingState();

        _state = new AtomicReference<State<T>>(initialState);
    }

    /**
     * Initializes a new instance of the {@link Deferred} class.
     * 
     * @param value The value.
     */
    public Deferred(final T value) {
        final State<T> initialState = new SuccessState<T>(value);

        _state = new AtomicReference<State<T>>(initialState);
    }

    /**
     * Initializes a new instance of the {@link Deferred} class.
     * 
     * @param cause The cause.
     */
    public Deferred(final Throwable cause) {
        if (cause == null) {
            throw new IllegalArgumentException("Cause must not be null");
        }

        final State<T> initialState = new FailureState<T>(cause);

        _state = new AtomicReference<State<T>>(initialState);
    }

    /**
     * Tries to resolves the {@link Deferred} with the specified value.
     * 
     * @param value The value.
     * @return A value indicating whether the {@link Deferred} was resolved.
     */
    public final boolean setSuccess(final T value) {
        return _state.get().setSuccess(value);
    }

    /**
     * Tries to rejects the {@link Deferred} with the specified throwable.
     * 
     * @param cause The cause.
     * @return A value indicating whether the {@link Deferred} was rejected.
     */
    public final boolean setFailure(final Throwable cause) {
        if (cause == null) {
            throw new IllegalArgumentException("Cause must not be null");
        }

        return _state.get().setFailure(cause);
    }

    @Override
    public final boolean isComplete() {
        return _state.get().isComplete();
    }

    @Override
    public void onComplete(final CompleteListener<T> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }

        _state.get().onComplete(listener);
    }

    @Override
    public final <R> Promise<R> then(final Continuation<T, R> continuation) {
        if (continuation == null) {
            throw new IllegalArgumentException("Continuation must not be null");
        }

        final Deferred<R> deferred = new Deferred<R>();

        onComplete(new CompleteListener<T>() {
            @Override
            public void onSuccess(final T value) {
                try {
                    continuation.onSuccess(value, deferred);
                } catch (final Throwable t) {
                    deferred.setFailure(t);
                }
            }

            @Override
            public void onFailure(final Throwable cause) {
                try {
                    continuation.onFailure(cause, deferred);
                } catch (final Throwable t) {
                    t.addSuppressed(cause);
                    deferred.setFailure(t);
                }
            }
        });

        return deferred;
    }

    /**
     * Defines a state of a {@link Deferred}.
     * 
     * @param <T> The value type.
     */
    private static interface State<T> {

        boolean isComplete();

        /**
         * Tries to resolves the {@link Deferred} with the specified value.
         * 
         * @param value The value.
         * @return A value indicating whether the {@link Deferred} was resolved.
         */
        boolean setSuccess(T value);

        /**
         * Tries to rejects the {@link Deferred} with the specified throwable.
         * 
         * @param throwable The throwable.
         * @return A value indicating whether the {@link Deferred} was rejected.
         */
        boolean setFailure(Throwable throwable);

        /**
         * Adds the specified complete callback.
         * 
         * @param listener The complete callback.
         */
        void onComplete(CompleteListener<T> listener);
    }

    /**
     * Represents the pending state of a {@link Deferred}.
     * 
     * @param <V> The value type.
     */
    private final class PendingState implements State<T> {

        /**
         * The stage queue.
         */
        private final ConcurrentLinkedQueue<Stage<T>> _stages;

        /**
         * Initializes a new instance of the {@link PendingState} class.
         */
        public PendingState() {
            _stages = new ConcurrentLinkedQueue<Stage<T>>();
        }

        /**
         * Changes the state to the specified state.
         * 
         * @param state The state.
         * @return A value indicating whether the state has been changed.
         */
        private boolean changeState(final State<T> state) {
            if (!_state.compareAndSet(this, state)) {
                return false;
            }
            complete(state);

            return true;
        }

        /**
         * Adds the specified stage.
         * 
         * @param stage The stage.
         */
        private void addStage(final Stage<T> stage) {
            // As the queue is unbounded, this method will never return false.
            _stages.offer(stage);
            // Trigger completion when the promise has been completed in the meantime.
            final State<T> state = _state.get();

            if (state != this) {
                complete(state);
            }
        }

        /**
         * Triggers completion with the specified state.
         * 
         * @param state The state.
         */
        private void complete(final State<T> state) {
            Stage<T> stage;

            while ((stage = _stages.poll()) != null) {
                try {
                    stage.complete(state);
                } catch (final Throwable t) {
                    // FIXME Handle error.
                }
            }
        }

        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public boolean setSuccess(final T value) {
            final State<T> state = new SuccessState<T>(value);

            return changeState(state);
        }

        @Override
        public boolean setFailure(final Throwable cause) {
            final State<T> state = new FailureState<T>(cause);

            return changeState(state);
        }

        @Override
        public void onComplete(final CompleteListener<T> listener) {
            final Stage<T> stage = new Stage<T>(listener);

            addStage(stage);
        }
    }

    /**
     * Represents the completed state of a {@link Deferred}.
     * 
     * @param <T> The value type.
     */
    private static abstract class CompleteState<T> implements State<T> {

        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public final boolean setSuccess(final T value) {
            return false;
        }

        @Override
        public final boolean setFailure(final Throwable cause) {
            return false;
        }
    }

    /**
     * Represents the resolved state of a {@link Deferred}.
     * 
     * @param <T> The value type.
     */
    private static final class SuccessState<T> extends CompleteState<T> {

        /**
         * The value.
         */
        private final T _value;

        /**
         * Initializes a new instance of the {@link SuccessState} class.
         * 
         * @param value The value.
         */
        public SuccessState(final T value) {
            _value = value;
        }

        @Override
        public void onComplete(final CompleteListener<T> listener) {
            listener.onSuccess(_value);
        }
    }

    /**
     * Represents the rejected state of a {@link Deferred}.
     * 
     * @param <T> The value type.
     */
    private static final class FailureState<T> extends CompleteState<T> {

        /**
         * The throwable.
         */
        private final Throwable _cause;

        /**
         * Initializes a new instance of the {@link FailureState} class.
         * 
         * @param cause The cause.
         */
        public FailureState(final Throwable cause) {
            _cause = cause;
        }

        @Override
        public void onComplete(final CompleteListener<T> listener) {
            listener.onFailure(_cause);
        }
    }

    /**
     * Represents a complete stage.
     * 
     * @param <T> The value type.
     */
    private static final class Stage<T> {

        /**
         * The complete callback.
         */
        private final CompleteListener<T> _listener;

        /**
         * The completed flag.
         */
        private final AtomicBoolean _completed;

        /**
         * Initializes a new instance of the {@link CompleteStage} class.
         * 
         * @param listener The complete callback.
         */
        public Stage(final CompleteListener<T> listener) {
            _listener = listener;

            _completed = new AtomicBoolean(false);
        }

        /**
         * Triggers completion with the specified state.
         * 
         * @param state The state.
         */
        public void complete(final State<T> state) {
            if (_completed.compareAndSet(false, true)) {
                state.onComplete(_listener);
            }
        }
    }
}