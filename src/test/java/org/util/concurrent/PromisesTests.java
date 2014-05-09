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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class PromisesTests {

    @Test
    public void succeededTest() {
        // Arrange
        // Act
        final Promise<Integer> promise = Promises.succeeded(1);

        // Assert
        assertNotNull(promise);
        assertTrue(promise.isComplete());
    }

    @Test
    public void failedTest() {
        // Arrange
        // Act
        final Promise<Integer> promise = Promises.failed(new Throwable());

        // Assert
        assertNotNull(promise);
        assertTrue(promise.isComplete());
    }

    @Test
    public void thenWithSucceededTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final CompleteCallback<Integer> callback = createStrictMock(CompleteCallback.class);

        callback.onSuccess(1);
        replay(callback);
        final Promise<Integer> promise = Promises.succeeded(1);

        // Act
        promise.then(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void thenWithFailedTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final CompleteCallback<Integer> callback = createStrictMock(CompleteCallback.class);
        final Throwable cause = new Throwable();

        callback.onFailure(cause);
        replay(callback);
        final Promise<Integer> promise = Promises.failed(cause);

        // Act
        promise.then(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void getWithSucceededAndToFutureTest() throws InterruptedException, ExecutionException {
        // Arrange
        final Promise<Integer> promise = Promises.succeeded(1);
        final Future<Integer> future = Promises.toFuture(promise);

        // Act
        // Assert
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
        assertThat(future.get(), is(1));
    }

    @Test(expected = ExecutionException.class)
    public void getWithFailedAndToFutureTest() throws InterruptedException, ExecutionException {
        // Arrange
        final Promise<Integer> promise = Promises.failed(new Throwable());
        final Future<Integer> future = Promises.toFuture(promise);

        // Act
        // Assert
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
        future.get();
    }

    @Test
    public void getWithSucceededAndToFutureAndTimeoutTest() throws InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        final Promise<Integer> promise = Promises.succeeded(1);
        final Future<Integer> future = Promises.toFuture(promise);

        // Act
        // Assert
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
        assertThat(future.get(0, TimeUnit.MICROSECONDS), is(1));
    }

    @Test(expected = ExecutionException.class)
    public void getWithFailedAndToFutureAndTimeoutTest() throws InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        final Promise<Integer> promise = Promises.failed(new Throwable());
        final Future<Integer> future = Promises.toFuture(promise);

        // Act
        // Assert
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
        future.get(0, TimeUnit.MICROSECONDS);
    }
}
