/*
 * Copyright (C) 2025 Dragonstb
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * See <http://www.gnu.org/licenses/gpl-2.0.html>
 */

package dev.dragonstb.trpgnarrator.virtualhost.broker;


import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCommand;
import dev.dragonstb.trpgnarrator.virtualhost.hostconnector.HostConnector;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import lombok.NonNull;

/**
 *
 * @author Dragonstb
 * @since
 */
final class SyncBrokerImp implements SynchronousBroker {

    /** Connector to the outside world. */
    private final HostConnector connector;
    private final Map<String, BrokerChannel> channels = new TreeMap<>();

    SyncBrokerImp(@NonNull HostConnector connector) {
        this.connector = connector;
    }

    @Override
    public void registerToChannel(@NonNull Receiver receiver, @NonNull String channel) {
        if(channels.containsKey(channel)) {
            channels.get(channel).addReceiver(receiver);
        }
        else {
            BrokerChannel newChannel = new BrokerChannel(channel);
            channels.put(channel, newChannel);
            newChannel.addReceiver(receiver);
        }
    }

    @Override
    public void deregisterFromChannel(@NonNull Receiver receiver, @NonNull String channelName) {
        if(!channels.containsKey(channelName)) {
            return;
        }

        BrokerChannel channel = channels.get(channelName);
        channel.removeReceiver(receiver);
        if(channel.isEmpty()) {
            channels.remove(channelName);
        }
    }

    @Override
    public void send(@NonNull Object obj, @NonNull String channelName) {
        BrokerChannel channel = channels.get(channelName);
        if(channel != null) {
            channel.send(obj);
        }
    }

    @Override
    public List<Optional<Object>> request(@NonNull String channelName, @NonNull FetchCommand fetch, boolean skipEmpties) {
        BrokerChannel channel = channels.get(channelName);
        return channel!=null ? channel.request(fetch, skipEmpties) : new ArrayList<>();
    }

    @Override
    public void update(float tpf) {
    }

}
