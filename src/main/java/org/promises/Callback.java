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
 * Defines a {@link Promise} callback.
 * 
 * @param <V> The value type
 */
public interface Callback<V> {

    /**
     * Invoked when the {@link Promise} was resolved.
     * 
     * @param value The value.
     */
    void onResolved(V value);

    /**
     * Invoked when the {@link Promise} was rejected.
     * 
     * @param throwable The {@link Throwable}.
     */
    void onRejected(Throwable throwable);
}
