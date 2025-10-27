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

package dev.dragonstb.trpgnarrator.virtualhost.figurine;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import dev.dragonstb.trpgnarrator.virtualhost.broker.ChannelNames;
import dev.dragonstb.trpgnarrator.virtualhost.broker.Receiver;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCommand;
import dev.dragonstb.trpgnarrator.virtualhost.generic.Message;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FigurineDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FigurinesListDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;

/** The puppeteer.
 *
 * @author Dragonstb
 * @since 0.0.2
 */
final class FigurineController implements Figurines, Receiver {

    private final SynchronousBroker broker;
    private final Map<String, Figurine> figurines = new HashMap<>();

    FigurineController(@NonNull SynchronousBroker broker) {
        this.broker = broker;
        init();
    }

    private void init() {
        broker.registerToChannel(this, ChannelNames.GET_FIGURINE_DATA);

        // TODO: don't use hard coded content, but derive figurines from some data object
        int idNumber = 0;
        Figurine fig = new Figurine(String.valueOf(idNumber), ColorRGBA.Blue.mult(.33f));
        figurines.put(fig.getId(), fig);

        int fieldId = 15;
        FetchCommand fetch = new FetchCommand(FetchCodes.BOARD_FIELD_LOCATION, fieldId);
        List<Optional<Object>> list = broker.request(ChannelNames.GET_BOARD_DATA, fetch, true);

        // TODO: this reduction from List<Optional<Object>> to one instance of <T extends Object> happens fairly often. Write a centrally
        // accessible method, or offer a special method in the interface SynchronousBroker
        String code = VHostErrorCodes.V62000;
        if(list.isEmpty()){
            String use = VHostErrorCodes.assembleCodedMsg("No list of locations.", code);
            throw new NullPointerException(use);
        }

        Optional<Object> opt = list.getFirst();
        if(opt == null || opt.isEmpty()) {
            String use = VHostErrorCodes.assembleCodedMsg("No location present.", code);
            throw new NullPointerException(use);
        }

        Object obj = opt.get();
        if(!(obj instanceof Vector3f)) {
            String use = VHostErrorCodes.assembleCodedMsg("Object is not a location.", code);
            throw new ClassCastException(use);
        }

        Vector3f location = (Vector3f)opt.get();
        fig.setLocation(location);
        fig.setFieldId(fieldId);
    }

    @Override
    public void receive(Message msg) {

    }

    @Override
    public Optional<Object> request(FetchCommand fetch) {
        Optional<Object> opt = switch(fetch.getCommand()) {
            case FetchCodes.FIGURINE_FULL_LIST -> { yield Optional.of(getFigurinesDTOs()); }
            default -> {yield Optional.empty();}
        };
        return opt;
    }

    private FigurinesListDTO getFigurinesDTOs() {
        List<FigurineDTO> dtos = figurines.values().stream().map(fig -> fig.asDTO()).collect(Collectors.toList());
        FigurinesListDTO list = new FigurinesListDTO(dtos);
        return list;
    }

}
