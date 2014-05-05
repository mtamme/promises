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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 
 */
@RunWith(JUnit4.class)
public final class SampleTests {

    @Test
    public void startTest() {
        final Deferred<Void> deferred = new Deferred<Void>();

        // listener.onConnecting();
        // transport.negotiate();
        deferred.then(new RunAsync<Void, Void>() {
            @Override
            protected Promise<Void> doRun(final Void value) throws Exception {
                // transport.connect();
                return null;
            }
        }).then(new Run<Void, Void>() {
            @Override
            protected Void doRun(final Void value) throws Exception {
                // listener.onConnected();
                return null;
            }
        }).onComplete(new Call<Void>() {
            @Override
            public void onFailure(final Throwable cause) {
                // listener.onError();
                // listener.onDisconnected();
            }
        });
    }

    @Test
    public void stopTest() {
        final Deferred<Void> deferred = new Deferred<Void>();

        // listener.onDisconnecting();
        // channel.close();
        deferred.then(new Catch<Void>() {
            @Override
            protected Void doCatch(final Throwable cause) throws Exception {
                // listener.onError();
                return null;
            }
        }).then(new RunAsync<Void, Void>() {
            @Override
            protected Promise<Void> doRun(final Void value) throws Exception {
                // transport.abort();
                return null;
            }
        }).onComplete(new Call<Void>() {
            @Override
            protected void onComplete(final Void value, final Throwable cause) {
                if (cause != null) {
                    // listener.onError();
                }
                // listener.onDisconnected();
            }
        });
    }

    @Test
    public void reconnectTest() {
        final Deferred<Void> deferred = new Deferred<Void>();

        // listener.onReconnecting();
        // channel.close();
        deferred.then(new Catch<Void>() {
            @Override
            protected Void doCatch(final Throwable cause) throws Exception {
                // listener.onError();
                return null;
            }
        }).then(new RunAsync<Void, Void>() {
            @Override
            protected Promise<Void> doRun(final Void value) throws Exception {
                // transport.reconnect();
                return null;
            }
        }).onComplete(new Call<Void>() {
            @Override
            public void onSuccess(final Void value) {
                // listener.onReconnected();
            }

            @Override
            public void onFailure(final Throwable cause) {
                // listener.onError();
            }
        });

        deferred.setFailure(new Exception());
    }
}
