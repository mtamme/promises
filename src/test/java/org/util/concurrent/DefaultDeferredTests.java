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
    public void trySuccessTest() {
        // Arrange
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        final boolean success = deferred.trySuccess(1);

        // Assert
        assertTrue(success);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void trySuccessWithSetFailureTest() {
        // Arrange
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        deferred.setFailure(new Throwable());
        final boolean success = deferred.trySuccess(1);

        // Assert
        assertFalse(success);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void trySuccessWithThenTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Completable<Integer> completable = createStrictMock(Completable.class);

        completable.setSuccess(1);
        replay(completable);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        deferred.then(completable);

        // Act
        final boolean success = deferred.trySuccess(1);

        // Assert
        verify(completable);
        assertTrue(success);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void setFailureTest() {
        // Arrange
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        final boolean failure = deferred.tryFailure(new Throwable());

        // Assert
        assertTrue(failure);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void setFailureWithSetSuccessTest() {
        // Arrange
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        deferred.setSuccess(1);
        final boolean failure = deferred.tryFailure(new Throwable());

        // Assert
        assertFalse(failure);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void setFailureWithThenTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Completable<Integer> completable = createStrictMock(Completable.class);
        final Throwable cause = new Throwable();

        completable.setFailure(cause);
        replay(completable);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        deferred.then(completable);

        // Act
        final boolean failure = deferred.tryFailure(cause);

        // Assert
        verify(completable);
        assertTrue(failure);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void thenTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Completable<Integer> completable = createStrictMock(Completable.class);

        replay(completable);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        deferred.then(completable);

        // Assert
        verify(completable);
    }

    @Test
    public void thenWithSetSuccessTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Completable<Integer> completable = createStrictMock(Completable.class);

        completable.setSuccess(1);
        replay(completable);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        deferred.setSuccess(1);

        // Act
        deferred.then(completable);

        // Assert
        verify(completable);
    }

    @Test
    public void thenWithSetFailureTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Completable<Integer> completable = createStrictMock(Completable.class);
        final Throwable cause = new Throwable();

        completable.setFailure(cause);
        replay(completable);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        deferred.setFailure(cause);

        // Act
        deferred.then(completable);

        // Assert
        verify(completable);
    }
}
