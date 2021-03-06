/*
 * Copyright © Martin Tamme
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.util.concurrent.promise;

/**
 * Defines a promised value.
 * 
 * @param <T> The value type.
 */
public interface Promise<T> {

    /**
     * Returns a value indicating whether the promise is complete.
     * 
     * @return A value indicating whether the promise is complete.
     */
    boolean isComplete();

    /**
     * Adds the specified completable.
     * 
     * @param completable The completable.
     */
    void then(Completable<? super T> completable);

    /**
     * Adds the specified continuation.
     * 
     * @param continuation The continuation.
     * @return The result.
     */
    <R> Promise<R> then(Continuation<? super T, ? extends R> continuation);
}
