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

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class DefaultDeferredTests {

    @Test
    public void constructorTest() {
        // Arrange
        // Act
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Assert
        assertFalse(deferred.isComplete());
    }

    @Test
    public void setSuccessTest() {
        // Arrange
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        final boolean success = deferred.setSuccess(1);

        // Assert
        assertTrue(success);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void setSuccessWithFailedTest() {
        // Arrange
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        deferred.setFailure(new Throwable());
        final boolean success = deferred.setSuccess(1);

        // Assert
        assertFalse(success);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void setSuccessWithThenTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> onSuccess = createStrictMock(Callback.class);

        onSuccess.onSuccess(1);
        replay(onSuccess);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        deferred.then(onSuccess);

        // Act
        final boolean success = deferred.setSuccess(1);

        // Assert
        verify(onSuccess);
        assertTrue(success);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void setFailureTest() {
        // Arrange
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        final boolean failure = deferred.setFailure(new Throwable());

        // Assert
        assertTrue(failure);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void setFailureWithSucceededTest() {
        // Arrange
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        deferred.setSuccess(1);
        final boolean failure = deferred.setFailure(new Throwable());

        // Assert
        assertFalse(failure);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void setFailureWithThenTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = createStrictMock(Callback.class);
        final Throwable cause = new Throwable();

        callback.onFailure(cause);
        replay(callback);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        deferred.then(callback);

        // Act
        final boolean failure = deferred.setFailure(cause);

        // Assert
        verify(callback);
        assertTrue(failure);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void thenWithPendingTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> onSuccess = createStrictMock(Callback.class);

        replay(onSuccess);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        deferred.then(onSuccess);

        // Assert
        verify(onSuccess);
    }

    @Test
    public void thenWithSucceededTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> onSuccess = createStrictMock(Callback.class);

        onSuccess.onSuccess(1);
        replay(onSuccess);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        deferred.setSuccess(1);

        // Act
        deferred.then(onSuccess);

        // Assert
        verify(onSuccess);
    }

    @Test
    public void thenWithFailedTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = createStrictMock(Callback.class);
        final Throwable cause = new Throwable();

        callback.onFailure(cause);
        replay(callback);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        deferred.setFailure(cause);

        // Act
        deferred.then(callback);

        // Assert
        verify(callback);
    }
}
