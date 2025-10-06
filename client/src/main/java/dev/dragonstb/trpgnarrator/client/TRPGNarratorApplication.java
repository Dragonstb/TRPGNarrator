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
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import dev.dragonstb.trpgnarrator.client.ingame.IngameAppState;
import dev.dragonstb.trpgnarrator.client.ingame.IngameCamControl;
import dev.dragonstb.trpgnarrator.client.ingame.figurine.Figurine;
import dev.dragonstb.trpgnarrator.client.ingame.figurine.FigurineBuilder;

/**
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public class TRPGNarratorApplication extends SimpleApplication{

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        AMAccessor.setAssetManager(assetManager);

        IngameAppState ingameAppState = new IngameAppState();
        Node ingameRoot = ingameAppState.getIngameRoot();
        ingameRoot.addLight(new DirectionalLight(Vector3f.UNIT_XYZ.negate(), ColorRGBA.White));
        ingameRoot.addLight(new AmbientLight(ColorRGBA.DarkGray));
        rootNode.attachChild(ingameRoot);

        Figurine fig = FigurineBuilder.ofId("Figurine").setColor(ColorRGBA.Blue.mult(.33f)).build();
        ingameRoot.attachChild(fig.getNode());

        IngameCamControl camControl = new IngameCamControl(cam);
        fig.getNode().addControl(camControl);

        inputManager.addRawInputListener(camControl);

    }



}
