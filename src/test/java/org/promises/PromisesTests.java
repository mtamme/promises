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
    public void resolvedTest() {
        // Arrange
        // Act
        final Promise<Integer> promise = Promises.success(1);

        // Assert
        assertNotNull(promise);
        assertTrue(promise.isComplete());
    }

    @Test
    public void rejectedTest() {
        // Arrange
        // Act
        final Promise<Integer> promise = Promises.failure(new Throwable());

        // Assert
        assertNotNull(promise);
        assertTrue(promise.isComplete());
    }

    @Test
    public void addCallbackWithResolvedTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = createStrictMock(Callback.class);

        callback.onSuccess(1);
        replay(callback);
        final Promise<Integer> promise = Promises.success(1);

        // Act
        promise.then(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void addCallbackWithRejectedTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = createStrictMock(Callback.class);
        final Throwable cause = new Throwable();

        callback.onFailure(cause);
        replay(callback);
        final Promise<Integer> promise = Promises.failure(cause);

        // Act
        promise.then(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void getWithResolvedAndToFutureTest() throws InterruptedException, ExecutionException {
        // Arrange
        final Promise<Integer> promise = Promises.success(1);
        final Future<Integer> future = Promises.toFuture(promise);

        // Act
        // Assert
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
        assertThat(future.get(), is(1));
    }

    @Test(expected = ExecutionException.class)
    public void getWithRejectedAndToFutureTest() throws InterruptedException, ExecutionException {
        // Arrange
        final Promise<Integer> promise = Promises.failure(new Throwable());
        final Future<Integer> future = Promises.toFuture(promise);

        // Act
        // Assert
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
        future.get();
    }

    @Test
    public void getWithResolvedAndToFutureAndTimeoutTest() throws InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        final Promise<Integer> promise = Promises.success(1);
        final Future<Integer> future = Promises.toFuture(promise);

        // Act
        // Assert
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
        assertThat(future.get(0, TimeUnit.MICROSECONDS), is(1));
    }

    @Test(expected = ExecutionException.class)
    public void getWithRejectedAndToFutureAndTimeoutTest() throws InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        final Promise<Integer> promise = Promises.failure(new Throwable());
        final Future<Integer> future = Promises.toFuture(promise);

        // Act
        // Assert
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
        future.get(0, TimeUnit.MICROSECONDS);
    }
}
