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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class SampleTests {

    @Test
    public void startTest() {
        final Deferred<Void> deferred = Promises.newDeferred();

        // listener.onConnecting();
        // transport.negotiate();
        deferred.then(new Compose<Void, Void>() {
            @Override
            protected Promise<Void> doCompose(final Void value) throws Exception {
                // transport.connect();
                return null;
            }
        }).then(new Apply<Void, Void>() {
            @Override
            protected Void doApply(Void value) throws Exception {
                // manager.start(context);
                return null;
            }
        }).then(new OnComplete<Void>() {
            @Override
            protected void onSuccess(final Void value) throws Exception {
                // listener.onConnected();

            }

            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                // listener.onError();
                // listener.onDisconnected();
            }
        });
    }

    @Test
    public void stopTest() {
        final Deferred<Void> deferred = Promises.newDeferred();

        // listener.onDisconnecting();
        // manager.stop(context);
        // channel.close();
        deferred.then(new Catch<Void>() {
            @Override
            protected Void doCatch(final Throwable cause) throws Exception {
                // listener.onError();
                return null;
            }
        }).then(new Compose<Void, Void>() {
            @Override
            protected Promise<Void> doCompose(final Void value) throws Exception {
                // transport.abort();
                return null;
            }
        }).then(new OnComplete<Void>() {
            @Override
            protected void onSuccess(final Void value) throws Exception {
                // listener.onDisconnected();
            }

            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                // listener.onError();
                // listener.onDisconnected();
            }
        });
    }

    @Test
    public void reconnectTest() {
        final Deferred<Void> deferred = Promises.newDeferred();

        // listener.onReconnecting();
        // channel.close();
        deferred.then(new Catch<Void>() {
            @Override
            protected Void doCatch(final Throwable cause) throws Exception {
                // listener.onError();
                return null;
            }
        }).then(new Compose<Void, Void>() {
            @Override
            protected Promise<Void> doCompose(final Void value) throws Exception {
                // transport.reconnect();
                return null;
            }
        }).then(new OnComplete<Void>() {
            @Override
            protected void onSuccess(final Void value) throws Exception {
                // listener.onReconnected();
            }

            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                // listener.onError();
                // manager.stop(context);
            }
        });

        deferred.tryFailure(new Exception());
    }
}
