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

package dev.dragonstb.trpgnarrator.client;

import com.jme3.app.SimpleApplication;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import dev.dragonstb.trpgnarrator.client.clientconnector.ClientConnectorBuilder;
import dev.dragonstb.trpgnarrator.client.clientconnector.LocalClientForApp;
import dev.dragonstb.trpgnarrator.client.ingame.IngameAppState;
import dev.dragonstb.trpgnarrator.client.ingame.IngameCamControl;
import dev.dragonstb.trpgnarrator.client.ingame.figurine.Figurine;
import dev.dragonstb.trpgnarrator.client.ingame.figurine.FigurineBuilder;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.HostType;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.LocalVirtualHost;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VirtualHostBuilder;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public class TRPGNarratorApplication extends SimpleApplication implements RawInputListener{

    private static final Logger LOGGER = Logger.getLogger(TRPGNarratorApplication.class.getName());

    private IngameAppState ingameAppState;
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4); // TODO: configurable pool size
    private int currentPickX;
    private int currentPickY;
    private Figurine fig;


    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        AMAccessor.setAssetManager(assetManager);

        ingameAppState = new IngameAppState();
        Node ingameRoot = ingameAppState.getIngameRoot();
        ingameRoot.addLight(new DirectionalLight(Vector3f.UNIT_XYZ.negate(), ColorRGBA.White));
        ingameRoot.addLight(new AmbientLight(ColorRGBA.DarkGray));
        rootNode.attachChild(ingameRoot);

        fig = FigurineBuilder.ofId("Figurine").setColor(ColorRGBA.Blue.mult(.33f)).build();
        ingameAppState.addFigurine(fig, 35);


        IngameCamControl camControl = new IngameCamControl(cam);
        fig.getNode().addControl(camControl);

        stateManager.attach(ingameAppState);
        ingameAppState.setEnabled(true);

        // TODO: just one raw input listener that delegates inputs to the currently active targets
        inputManager.addRawInputListener(camControl);
        inputManager.addRawInputListener(this);

        // TODO: inject client connecor to the ingame app state
        HostType hostType = HostType.local;
        LocalVirtualHost host;
        LocalClientForApp client;
        try {
            host = (LocalVirtualHost)(new VirtualHostBuilder(hostType).build());
            client = (LocalClientForApp)(new ClientConnectorBuilder(hostType).build());
            client.connectToVirtualHost(host);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Caugth {0}: {1}", new Object[]{e.getClass().getSimpleName(), e.getMessage()});
        }

    }

    @Override
    public void destroy() {
        super.destroy();
        executor.shutdownNow();
    }

    // _____  raw input listener  __________________________________________________________________________________________________________

    @Override
    public void beginInput() {}

    @Override
    public void endInput() {}

    @Override
    public void onJoyAxisEvent(JoyAxisEvent evt) {}

    @Override
    public void onJoyButtonEvent(JoyButtonEvent evt) {}

    @Override
    public void onMouseMotionEvent(MouseMotionEvent evt) {
        if(evt.getX() != currentPickX || evt.getY() != currentPickY) {
            currentPickX = evt.getX();
            currentPickY = evt.getY();
            Vector2f clickCoord=new Vector2f(currentPickX, currentPickY);
            Vector3f origin = cam.getWorldCoordinates(clickCoord, 0);
            Vector3f direction = cam.getWorldCoordinates(clickCoord, 1f).subtract(origin).normalizeLocal();
            Ray ray = new Ray(origin, direction);
            ingameAppState.pickField(ray, executor);
        }
    }

    @Override
    public void onMouseButtonEvent(MouseButtonEvent evt) {
        // TODO: map mouse buttons to actions
        if(evt.getButtonIndex() == 0 && evt.isReleased()) {
            ingameAppState.setIntoMovementTo(fig, executor);
        }
    }

    @Override
    public void onKeyEvent(KeyInputEvent evt) {}

    @Override
    public void onTouchEvent(TouchEvent evt) {}

}
