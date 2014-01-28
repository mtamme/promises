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

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class CompletablePromiseTests {

    @Test
    public void constructorTest() {
        // Arrange
        // Act
        final CompletablePromise<Integer> promise = new CompletablePromise<Integer>();

        // Assert
        assertFalse(promise.isCompleted());
    }

    @Test
    public void resolveTest() {
        // Arrange
        final CompletablePromise<Integer> promise = new CompletablePromise<Integer>();

        // Act
        final boolean resolved = promise.resolve(1);

        // Assert
        assertTrue(resolved);
        assertTrue(promise.isCompleted());
    }

    @Test
    public void resolveWithRejectedTest() {
        // Arrange
        final CompletablePromise<Integer> promise = new CompletablePromise<Integer>();

        // Act
        promise.reject(new Throwable());
        final boolean resolved = promise.resolve(1);

        // Assert
        assertFalse(resolved);
        assertTrue(promise.isCompleted());
    }

    @Test
    public void resolveWithAddCallbackTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);

        callback.onResolved(1);
        replay(callback);
        final CompletablePromise<Integer> promise = new CompletablePromise<Integer>();

        promise.addCallback(callback);

        // Act
        final boolean resolved = promise.resolve(1);

        // Assert
        verify(callback);
        assertTrue(resolved);
        assertTrue(promise.isCompleted());
    }

    @Test
    public void rejectTest() {
        // Arrange
        final CompletablePromise<Integer> promise = new CompletablePromise<Integer>();

        // Act
        final boolean rejected = promise.reject(new Throwable());

        // Assert
        assertTrue(rejected);
        assertTrue(promise.isCompleted());
    }

    @Test
    public void rejectWithResolvedTest() {
        // Arrange
        final CompletablePromise<Integer> promise = new CompletablePromise<Integer>();

        // Act
        promise.resolve(1);
        final boolean rejected = promise.reject(new Throwable());

        // Assert
        assertFalse(rejected);
        assertTrue(promise.isCompleted());
    }

    @Test
    public void rejectWithAddCallbackTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);
        final Throwable throwable = new Throwable();

        callback.onRejected(throwable);
        replay(callback);
        final CompletablePromise<Integer> promise = new CompletablePromise<Integer>();

        promise.addCallback(callback);

        // Act
        final boolean rejected = promise.reject(throwable);

        // Assert
        verify(callback);
        assertTrue(rejected);
        assertTrue(promise.isCompleted());
    }

    @Test
    public void addCallbackWithPendingTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);

        replay(callback);
        final CompletablePromise<Integer> promise = new CompletablePromise<Integer>();

        // Act
        promise.addCallback(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void addCallbackWithResolvedTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);

        callback.onResolved(1);
        replay(callback);
        final CompletablePromise<Integer> promise = new CompletablePromise<Integer>();

        promise.resolve(1);

        // Act
        promise.addCallback(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void addCallbackWithRejectedTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);
        final Throwable throwable = new Throwable();

        callback.onRejected(throwable);
        replay(callback);
        final CompletablePromise<Integer> promise = new CompletablePromise<Integer>();

        promise.reject(throwable);

        // Act
        promise.addCallback(callback);

        // Assert
        verify(callback);
    }
}
